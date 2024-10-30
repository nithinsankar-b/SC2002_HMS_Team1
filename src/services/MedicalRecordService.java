
package services;

import models.Appointment;
import models.AppointmentRequest;
import models.MedicalRecord;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MedicalRecordService {
    private final Map<String, MedicalRecord> medicalRecords;
    private final String medicalRecordFile = "data/medical_record.csv"; // Specify your CSV file path

    public MedicalRecordService() {
        this.medicalRecords = new HashMap<>();
        loadRecordsFromCSV(); // Load records from CSV on initialization
    }

    // Load all medical records from CSV
    private void loadRecordsFromCSV() {
    	
        try (BufferedReader br = new BufferedReader(new FileReader(medicalRecordFile))) {
            String line;
            while ((line = br.readLine()) != null) {
            	
                String[] fields = line.split(",");
                if (fields.length == 11) { // Ensure we have enough fields
                    MedicalRecord record = new MedicalRecord(
                            fields[0], // patientID
                            fields[1], // name
                            fields[2], // dob
                            fields[3], // gender
                            fields[4], // phoneNumber
                            fields[5], // emailAddress
                            fields[6], // bloodType
                            fields[7], // pastDiagnoses
                            fields[8], // pastPrescriptions
                            fields[9], // newDiagnosis
                            fields[10] // newPrescription
                    );
                    medicalRecords.put(record.getPatientID(), record);
                    
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Save records back to CSV
    public void saveRecordsToCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(medicalRecordFile))) {
            for (MedicalRecord record : medicalRecords.values()) {
                bw.write(String.join(",",
                        record.getPatientID(),
                        record.getName(),
                        record.getDob(),
                        record.getGender(),
                        record.getPhoneNumber(),
                        record.getEmailAddress(),
                        record.getBloodType(),
                        record.getPastDiagnoses() != null ? record.getPastDiagnoses().replace("\n", "; ") : "",
                        record.getPastPrescriptions() != null ? record.getPastPrescriptions().replace("\n", "; ") : "",
                        record.getNewDiagnosis() != null ? record.getNewDiagnosis() : "",
                        record.getNewPrescription() != null ? record.getNewPrescription() : ""
                ));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Add a new diagnosis and prescription for a patient
    public void addNewDiagnosisPrescription(String patientID, String newDiagnosis, String newPrescription) {
        MedicalRecord record = medicalRecords.get(patientID);
        if (record != null) {
            record.addNewDiagnosis(newDiagnosis);
            record.addNewPrescription(newPrescription);
            saveRecordsToCSV(); // Save after updating
        }
    }

    // Get a medical record for a patient by ID
    public MedicalRecord getMedicalRecord(String patientID) {
    	System.out.println("HEYY");
    	//System.out.println(medicalRecords.get(patientID));
        return medicalRecords.get(patientID); // Returns the MedicalRecord or null if not found
    }

    // Print a medical record for a patient
    public void printMedicalRecord(String patientID) {
        MedicalRecord record = medicalRecords.get(patientID);
        if (record != null) {
            System.out.println("Patient ID: " + record.getPatientID());
            System.out.println("Name: " + record.getName());


System.out.println("Date of Birth: " + record.getDob());
            System.out.println("Gender: " + record.getGender());
            System.out.println("Phone Number: " + record.getPhoneNumber());
            System.out.println("Email Address: " + record.getEmailAddress());
            System.out.println("Blood Type: " + record.getBloodType());
            System.out.println("Past Diagnoses:\n" + record.getPastDiagnoses());
            System.out.println("Past Prescriptions:\n" + record.getPastPrescriptions());
            System.out.println("New Diagnosis: " + record.getNewDiagnosis());
            System.out.println("New Prescription: " + record.getNewPrescription());
        } else {
            System.out.println("No medical record found for Patient ID: " + patientID);
        }
    }

    // Additional methods related to appointment requests can be integrated below
    // e.g., acceptRequest, getPendingRequests, processPendingRequests, etc.

    // Assuming other methods similar to AppointmentRequestService can be added here.
}