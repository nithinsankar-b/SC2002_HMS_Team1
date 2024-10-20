package services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import models.Patient;

public class PatientService {
    private final Map<String, Patient> patients;

    public PatientService() {
        this.patients = new HashMap<>();
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
        // Path to the CSV file containing patient information (update with the actual file path)
        String csvFilePath = "../data/Patient_List.csv";

        // Formatter for parsing the date format used in the CSV (e.g., "yyyy-MM-dd")
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Try-with-resources block so BufferedReader is closed automatically after usage
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line;

            // Skip the header row of the CSV file (assumes the first line is a header)
            br.readLine();

            // Read each line of the CSV until the end of the file
            while ((line = br.readLine()) != null) {
                // Split the line into values based on commas (CSV delimiter)
                String[] values = line.split(",");

                // Extract and trim each value from the array based on the column order in the CSV
                String patientId = values[0].trim();                            // Patient ID
                String name = values[1].trim();                                 // Name
                LocalDate dob = LocalDate.parse(values[2].trim(), formatter);   // Date of Birth (parsed using the formatter)
                String gender = values[3].trim();                               // Gender
                String bloodType = values[4].trim();                            // Blood Type
                String contactInformation = values[5].trim();                   // Contact Information (Email address)

                // Create a new Patient object with the parsed values
                Patient patient = new Patient(patientId, "defaultPassword", name, dob, gender, bloodType, contactInformation);

                // Add the created patient to the map with the patient ID as the key
                patients.put(patient.getHospitalID(), patient);
            }
        } catch (IOException e) {
            // Print error message if there is an issue reading the CSV file
            System.out.println("Error reading the CSV file: " + e.getMessage());
        }
    }
}
