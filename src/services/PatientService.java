package services;

import enums.AppointmentStatus;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import enums.UserRole;
import models.Appointment;
import models.Patient;

public class PatientService {
    private final Map<String, Patient> patients;
    private final AppointmentService appointmentService;

    public PatientService(Patient patient) {
        this.patients = new HashMap<>();
        this.appointmentService = new AppointmentService(); // Initialize AppointmentService
        loadPatientsFromCSV(); // Load patients from the CSV file
    }

    // Method to retrieve a patient by their hospital ID
    public Patient getPatientById(String hospitalID) {
        // Returns a patient object
        return patients.get(hospitalID);
    }

    // Method to update patient contact information
    public boolean updatePatientContact(String hospitalID, String newContactInformation) {
        Patient patient = patients.get(hospitalID);
        if (patient != null) {
            patient.setContactInformation(newContactInformation);
            savePatientsToCSV(); // Save changes after update
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

    private void loadPatientsFromCSV() {
        String filePath = "data\\Patient.csv";
        String line;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Read file form this file path
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // While line is not empty
            while ((line = br.readLine()) != null) {
                // Storing each line in a list with delimiter that is a comma
                String[] patientData = line.split(",");

                if (patientData.length == 6) {
                    // Getting each field from the array
                    String patientId = patientData[0].trim();
                    String name = patientData[1].trim();
                    String dobString = patientData[2].trim();
                    String gender = patientData[3].trim();
                    String bloodType = patientData[4].trim();
                    String contactInformation = patientData[5].trim();

                    // Skip header row if it's detected
                    if (patientId.equalsIgnoreCase("patientID")) {
                        continue;
                    }

                    // Parsing the date into the appropriate format
                    LocalDate dob = LocalDate.parse(dobString, formatter);

                    // Putting a default password. Actual password is linked to hospitalID in User.
                    String password = "defaultPassword";

                    // Create a new Patient object and add it to the collection
                    patients.put(patientId, new Patient(patientId,password, UserRole.PATIENT, name, dob, gender, bloodType, contactInformation));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
    }

    // Method to save patients back to the CSV file
    private void savePatientsToCSV() {
        String csvFilePath = "data/Patient.csv";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFilePath))) {
            // Write the header
            bw.write("Patient ID,Name,Date of Birth,Gender,Blood Type,Contact Information");
            bw.newLine();

            // Write each patient's information
            for (Patient patient : patients.values()) {
                String line = String.join(",",
                        patient.getHospitalID(),
                        patient.getName(),
                        patient.getDateOfBirth().toString(),
                        patient.getGender(),
                        patient.getBloodType(),
                        patient.getContactInformation());
                bw.write(line);
                bw.newLine();
            }
            System.out.println("Patient information saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving the CSV file: " + e.getMessage());
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
            updatedAppointment.setStatus(AppointmentStatus.PENDING);
            return appointmentService.rescheduleAppointment(appointmentID, updatedAppointment);
        }

        return false;
    }

    public boolean deletePatient(String hospitalID) {
        if (patients.containsKey(hospitalID)) {
            patients.remove(hospitalID);
            savePatientsToCSV(); // Save changes after deletion
            return true;
        }
        return false;
    }
    
}