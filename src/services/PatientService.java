// File: services/PatientService.java
package services;

import enums.AppointmentStatus;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import models.Appointment;
import models.Patient;

public class PatientService {
    private final Map<String, Patient> patients;
    private final AppointmentService appointmentService;

    public PatientService() {
        this.patients = new HashMap<>();
        this.appointmentService = new AppointmentService(); // Initialize AppointmentService
        loadPatients(); // Load patients from the CSV file
    }

    // Method to retrieve a patient by their hospital ID
    public Patient getPatientById(String hospitalID) {
        return patients.get(hospitalID);
    }

    // Method to update patient contact information
    public boolean updatePatientContact(String hospitalID, String newContactInformation) {
        Patient patient = patients.get(hospitalID);
        if (patient != null) {
            patient.setContactInformation(newContactInformation);
            return true;
        }
        return false;
    }

    // Method to list all patients
    public void listAllPatients() {
        patients.values().forEach(patient -> {
            System.out.println("Patient ID: " + patient.getHospitalID());
            System.out.println("Name: " + patient.getName());
            System.out.println("Date of Birth: " + patient.getDateOfBirth());
            System.out.println("Gender: " + patient.getGender());
            System.out.println("Blood Type: " + patient.getBloodType());
            System.out.println("Contact Information: " + patient.getContactInformation());
            System.out.println("Registered: " + patient.getIsRegistered());
            System.out.println("------------------------");
        });
    }

    // Method to load patients from the CSV file
    private void loadPatients() {
        String csvFilePath = "../data/Patient_List.csv";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;
            br.readLine(); // Skip the header row

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                String patientId = values[0].trim();
                String name = values[1].trim();
                LocalDate dob = LocalDate.parse(values[2].trim(), formatter);
                String gender = values[3].trim();
                String bloodType = values[4].trim();
                String contactInformation = values[5].trim();

                // Check again. Do we need password?
                Patient patient = new Patient(patientId, "defaultPassword", name, dob, gender, bloodType, contactInformation);
                patients.put(patient.getHospitalID(), patient);
            }
        } catch (IOException e) {
            System.out.println("Error reading the CSV file: " + e.getMessage());
        }
    }

    // Method to get all appointments for a given patient
    public Appointment[] getPatientAppointments(String patientID) {
        List<Appointment> allAppointments = appointmentService.viewScheduledAppointments();
        List<Appointment> patientAppointments = new ArrayList<>();

        for (Appointment appointment : allAppointments) {
            if (appointment.getPatientId().equals(patientID)) {
                patientAppointments.add(appointment);
            }
        }
         // Convert the list to an array and return
        return patientAppointments.toArray(Appointment[]::new);
    }

    // Method to create an appointment for a patient
    public boolean createAppointment(String patientID, String doctorID, LocalDateTime appointmentDate) {
        String appointmentID = "APPT" + System.currentTimeMillis(); // Generate a unique ID
        Appointment newAppointment = new Appointment(appointmentID, patientID, doctorID, appointmentDate);
        return appointmentService.scheduleAppointment(newAppointment);
    }

    // Method to cancel an existing appointment for a patient
    public boolean cancelAppointment(String patientID, String appointmentID) {
        Appointment appointment = appointmentService.getAppointment(appointmentID);

        if (appointment != null && appointment.getPatientId().equals(patientID)) {
            return appointmentService.cancelAppointment(appointmentID);
        }

        return false;
    }

    // Method to reschedule an existing appointment for a patient
    public boolean rescheduleAppointment(String patientID, String appointmentID, LocalDateTime newDate) {
        Appointment appointment = appointmentService.getAppointment(appointmentID);

        if (appointment != null && appointment.getPatientId().equals(patientID)) {
            Appointment updatedAppointment = new Appointment(appointmentID, patientID, appointment.getDoctorId(), newDate);
            updatedAppointment.setStatus(AppointmentStatus.RESCHEDULED);
            return appointmentService.rescheduleAppointment(appointmentID, updatedAppointment);
        }

        return false;
    }

    public boolean deletePatient(String hospitalID) {
        // Remove the patient from the map by ID if present
        if (patients.containsKey(hospitalID)) {
            patients.remove(hospitalID);
            return true;
        }
        return false; // Return false if the patient was not found
    }
    
}