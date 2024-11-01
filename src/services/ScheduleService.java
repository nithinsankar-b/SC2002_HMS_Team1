
package services;

import models.Schedule;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ScheduleService {
    private static final String SCHEDULE_FILE = "data/schedule.csv"; // Adjust the path as needed
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private final Map<String, Map<LocalDate, Map<LocalTime, Schedule>>> scheduleMap; // doctorID -> (date -> (time -> Schedule))

    public ScheduleService() {
        this.scheduleMap = new HashMap<>();
        loadSchedule(); // Load schedules from CSV on initialization
    }

    private void loadSchedule() {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(SCHEDULE_FILE))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                
                if (parts.length == 4) { // Ensure we have all necessary parts
                    try {
                        String doctorID = parts[0];
                        LocalDate date = LocalDate.parse(parts[1], DATE_FORMAT);
                        LocalTime timeSlot = LocalTime.parse(parts[2], TIME_FORMAT);
                        String status = parts[3]; // Read status (Available, Blocked, or Patient ID)

                        // Initialize the schedule structure with the Schedule object
                        Schedule schedule = new Schedule(doctorID, date, timeSlot, status);
                        scheduleMap.computeIfAbsent(doctorID, k -> new HashMap<>())
                                   .computeIfAbsent(date, k -> new HashMap<>())
                                   .put(timeSlot, schedule); // Store the Schedule object for each time slot
                       
                    } catch (DateTimeParseException e) {
                        System.out.println("Error parsing date or time in line: " + line);
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean bookAppointment(String doctorID, LocalDate date, LocalTime timeSlot, String patientID) {
        Map<LocalDate, Map<LocalTime, Schedule>> doctorSchedule = scheduleMap.get(doctorID);
        if (doctorSchedule != null) {
            Schedule schedule = doctorSchedule.get(date).get(timeSlot);
            if (schedule != null && "Available".equals(schedule.getStatus())) {
                schedule.setStatus(patientID); // Replace "Available" with patient ID
                saveSchedule(); // Save changes to CSV
                return true; // Booking successful
            }
        }
        return false; // Booking failed
    }

    public void setAvailable(String doctorID, LocalDate date, LocalTime timeSlot) {
        Map<LocalDate, Map<LocalTime, Schedule>> doctorSchedule = scheduleMap.get(doctorID);
        if (doctorSchedule != null) {
            Schedule schedule = doctorSchedule.get(date).get(timeSlot);
            if (schedule != null) {
                String currentStatus = schedule.getStatus();
                if ("Blocked".equals(currentStatus)) {
                    schedule.setStatus("Available"); // Change status to Available
                    saveSchedule(); // Save changes to CSV
                } else {
                    System.out.println("Already available");
                }
            }
        }
    }

    public void setUnavailable(String doctorID, LocalDate date, LocalTime timeSlot) {
        Map<LocalDate, Map<LocalTime, Schedule>> doctorSchedule = scheduleMap.get(doctorID);
        if (doctorSchedule != null) {
            Schedule schedule = doctorSchedule.get(date).get(timeSlot);
            if (schedule != null) {
                String currentStatus = schedule.getStatus();
                if ("Available".equals(currentStatus)) {
                    schedule.setStatus("Blocked"); // Change status to Blocked
                    saveSchedule(); // Save changes to CSV
                } else {
                    System.out.println("Already blocked");
                }
            }
        }
    }
    
 
    public void printSchedule(String doctorID) {
        Map<LocalDate, Map<LocalTime, Schedule>> doctorSchedule = scheduleMap.get(doctorID);
        if (doctorSchedule != null) {
            // Use a TreeMap to sort dates
            Map<LocalDate, Map<LocalTime, Schedule>> sortedSchedule = new TreeMap<>(doctorSchedule);

            for (Map.Entry<LocalDate, Map<LocalTime, Schedule>> dateEntry : sortedSchedule.entrySet()) {
                LocalDate date = dateEntry.getKey();
                System.out.println("Date: " + date);

                // Use a TreeMap to sort time slots
                Map<LocalTime, Schedule> sortedTimeSlots = new TreeMap<>(dateEntry.getValue());
                for (Map.Entry<LocalTime, Schedule> timeEntry : sortedTimeSlots.entrySet()) {
                    LocalTime timeSlot = timeEntry.getKey();
                    Schedule schedule = timeEntry.getValue();
                    System.out.println("Time: " + timeSlot + " - Status: " + schedule.getStatus());
                }
                System.out.println();
            }
        } else {
            System.out.println("No schedule found for Doctor ID: " + doctorID);
        }
    }
    
    
   public void printUpcomingAppointments(String doctorID) {
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        List<String> upcomingAppointments = new ArrayList<>();

        // Get the doctor's schedule based on the provided doctorID
        Map<LocalDate, Map<LocalTime, Schedule>> doctorSchedule = scheduleMap.get(doctorID);

        if (doctorSchedule != null) {
            for (Map.Entry<LocalDate, Map<LocalTime, Schedule>> dateEntry : doctorSchedule.entrySet()) {
                LocalDate date = dateEntry.getKey();

                // Only consider dates that are in the future, or today's date with a future time slot
                if (date.isAfter(today) || (date.equals(today) && dateEntry.getValue().keySet().stream().anyMatch(time -> time.isAfter(currentTime)))) {
                    for (Map.Entry<LocalTime, Schedule> timeEntry : dateEntry.getValue().entrySet()) {
                        LocalTime timeSlot = timeEntry.getKey();
                        Schedule schedule = timeEntry.getValue();
                        String status = schedule.getStatus();

                        // Check if the status is neither "Available" nor "Blocked"
                        if (!"Available".equals(status) && !"Blocked".equals(status) && (date.isAfter(today) || (date.equals(today) && timeSlot.isAfter(currentTime)))) {
                            upcomingAppointments.add("Patient ID: " + status + ", Date: " + date + ", Time Slot: " + timeSlot);
                        }
                    }
                }
            }
        } else {
            System.out.println("No schedule found for Doctor ID: " + doctorID);
            return; // Exit if the doctor ID is not found
        }

        // Print the upcoming appointments
        if (upcomingAppointments.isEmpty()) {
            System.out.println("No upcoming appointments found for Doctor ID: " + doctorID);
        } else {
            for (String appointment : upcomingAppointments) {
                System.out.println(appointment);
            }
        }
    }
    
    
    
    private void saveSchedule() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCHEDULE_FILE))) {
            // Iterate over each doctor in the scheduleMap
            for (Map.Entry<String, Map<LocalDate, Map<LocalTime, Schedule>>> entry : scheduleMap.entrySet()) {
                String doctorID = entry.getKey();

                // Sort dates using TreeMap
                Map<LocalDate, Map<LocalTime, Schedule>> sortedDateSchedule = new TreeMap<>(entry.getValue());
                for (Map.Entry<LocalDate, Map<LocalTime, Schedule>> dateEntry : sortedDateSchedule.entrySet()) {
                    LocalDate date = dateEntry.getKey();

                    // Sort time slots using TreeMap
                    Map<LocalTime, Schedule> sortedTimeSlots = new TreeMap<>(dateEntry.getValue());
                    for (Map.Entry<LocalTime, Schedule> timeEntry : sortedTimeSlots.entrySet()) {
                        LocalTime timeSlot = timeEntry.getKey();
                        String status = timeEntry.getValue().getStatus();
                        writer.write(doctorID + "," + date.format(DATE_FORMAT) + "," + timeSlot.format(TIME_FORMAT) + "," + status);
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
