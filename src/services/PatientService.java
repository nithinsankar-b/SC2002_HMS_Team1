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

import enums.UserRole;
import models.Patient;
import models.User;
import models.MedicalRecord;
import services.MedicalRecordService;
import services.UserService;
import services.AppointmentService;

/**
 * Service class to manage operations related to patients.
 * <p>
 * This class handles functionalities like adding new patients, retrieving patient details,
 * and maintaining the consistency between in-memory data and persistent storage (CSV file).
 * </p>
 */
public class PatientService {
    private final Map<String, Patient> patients;
    private final UserService userService;

    /**
     * Constructor for the `PatientService` class.
     *
     * @param userService The `UserService` instance to manage user-related operations.
     */
    public PatientService(UserService userService) {
        this.userService = userService;
        this.patients = new HashMap<>();
        new AppointmentService(); // Initialize AppointmentService
        loadPatientsFromCSV();    // Load patients from the CSV file
    }

    /**
     * Adds a new patient to the system.
     * <p>
     * If the patient does not already exist, the patient is added to the in-memory data structure
     * and saved to the CSV file. Otherwise, a message is displayed indicating that the patient
     * already exists.
     * </p>
     *
     * @param patient The `Patient` object to be added.
     */
    public void addPatient(Patient patient) {
        if (!patients.containsKey(patient.getHospitalID())) { // Check if the patient already exists
            patients.put(patient.getHospitalID(), patient);   // Add patient to the map
            savePatientsToCSV();                             // Persist updated data to CSV
            System.out.println("Patient added successfully.");
        } else {
            System.out.println("Patient already exists.");
        }
    }

    /**
     * Retrieves a patient by their unique hospital ID.
     *
     * @param hospitalID The unique ID of the patient to be retrieved.
     * @return The `Patient` object if found; `null` otherwise.
     */
    public Patient getPatientById(String hospitalID) {
        return patients.get(hospitalID); // Retrieve the patient from the map
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

/**
 * Updates a patient's contact information (phone or email) based on the specified contact type.
 * If a medical record exists for the patient, the corresponding record is also updated.
 *
 * @param hospitalID           The unique ID of the patient whose contact information is being updated.
 * @param newContactInformation The new contact information (phone number or email address).
 * @param contactType           The type of contact information to update ("phone" or "email").
 * @return true if the patient's contact information is successfully updated; false if the patient is not found.
 */
public boolean updatePatientContact(String hospitalID, String newContactInformation, String contactType) {
    Patient patient = patients.get(hospitalID); // Retrieve the patient by ID
    if (patient != null) {
        MedicalRecordService medicalRecordService = new MedicalRecordService();

        // Update the corresponding medical record as well
        MedicalRecord medicalRecord = medicalRecordService.getMedicalRecord(hospitalID);
        if (medicalRecord != null) {
            // Update phone number or email address based on the contact type
            if ("phone".equalsIgnoreCase(contactType)) {
                medicalRecord.setPhoneNumber(newContactInformation);
            } else if ("email".equalsIgnoreCase(contactType)) {
                medicalRecord.setEmailAddress(newContactInformation);
            }
            medicalRecordService.saveRecordsToCSV(); // Save medical records after the update
        }

        savePatientsToCSV(); // Save updated patient information to CSV
        return true;
    }
    return false; // Return false if the patient is not found
}

/**
 * Lists all patients and their details.
 * This method is primarily for administrators to view a summary of all patients.
 * <p>
 * Prints the following details for each patient:
 * - Patient ID
 * - Name
 * - Date of Birth
 * - Gender
 * - Blood Type
 * - Contact Information
 * </p>
 */
public void listAllPatients() {
    // Loop through all patients in the collection and print their details
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

/**
 * Loads patient information from a CSV file and populates the internal `patients` collection.
 * The CSV file must have the following columns in this order:
 * Patient ID, Name, Date of Birth, Gender, Blood Type, and Contact Information.
 * 
 * <p>
 * This method skips the header line and reads each subsequent line, creating
 * Patient objects and storing them in the `patients` collection.
 * If a user with the same ID already exists in the system, the existing user's
 * password is preserved; otherwise, a default password is assigned.
 * </p>
 */
    private void loadPatientsFromCSV() {
        String filePath = "data/Patient.csv"; // Path to the CSV file containing patient data
        String line; // Variable to hold each line of the file
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Formatter for parsing date strings
    
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            // Skip the header line in the CSV file
            br.readLine();
            while ((line = br.readLine()) != null) {
                // Split the line into an array of values
                String[] patientData = line.split(",");
    
                if (patientData.length == 6) { // Ensure the line contains all required fields
                    String patientId = patientData[0].trim();  // Patient ID
                    String name = patientData[1].trim();       // Name
                    String dobString = patientData[2].trim();  // Date of Birth (as a string)
                    String gender = patientData[3].trim();     // Gender
                    String bloodType = patientData[4].trim();  // Blood Type
                    String contactInformation = patientData[5].trim(); // Contact Information (e.g., email)
    
                    // Parse the Date of Birth string into a LocalDate object
                    LocalDate dob = LocalDate.parse(dobString, formatter);
    
                    // Try to find an existing user by their ID
                    User existingUser = userService.getUserById(patientId);
    
                    // Use the existing user's password if found; otherwise, assign a default password
                    String password = (existingUser != null) ? existingUser.getPassword() : "defaultPassword";
    
                    // Create a new Patient object and add it to the collection
                    patients.put(patientId, 
                        new Patient(
                            existingUser != null 
                                ? existingUser 
                                : new User(patientId, password, UserRole.PATIENT), 
                            name, dob, gender, bloodType, contactInformation
                        )
                    );
                }
            }
        } catch (IOException e) {
            // Handle any errors that occur while reading the file
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
    }

/**
 * Saves the current list of patients back to the CSV file.
 * The CSV file includes patient details such as Patient ID, Name, Date of Birth, Gender, Blood Type,
 * and Contact Information. This method overwrites the existing file with the updated patient list.
 */
private void savePatientsToCSV() {
    String csvFilePath = "data/Patient.csv"; // File path for the patient CSV file
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(csvFilePath))) {
        // Write the header row to the CSV file
        bw.write("Patient ID,Name,Date of Birth,Gender,Blood Type,Contact Information");
        bw.newLine();

        // Iterate through all patients and write their details to the CSV file
        for (Patient patient : patients.values()) {
            String line = String.join(",",
                    patient.getHospitalID(),                // Patient ID
                    patient.getName(),                      // Name
                    patient.getDateOfBirth().toString(),    // Date of Birth
                    patient.getGender(),                    // Gender
                    patient.getBloodType(),                 // Blood Type
                    patient.getContactInformation());       // Contact Information (e.g., email)
            bw.write(line);
            bw.newLine();
        }
    } catch (IOException e) {
        // Handle any IO exceptions that occur during file writing
        System.out.println("Error saving the CSV file: " + e.getMessage());
    }
}
}
