package services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import enums.UserRole;
import models.Patient;
import models.User;
import models.MedicalRecord;
import services.MedicalRecordService;
import services.UserService;
import services.AppointmentService;

public class PatientService {
    private final Map<String, Patient> patients;
    private final UserService userService;

    public PatientService(UserService userService) {
        this.userService = userService;
        this.patients = new HashMap<>();
        new AppointmentService();// Initialize AppointmentService
        loadPatientsFromCSV(); // Load patients from the CSV file
    }

    // Method to add a new patient
    public void addPatient(Patient patient) {
        if (!patients.containsKey(patient.getHospitalID())) {
            patients.put(patient.getHospitalID(), patient);
            savePatientsToCSV(); // Save the updated patient list to CSV
            System.out.println("Patient added successfully.");
        } else {
            System.out.println("Patient already exists.");
        }
    }

    // Add a method to retrieve all patients as a list
    public Map<String, Patient> getPatients() {
        return patients;
    }

    // Method to retrieve a patient by their hospital ID
    public Patient getPatientById(String hospitalID) {
        return patients.get(hospitalID);
    }

    // Method to update patient contact information
    /* public boolean updatePatientContact(String hospitalID, String newContactInformation) {
        Patient patient = patients.get(hospitalID);
        if (patient != null) {
            patient.setContactInformation(newContactInformation);
            savePatientsToCSV(); // Save changes after update

            return true;
        }
        return false;
    } */

    public boolean updatePatientContact(String hospitalID, String newContactInformation, String contactType) {
        Patient patient = patients.get(hospitalID);
        if (patient != null) {
            MedicalRecordService medicalRecordService = new MedicalRecordService();
            MedicalRecord medicalRecord = medicalRecordService.getMedicalRecord(hospitalID);

            if (medicalRecord != null) {
                if (contactType.equalsIgnoreCase("phone")) {
                    newContactInformation = newContactInformation.replaceAll("(\\d{4})(?=\\d)", "$1 ");
                    medicalRecord.setPhoneNumber(newContactInformation);
                } else if (contactType.equalsIgnoreCase("email")) {
                    if (!newContactInformation.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                        System.out.println("Invalid email format. Please provide a valid email address.");
                        return false;
                    }
                    medicalRecord.setEmailAddress(newContactInformation);
                }
                medicalRecordService.saveRecordsToCSV();
            }

            if (contactType.equalsIgnoreCase("phone")) {
                newContactInformation = newContactInformation.replaceAll("(\\d{4})(?=\\d)", "$1 ");
            } else if (contactType.equalsIgnoreCase("email")) {
                if (!newContactInformation.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                    System.out.println("Invalid email format. Please provide a valid email address.");
                    return false;
                }
            }

            patient.setContactInformation(newContactInformation);
            savePatientsToCSV(); // Ensure the updated email or phone is saved to Patient.csv
            return true;
        }
        return false;
    }


    // Method to list all patients
    // For administrator to use
    public void listAllPatients() {
        patients.values().forEach(patient -> {
            System.out.println("Patient ID: " + patient.getHospitalID());
            System.out.println("Name: " + patient.getName());
            System.out.println("Date of Birth: " + patient.getDateOfBirth());
            System.out.println("Gender: " + patient.getGender());
            System.out.println("Blood Type: " + patient.getBloodType());
            System.out.println("Contact Information: " + patient.getContactInformation());
            System.out.println("------------------------");
        });
    }

    private void loadPatientsFromCSV() {
        String filePath = "data/Patient.csv";
        String line;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip the header line
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] patientData = line.split(",");

                if (patientData.length == 6) {
                    String patientId = patientData[0].trim();
                    String name = patientData[1].trim();
                    String dobString = patientData[2].trim();
                    String gender = patientData[3].trim();
                    String bloodType = patientData[4].trim();
                    String contactInformation = patientData[5].trim();

                    LocalDate dob = LocalDate.parse(dobString, formatter);

                    // Try to find an existing user if applicable
                    User existingUser = userService.getUserById(patientId);
                    String password = (existingUser != null) ? existingUser.getPassword() : "defaultPassword";

                    // Create a new Patient object and add it to the collection
                    // Conditional expression checks if user exists
                    // If YES, use it
                    // Otherwise, creates a new User object
                    patients.put(patientId, new Patient(existingUser != null ? existingUser : new User(patientId, password, UserRole.PATIENT), name, dob, gender, bloodType, contactInformation));
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
        } catch (IOException e) {
            System.out.println("Error saving the CSV file: " + e.getMessage());
        }
    }

    public boolean checkAndPromptPasswordChange(String hospitalID, Scanner scanner) {
        User user = userService.getUserById(hospitalID);
        try {
            String encryptedDefaultPassword = UserService.encryptPassword("password");
            if (user != null && user.getPassword().equals("ENC(" + encryptedDefaultPassword + ")")) {
                System.out.println("Your password is set to the default value. Please change it immediately.");
                while (true) {
                    System.out.print("Enter New Password: ");
                    String newPassword = scanner.nextLine();

                    System.out.print("Confirm New Password: ");
                    String confirmPassword = scanner.nextLine();

                    if (newPassword.equals(confirmPassword)) {
                        if (userService.changePassword(hospitalID, "password", newPassword)) {
                            System.out.println("Password changed successfully!");
                            return true;
                        } else {
                            System.out.println("Failed to change password. Please try again.");
                        }
                    } else {
                        System.out.println("Passwords do not match. Please try again.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return false;
    }

    public boolean removePatient(String patientId) {
        if (!patients.containsKey(patientId)) {
            return false;
        }

        patients.remove(patientId);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("data/Patient.csv"))) {
            for (Patient patient : patients.values()) {
                writer.write(patient.getHospitalID() + "," + patient.getName() + "," + patient.getDateOfBirth() + ","
                        + patient.getGender() + "," + patient.getBloodType() + "," + patient.getContactInformation());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Error updating Patient.csv: " + e.getMessage());
            return false;
        }
    }


}
