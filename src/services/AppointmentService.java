package services;

import models.Appointment;
import enums.AppointmentStatus;
import models.Medication;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentService{
    private static final String APPOINTMENT_FILE = "C:\\Users\\nithi\\Downloads\\SC2002_HMS_Team1\\src\\resources\\appointment.csv"; // Update path if needed
    private List<Appointment> appointments;

    public AppointmentService() {
        this.appointments = new ArrayList<>();
        createCSVIfNotExists();
        loadAppointmentsFromCSV();
    }

    public boolean scheduleAppointment(Appointment appointment) {
        boolean success = appointments.add(appointment);
        if (success) {
            saveAppointmentsToCSV();
        }
        return success;
    }

    public boolean cancelAppointment(String appointmentId) {
        boolean success = appointments.removeIf(appointment -> appointment.getAppointmentId().equals(appointmentId));
        if (success) {
            saveAppointmentsToCSV();
        }
        return success;
    }

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

    public List<Appointment> viewScheduledAppointments() {
        return appointments;
    }

    public Appointment getAppointment(String appointmentId) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equals(appointmentId)) {
                return appointment;
            }
        }
        return null;
    }

    public void recordAppointmentOutcome(String appointmentId, String serviceProvided, String consultationNotes, List<Medication> medications) {
        Appointment appointment = getAppointment(appointmentId);
        if (appointment != null && appointment.getStatus() == AppointmentStatus.PENDING) {
            appointment.setStatus(AppointmentStatus.COMPLETED);
            appointment.setServiceProvided(serviceProvided);
            appointment.setConsultationNotes(consultationNotes);
            for (Medication medication : medications) {
                appointment.addMedication(medication);
            }
            saveAppointmentsToCSV();
        }
    }

    // Create CSV file if it does not exist
    private void createCSVIfNotExists() {
        File file = new File(APPOINTMENT_FILE);
        if (!file.exists()) {
            try {
                file.createNewFile();
                // Write header to the CSV file
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                    writer.write("appointmentId,patientId,doctorId,appointmentDateTime,status,consultationNotes,serviceProvided,medications");
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
            writer.write("appointmentId,patientId,doctorId,appointmentDateTime,status,consultationNotes,serviceProvided,medications");
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
