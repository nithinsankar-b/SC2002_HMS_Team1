
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

/**
 * The {@code ScheduleService} class manages the scheduling of appointments for doctors.
 * It handles loading and saving schedules from/to a CSV file, booking appointments,
 * and modifying the availability status of time slots.
 */
public class ScheduleService {
    private static final String SCHEDULE_FILE = "data/schedule.csv"; // Path to the schedule CSV file
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private final Map<String, Map<LocalDate, Map<LocalTime, Schedule>>> scheduleMap; // doctorID -> (date -> (time -> Schedule))

    /**
     * Constructs a {@code ScheduleService} and loads the schedule from the CSV file.
     */
    public ScheduleService() {
        this.scheduleMap = new HashMap<>();
        loadSchedule(); // Load schedules from CSV on initialization
    }
    public Map<String, Map<LocalDate, Map<LocalTime, Schedule>>> getScheduleMap() {
        return scheduleMap;
    }

    /**
     * Loads the schedule data from the CSV file into memory.
     * The file should contain data in the format: doctorID, date, time, status.
     */
    private void loadSchedule() {
        try (BufferedReader br = Files.newBufferedReader(Paths.get(SCHEDULE_FILE))) {
            String line;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                
                if (parts.length == 4) {
                    try {
                        String doctorID = parts[0];
                        LocalDate date = LocalDate.parse(parts[1], DATE_FORMAT);
                        LocalTime timeSlot = LocalTime.parse(parts[2], TIME_FORMAT);
                        String status = parts[3]; // Status (Available, Blocked, or Patient ID)

                        Schedule schedule = new Schedule(doctorID, date, timeSlot, status);
                        scheduleMap.computeIfAbsent(doctorID, k -> new HashMap<>())
                                   .computeIfAbsent(date, k -> new HashMap<>())
                                   .put(timeSlot, schedule);
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

    public boolean cancelAppointment(String doctorID, LocalDate date, LocalTime timeSlot, String patientID) {
        Map<LocalDate, Map<LocalTime, Schedule>> doctorSchedule = scheduleMap.get(doctorID);
        if (doctorSchedule != null) {
            Schedule schedule = doctorSchedule.get(date).get(timeSlot);
            if (schedule != null && patientID.equals(schedule.getStatus())) {
                schedule.setStatus("Available"); // Reset status to "Available"
                saveSchedule(); // Save changes to CSV
                System.out.println("Appointment canceled and time slot is now available.");

            } else {
                System.out.println("No appointment found for the specified patient.");

            }
            return true;
        }
        else
        {
            return false;
        }
    }

    /**
     * Books an appointment for a specific doctor, date, and time slot.
     *
     * @param doctorID the ID of the doctor
     * @param date the date of the appointment
     * @param timeSlot the time slot for the appointment
     * @param patientID the ID of the patient
     * @return {@code true} if the booking was successful, {@code false} otherwise
     */
    public boolean bookAppointment(String doctorID, LocalDate date, LocalTime timeSlot, String patientID) {
        Map<LocalDate, Map<LocalTime, Schedule>> doctorSchedule = scheduleMap.get(doctorID);
        if (doctorSchedule != null) {
            Schedule schedule = doctorSchedule.get(date).get(timeSlot);
            if (schedule != null && "Available".equals(schedule.getStatus())) {
                schedule.setStatus(patientID); // Update status with patient ID
                saveSchedule(); // Save changes to CSV
                return true; // Booking successful
            }
        }
        return false; // Booking failed
    }

    /**
     * Sets a specified time slot as available for a doctor.
     *
     * @param doctorID the ID of the doctor
     * @param date the date of the time slot
     * @param timeSlot the time slot to set as available
     */
    public void setAvailable(String doctorID, LocalDate date, LocalTime timeSlot) {
        Map<LocalDate, Map<LocalTime, Schedule>> doctorSchedule = scheduleMap.get(doctorID);
        if (doctorSchedule != null) {
            Schedule schedule = doctorSchedule.get(date).get(timeSlot);
            if (schedule != null && "Blocked".equals(schedule.getStatus())) {
                schedule.setStatus("Available"); // Change status to Available
                saveSchedule(); // Save changes to CSV
            } else {
                System.out.println("Already available");
            }
        }
    }

    /**
     * Sets a specified time slot as unavailable (blocked) for a doctor.
     *
     * @param doctorID the ID of the doctor
     * @param date the date of the time slot
     * @param timeSlot the time slot to set as unavailable
     */
    public void setUnavailable(String doctorID, LocalDate date, LocalTime timeSlot) {
        Map<LocalDate, Map<LocalTime, Schedule>> doctorSchedule = scheduleMap.get(doctorID);
        if (doctorSchedule != null) {
            Schedule schedule = doctorSchedule.get(date).get(timeSlot);
            if (schedule != null && "Available".equals(schedule.getStatus())) {
                schedule.setStatus("Blocked"); // Change status to Blocked
                saveSchedule(); // Save changes to CSV
            } else {
                System.out.println("Already blocked");
            }
        }
    }

    /**
     * Prints the schedule for a specific doctor, sorted by date and time.
     *
     * @param doctorID the ID of the doctor whose schedule to print
     */
    public void printSchedule(String doctorID) {
        Map<LocalDate, Map<LocalTime, Schedule>> doctorSchedule = scheduleMap.get(doctorID);
        if (doctorSchedule != null) {
            Map<LocalDate, Map<LocalTime, Schedule>> sortedSchedule = new TreeMap<>(doctorSchedule);

            for (Map.Entry<LocalDate, Map<LocalTime, Schedule>> dateEntry : sortedSchedule.entrySet()) {
                LocalDate date = dateEntry.getKey();
                System.out.println("Date: " + date);

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

    /**
     * Prints upcoming appointments for a specific doctor.
     *
     * @param doctorID the ID of the doctor whose upcoming appointments to print
     */
    public void printUpcomingAppointments(String doctorID) {
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        List<String> upcomingAppointments = new ArrayList<>();

        Map<LocalDate, Map<LocalTime, Schedule>> doctorSchedule = scheduleMap.get(doctorID);

        if (doctorSchedule != null) {
            for (Map.Entry<LocalDate, Map<LocalTime, Schedule>> dateEntry : doctorSchedule.entrySet()) {
                LocalDate date = dateEntry.getKey();

                if (date.isAfter(today) || (date.equals(today) && dateEntry.getValue().keySet().stream().anyMatch(time -> time.isAfter(currentTime)))) {
                    for (Map.Entry<LocalTime, Schedule> timeEntry : dateEntry.getValue().entrySet()) {
                        LocalTime timeSlot = timeEntry.getKey();
                        Schedule schedule = timeEntry.getValue();
                        String status = schedule.getStatus();

                        if (!"Available".equals(status) && !"Blocked".equals(status) && (date.isAfter(today) || (date.equals(today) && timeSlot.isAfter(currentTime)))) {
                            upcomingAppointments.add("Patient ID: " + status + ", Date: " + date + ", Time Slot: " + timeSlot);
                        }
                    }
                }
            }
        } else {
            System.out.println("No schedule found for Doctor ID: " + doctorID);
            return;
        }

        if (upcomingAppointments.isEmpty()) {
            System.out.println("No upcoming appointments found for Doctor ID: " + doctorID);
        } else {
            for (String appointment : upcomingAppointments) {
                System.out.println(appointment);
            }
        }
    }

    /**
     * Saves the current schedule state to the CSV file.
     */
    private void saveSchedule() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCHEDULE_FILE))) {
            for (Map.Entry<String, Map<LocalDate, Map<LocalTime, Schedule>>> entry : scheduleMap.entrySet()) {
                String doctorID = entry.getKey();
                Map<LocalDate, Map<LocalTime, Schedule>> sortedDateSchedule = new TreeMap<>(entry.getValue());
                for (Map.Entry<LocalDate, Map<LocalTime, Schedule>> dateEntry : sortedDateSchedule.entrySet()) {
                    LocalDate date = dateEntry.getKey();
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
