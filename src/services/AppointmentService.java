package services;

import models.Appointment;
import enums.AppointmentStatus;
import enums.MedicationStatus;
import models.Medication;
import models.Schedule;

import java.io.*;
import interfaces.IAppointmentService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import services.ScheduleService;

/**
 * Service class for managing appointments in a hospital management system.
 * This class provides functionality to schedule, cancel, reschedule,
 * and view appointments, as well as to record outcomes and manage
 * medication statuses.
 */
public class AppointmentService implements IAppointmentService {
    private static final String APPOINTMENT_FILE = "data/appointment.csv";
    private List<Appointment> appointments;

    /**
     * Constructor that initializes the AppointmentService and loads
     * existing appointments from a CSV file.
     */
    public AppointmentService() {
        this.appointments = new ArrayList<>();
        createCSVIfNotExists();
        loadAppointmentsFromCSV();
    }

    /**
     * Schedules a new appointment.
     *
     * @param appointment The appointment to be scheduled.
     * @return true if the appointment was successfully scheduled, false otherwise.
     */
    @Override
    public boolean scheduleAppointment(Appointment appointment) {
        boolean success = appointments.add(appointment);
        if (success) {
            saveAppointmentsToCSV();
        }
        return success;
    }

    /**
     * Cancels an existing appointment.
     *
     * @param appointmentId The ID of the appointment to be canceled.
     * @return true if the appointment was successfully canceled, false otherwise.
     */
    @Override
    public boolean cancelAppointment(String appointmentId) {
        boolean success = appointments.removeIf(appointment -> appointment.getAppointmentId().equals(appointmentId));
        if (success) {
            saveAppointmentsToCSV();
        }
        return success;
    }

    /**
     * Reschedules an existing appointment.
     *
     * @param appointmentId  The ID of the appointment to be rescheduled.
     * @param newAppointment The new appointment details.
     * @return true if the appointment was successfully rescheduled, false otherwise.
     */
    @Override
    public boolean rescheduleAppointment(String appointmentId, Appointment newAppointment) {
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getAppointmentId().equals(appointmentId)) {
                appointments.set(i, newAppointment);
                saveAppointmentsToCSV();
                return true;
            }
        }
        return false;
    }

    /**
     * Views all scheduled appointments.
     *
     * @return A list of all scheduled appointments.
     */
    @Override
    public List<Appointment> viewScheduledAppointments() {
        return new ArrayList<>(appointments); // Return a copy to avoid modification from outside
    }

    /**
     * Retrieves an appointment by its ID.
     *
     * @param appointmentId The ID of the appointment to retrieve.
     * @return The appointment if found, null otherwise.
     */
    @Override
    public Appointment getAppointment(String appointmentId) {
        return appointments.stream()
                .filter(appointment -> appointment.getAppointmentId().equals(appointmentId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves an appointment by its ID. This method reuses the getAppointment method.
     *
     * @param appointmentId The ID of the appointment to retrieve.
     * @return The appointment if found, null otherwise.
     */
    @Override
    public Appointment getAppointmentById(String appointmentId) {
        return getAppointment(appointmentId); // Reuse existing method
    }

    /**
     * Records the outcome of an appointment, including services provided
     * and prescribed medications.
     *
     * @param appointmentId         The ID of the appointment to record the outcome for.
     * @param serviceProvided       The services provided during the appointment.
     * @param prescribedMedications The list of medications prescribed during the appointment.
     * @param consultationNotes     Additional notes from the consultation.
     */
    @Override
    public void recordAppointmentOutcome(String appointmentId, String serviceProvided, List<Medication> prescribedMedications, List<Integer> prescribedQuantities, String consultationNotes) {
        Appointment appointment = getAppointment(appointmentId);

        // Check if the appointment exists and is in a PENDING state
        if (appointment != null && appointment.getStatus() == AppointmentStatus.PENDING) {
            appointment.setMedicationStatus(MedicationStatus.PENDING);
            appointment.setStatus(AppointmentStatus.COMPLETED);
            appointment.setServiceProvided(serviceProvided);
            appointment.setConsultationNotes(consultationNotes);

            // Iterate through the prescribed medications and quantities
            for (int i = 0; i < prescribedMedications.size(); i++) {
                Medication medication = prescribedMedications.get(i);
                int quantity = prescribedQuantities.get(i);  // Get the corresponding quantity

                // Update the medication quantity and add it to the appointment
                medication.setQuantity(quantity);
                appointment.addMedication(medication, quantity);
            }

            // Save the updated appointments list to CSV
            saveAppointmentsToCSV();
        } else {
            System.out.println("Appointment not found or already completed.");
        }
    }

    /**
     * Updates the medication status for a given appointment to DISPENSED.
     *
     * @param appointmentId The ID of the appointment for which to update the medication status.
     */
    public void updateMedicationStatus(String appointmentId) {
        Appointment appointment = getAppointment(appointmentId);
        if (appointment != null) {
            appointment.setMedicationStatus(MedicationStatus.DISPENSED);
            saveAppointmentsToCSV();
        }
    }

    /**
     * Retrieves available appointment slots for a specific doctor on a specific date.
     *
     * @param doctorId The ID of the doctor.
     * @param date     The date for which to find available slots.
     * @return A list of available LocalDateTime slots for the specified doctor and date.
     */
    /*
    @Override
    public List<LocalDateTime> getAvailableSlots(String doctorId, LocalDate date) {
        List<LocalDateTime> availableSlots = new ArrayList<>();
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(17, 0);

        for (LocalTime time = startTime; time.isBefore(endTime); time = time.plusMinutes(30)) {
            LocalDateTime slot = LocalDateTime.of(date, time);
            boolean isOccupied = appointments.stream()
                    .anyMatch(appointment -> appointment.getDoctorId().equals(doctorId) && appointment.getAppointmentDateTime().equals(slot)&&appointment);
            if (!isOccupied) {
                availableSlots.add(slot);
            }
        }
        return availableSlots;
    }
    */
    public List<LocalDateTime> getAvailableSlots(String doctorId, LocalDate date) {
        List<LocalDateTime> availableSlots = new ArrayList<>();
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(17, 0);
        ScheduleService scheduleService = new ScheduleService();

        // Fetch the schedule for the doctor on the given date
        Map<LocalDate, Map<LocalTime, Schedule>> doctorSchedule = scheduleService.getScheduleMap().get(doctorId);
        if (doctorSchedule != null) {
            Map<LocalTime, Schedule> daySchedule = doctorSchedule.get(date);
            if (daySchedule != null) {
                // Iterate over all the time slots between start and end time
                for (LocalTime time = startTime; time.isBefore(endTime); time = time.plusMinutes(30)) {
                    LocalDateTime slot = LocalDateTime.of(date, time);
                    Schedule schedule = daySchedule.get(time);

                    // If the time slot is available (status is "Available")
                    if (schedule != null && "Available".equals(schedule.getStatus())) {
                        availableSlots.add(slot);  // Add to available slots
                    }
                }
            }
        }

        return availableSlots;
    }

    // Create CSV file if it does not exist
    private void createCSVIfNotExists() {
        File file = new File(APPOINTMENT_FILE);
        if (!file.exists()) {
            try {
                file.createNewFile();
                // Write header to the CSV file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write("appointmentId,patientId,doctorId,appointmentDateTime,status,consultationNotes,serviceProvided,medications,quantity,medicationStatus");
                    writer.newLine();
                }
            } catch (IOException e) {
                System.err.println("Error creating CSV file at path: " + APPOINTMENT_FILE + " - " + e.getMessage());
            }
        }
    }

    // Load appointments from CSV file
    public void loadAppointmentsFromCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader(APPOINTMENT_FILE))) {
            String line;
            // Skip header line
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                Appointment appointment = Appointment.fromString(line);
                if (appointment != null) {
                    appointments.add(appointment);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading appointments from CSV at path: " + APPOINTMENT_FILE + " - " + e.getMessage());
        }
    }

    // Save appointments to CSV file
    public void saveAppointmentsToCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(APPOINTMENT_FILE))) {
            // Write header to the CSV file
            writer.write("appointmentId,patientId,doctorId,appointmentDateTime,status,consultationNotes,serviceProvided,medications,quantity,medicationStatus");
            writer.newLine();
            for (Appointment appointment : appointments) {
                writer.write(appointment.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing appointments to CSV at path: " + APPOINTMENT_FILE + " - " + e.getMessage());
        }
    }

    public Appointment findAppointment(String patientId, String doctorId, LocalDate date, LocalTime timeSlot) {
        for (Appointment appointment : appointments) {
            if (appointment.getPatientId().equals(patientId) &&
                    appointment.getDoctorId().equals(doctorId) &&
                    appointment.getAppointmentDateTime().toLocalDate().equals(date) &&
                    appointment.getAppointmentDateTime().toLocalTime().equals(timeSlot)) {

                return appointment;
            }
        }
        return null; // Return null if no matching appointment is found
    }

    public void updateAppointment(Appointment updatedAppointment) {
        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            if (appointment.getAppointmentId().equals(updatedAppointment.getAppointmentId())) {
                appointments.set(i, updatedAppointment);
                System.out.println("Appointment updated successfully.");
                saveAppointmentsToCSV();
                return;
            }
        }
        System.out.println("Appointment not found.");
    }

}
