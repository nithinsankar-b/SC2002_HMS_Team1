
package services;

import models.Schedule;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import interfaces.IScheduleService;

/**
 * The {@code ScheduleService} class manages the scheduling of appointments for doctors.
 * It handles loading and saving schedules from/to a CSV file, booking appointments,
 * and modifying the availability status of time slots.
 */
public class ScheduleService implements IScheduleService{
    private static final String SCHEDULE_FILE = "data/schedule.csv"; // Path to the schedule CSV file
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
    private final Map<String, Map<LocalDate, Map<LocalTime, Schedule>>> scheduleMap; // doctorID -> (date -> (time -> Schedule))

    /**
     * Constructs a {@code ScheduleService} and loads the schedule from the CSV file.
     */
   
   /* public ScheduleService() {
        this.scheduleMap = new HashMap<>();
        
        // Check if schedule CSV already exists; generate if not
        if (!Files.exists(Paths.get(SCHEDULE_FILE))) {
            generateSchedule("D001", LocalDate.of(2024, 11, 11), LocalDate.of(2024, 12, 31), "Available");
            generateSchedule("D002", LocalDate.of(2024, 11, 11), LocalDate.of(2024, 12, 31), "Available");
        }
        
        loadSchedule(); // Load the schedule data into memory
    }*/
    public ScheduleService() {
        this.scheduleMap = new HashMap<>();

        if (!Files.exists(Paths.get(SCHEDULE_FILE))) {
            // If schedule.csv doesn't exist, generate schedule for all doctors
            generateSchedulesForAllDoctors();
        } else {
            loadSchedule(); // Load any existing schedules
            ensureSchedulesExist(); // Ensure schedules exist for all doctors
        }
    }

    // Generates schedules for all doctors in doctors.csv
    private void generateSchedulesForAllDoctors() {
        List<String> doctorIds = loadDoctorIds(); // Load doctor IDs from doctors.csv
        LocalDate today = LocalDate.now();
        LocalDate endOfYear = LocalDate.of(today.getYear(), 12, 31);

        for (String doctorID : doctorIds) {
            generateSchedule(doctorID, today, endOfYear, "Available");
        }
    }

    // Ensures all doctors in doctors.csv have a schedule in schedule.csv
    private void ensureSchedulesExist() {
        List<String> doctorIds = loadDoctorIds(); // Load doctor IDs from doctors.csv
        LocalDate today = LocalDate.now();
        LocalDate endOfYear = LocalDate.of(today.getYear(), 12, 31);

        for (String doctorID : doctorIds) {
            if (!scheduleMap.containsKey(doctorID)) {
                generateSchedule(doctorID, today, endOfYear, "Available");
            }
        }
    }

    // Load doctor IDs from doctors.csv
    private List<String> loadDoctorIds() {
        List<String> doctorIds = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("data/doctor.csv"))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                doctorIds.add(parts[0]); // Assuming the doctor ID is in the first column
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doctorIds;
    }
    
    private void generateSchedule(String doctorID, LocalDate startDate, LocalDate endDate, String status) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(SCHEDULE_FILE, true))) {
            if (Files.size(Paths.get(SCHEDULE_FILE)) == 0) {
                // Write header if file is empty
                writer.write("doctorID,date,time,status");
                writer.newLine();
            }

            // Loop through each date
            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                LocalTime timeSlot = LocalTime.of(9, 0);

                while (!timeSlot.isAfter(LocalTime.of(16, 30))) {
                    // Write doctorID, date as string, time as string, and status
                    writer.write(String.join(",", doctorID, date.format(DATE_FORMAT), timeSlot.format(TIME_FORMAT), status));
                    writer.newLine();

                    // Move to the next time slot
                    timeSlot = timeSlot.plusMinutes(30);
                }
            }
            System.out.println("Schedule generated for " + doctorID);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            } else if(schedule != null && "Available".equals(schedule.getStatus())){
                System.out.println("Already available");
            }
        }
    }
    public void setAvailable1(String doctorID, LocalDate date, LocalTime timeSlot) {
        Map<LocalDate, Map<LocalTime, Schedule>> doctorSchedule = scheduleMap.get(doctorID);
        if (doctorSchedule != null) {
            Schedule schedule = doctorSchedule.get(date).get(timeSlot);
            System.out.println(schedule.getStatus());
            if (schedule != null && schedule.getStatus().startsWith("P")){
                schedule.setStatus("Available"); // Change status to Available
                saveSchedule(); // Save changes to CSV
            } else if(schedule != null && "Available".equals(schedule.getStatus())){
                System.out.println("Already available");
            }
        }
    }
    public void setUnavailable2(String doctorID, LocalDate date, LocalTime timeSlot, String patientID) {
        Map<LocalDate, Map<LocalTime, Schedule>> doctorSchedule = scheduleMap.get(doctorID);
        if (doctorSchedule != null) {
            Schedule schedule = doctorSchedule.get(date).get(timeSlot);
            if (schedule != null && "Available".equals(schedule.getStatus())) {
                schedule.setStatus(patientID); // Change status to Blocked
                saveSchedule(); // Save changes to CSV
            } else if(schedule != null && "Blocked".equals(schedule.getStatus()))
                System.out.println("Already blocked");
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
            } else if(schedule != null && "Blocked".equals(schedule.getStatus()))
                System.out.println("Already blocked");
            }
        }
    

    /**
     * Prints the schedule for a specific doctor, sorted by date and time.
     *
     * @param doctorID the ID of the doctor whose schedule to print
     */
    /*public void printSchedule(String doctorID) {
        Map<LocalDate, Map<LocalTime, Schedule>> doctorSchedule = scheduleMap.get(doctorID);
        LocalDate currentDate = LocalDate.now();
        
        if (doctorSchedule != null) {
            Map<LocalDate, Map<LocalTime, Schedule>> sortedSchedule = new TreeMap<>(doctorSchedule);

            for (Map.Entry<LocalDate, Map<LocalTime, Schedule>> dateEntry : sortedSchedule.entrySet()) {
                LocalDate date = dateEntry.getKey();
                
                // Only print schedules for the current date and future dates
                if (!date.isBefore(currentDate)) {
                    System.out.println("Date: " + date);

                    Map<LocalTime, Schedule> sortedTimeSlots = new TreeMap<>(dateEntry.getValue());
                    for (Map.Entry<LocalTime, Schedule> timeEntry : sortedTimeSlots.entrySet()) {
                        LocalTime timeSlot = timeEntry.getKey();
                        Schedule schedule = timeEntry.getValue();
                        System.out.println("Time: " + timeSlot + " - Status: " + schedule.getStatus());
                    }
                    System.out.println();
                }
            }
        } else {
            System.out.println("No schedule found for Doctor ID: " + doctorID);
        }
    }*/
    /*public void printSchedule(String doctorID) {
        Map<LocalDate, Map<LocalTime, Schedule>> doctorSchedule = scheduleMap.get(doctorID);
        LocalDate currentDate = LocalDate.now();

        if (doctorSchedule != null) {
            Map<LocalDate, Map<LocalTime, Schedule>> sortedSchedule = new TreeMap<>(doctorSchedule);

            // Iterate through sorted schedule for this doctor
            for (Map.Entry<LocalDate, Map<LocalTime, Schedule>> dateEntry : sortedSchedule.entrySet()) {
                LocalDate date = dateEntry.getKey();

                // Only print schedules for the current date and future dates
                if (!date.isBefore(currentDate)) {
                    System.out.println("Date: " + date);

                    // Sort time slots for the current date
                    Map<LocalTime, Schedule> sortedTimeSlots = new TreeMap<>(dateEntry.getValue());
                    for (Map.Entry<LocalTime, Schedule> timeEntry : sortedTimeSlots.entrySet()) {
                        LocalTime timeSlot = timeEntry.getKey();
                        Schedule schedule = timeEntry.getValue();
                        System.out.println("Time: " + timeSlot + " - Status: " + schedule.getStatus());
                    }
                    System.out.println();
                }
            }
        } else {
            System.out.println("No schedule found for Doctor ID: " + doctorID);
        }
    } */

    public void printSchedule(String doctorID) {
        Map<LocalDate, Map<LocalTime, Schedule>> doctorSchedule = scheduleMap.get(doctorID);
        LocalDate currentDate = LocalDate.now();

        // Ask user for the date
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the day (1-31): ");
        int day = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter the month (1-12): ");
        int month = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter the year (YYYY): ");
        int year = Integer.parseInt(scanner.nextLine());

        // Create the selected date
        LocalDate selectedDate = LocalDate.of(year, month, day);

        // Check if the selected date is within the allowed range (18 Nov 2024 to 18 Dec 2024)
        LocalDate startDate = LocalDate.of(2024, 11, 18);
        LocalDate endDate = LocalDate.of(2024, 12, 18);

        if (selectedDate.isBefore(startDate) || selectedDate.isAfter(endDate)) {
            System.out.println("Selected date is outside one month range");
            return;
        }

        // Check if the doctor has a schedule
        if (doctorSchedule != null && doctorSchedule.containsKey(selectedDate)) {
            Map<LocalTime, Schedule> timeSlots = doctorSchedule.get(selectedDate);

            // Sort time slots by time
            Map<LocalTime, Schedule> sortedTimeSlots = new TreeMap<>(timeSlots);

            System.out.println("Schedule for Doctor ID: " + doctorID + " on " + selectedDate);

            // Print the schedule for the selected date
            for (Map.Entry<LocalTime, Schedule> timeEntry : sortedTimeSlots.entrySet()) {
                LocalTime timeSlot = timeEntry.getKey();
                Schedule schedule = timeEntry.getValue();
                System.out.println("Time: " + timeSlot + " - Status: " + schedule.getStatus());
            }
        } else {
            System.out.println("No schedule found for Doctor ID: " + doctorID + " on " + selectedDate);
        }
    }



    /**
     * Prints upcoming appointments for a specific doctor.
     *
     * @param doctorID the ID of the doctor whose upcoming appointments to print
     */
    /*public void printUpcomingAppointments(String doctorID) {
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
    }*/
    public void printUpcomingAppointments(String doctorID) {
        LocalDate today = LocalDate.now();
        LocalTime currentTime = LocalTime.now();
        List<String> upcomingAppointments = new ArrayList<>();

        Map<LocalDate, Map<LocalTime, Schedule>> doctorSchedule = scheduleMap.get(doctorID);

        if (doctorSchedule != null) {
            // Iterate through the schedule of the doctor
            for (Map.Entry<LocalDate, Map<LocalTime, Schedule>> dateEntry : doctorSchedule.entrySet()) {
                LocalDate date = dateEntry.getKey();

                // Skip past dates
                if (date.isAfter(today) || (date.equals(today) && !dateEntry.getValue().isEmpty())) {
                    // Iterate through time slots for the given date
                    for (Map.Entry<LocalTime, Schedule> timeEntry : dateEntry.getValue().entrySet()) {
                        LocalTime timeSlot = timeEntry.getKey();
                        Schedule schedule = timeEntry.getValue();
                        String status = schedule.getStatus();

                        // Check if appointment exists (not available or blocked)
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

        // Display upcoming appointments
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
            writer.write("doctorID,date,time,status"); // CSV header line
            writer.newLine();

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
