package services;

import models.Appointment;
import enums.AppointmentStatus;
import enums.MedicationStatus;
import models.Medication;

import java.io.*;
import interfaces.IAppointmentService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AppointmentService implements IAppointmentService {
    private static final String APPOINTMENT_FILE = "data/appointment.csv";
    private List<Appointment> appointments;

    public AppointmentService() {
        this.appointments = new ArrayList<>();
        createCSVIfNotExists();
        loadAppointmentsFromCSV();
    }

    @Override
    public boolean scheduleAppointment(Appointment appointment) {
        boolean success = appointments.add(appointment);
        if (success) {
            saveAppointmentsToCSV();
        }
        return success;
    }

    @Override
    public boolean cancelAppointment(String appointmentId) {
        boolean success = appointments.removeIf(appointment -> appointment.getAppointmentId().equals(appointmentId));
        if (success) {
            saveAppointmentsToCSV();
        }
        return success;
    }

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

    @Override
    public List<Appointment> viewScheduledAppointments() {
        return new ArrayList<>(appointments); // Return a copy to avoid modification from outside
    }

    @Override
    public Appointment getAppointment(String appointmentId) {
        return appointments.stream()
                .filter(appointment -> appointment.getAppointmentId().equals(appointmentId))
                .findFirst()
                .orElse(null);
    }
    
    @Override
    public Appointment getAppointmentById(String appointmentId) {
        return getAppointment(appointmentId); // Reuse existing method
    }


    @Override
    public void recordAppointmentOutcome(String appointmentId, String serviceProvided, List<Medication> prescribedMedications, String consultationNotes) {
        Appointment appointment = getAppointment(appointmentId);
        if (appointment != null && appointment.getStatus() == AppointmentStatus.PENDING) {
            appointment.setMedicationStatus(MedicationStatus.PENDING);
            appointment.setStatus(AppointmentStatus.COMPLETED);
            appointment.setServiceProvided(serviceProvided);
            appointment.setConsultationNotes(consultationNotes);
            for (Medication medication : prescribedMedications) {
                appointment.addMedication(medication);
            }
            saveAppointmentsToCSV();
        }
    }
    
    public void updateMedicationStatus(String appointmentId) {
        Appointment appointment = getAppointment(appointmentId);
        if (appointment != null) {
            appointment.setMedicationStatus(MedicationStatus.DISPENSED);
            saveAppointmentsToCSV();
        }
    }

    @Override
    public List<LocalDateTime> getAvailableSlots(String doctorId, LocalDate date) {
        List<LocalDateTime> availableSlots = new ArrayList<>();
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(17, 0);

        for (LocalTime time = startTime; time.isBefore(endTime); time = time.plusMinutes(30)) {
            LocalDateTime slot = LocalDateTime.of(date, time);
            boolean isOccupied = appointments.stream()
                    .anyMatch(appointment -> appointment.getDoctorId().equals(doctorId) && appointment.getAppointmentDateTime().equals(slot));
            if (!isOccupied) {
                availableSlots.add(slot);
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
    private void loadAppointmentsFromCSV() {
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
    private void saveAppointmentsToCSV() {
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
}
