package services;

import enums.PrescriptionStatus;
import models.AppointmentOutcomeRecord;
import interfaces.IPharmacistView;
import models.Patient;
import models.Pharmacist;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentOutcomeService implements IPharmacistView {

    private static final String CSV_FILE = "AppointmentOutcomeRecord.csv";

    /**
     * Get the patient details based on patientId
     */
    @Override
    public Patient getPatientDetails(String patientId) {
        // You can implement logic to fetch patient details from another source (like database)
        // For this example, let's assume we fetch it from a hardcoded list or database.
        return new Patient(patientId, "John Doe"); // Dummy patient for the sake of the example
    }

    /**
     * Get the appointment records from the CSV file for the given patient ID.
     */
    @Override
    public List<AppointmentOutcomeRecord> getAppointmentRecords(String patientId) {
        List<AppointmentOutcomeRecord> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equals(patientId)) {
                    records.add(new AppointmentOutcomeRecord(values[0], values[1], PrescriptionStatus.valueOf(values[2])));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    /**
     * Update the prescription status from PENDING to DISPENSED in the CSV file.
     */
    @Override
    public boolean updatePrescriptionStatus(String patientId, String medicineName, PrescriptionStatus status) {
        List<AppointmentOutcomeRecord> records = new ArrayList<>();
        boolean updated = false;

        // Read the CSV and update the status in memory
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                AppointmentOutcomeRecord record = new AppointmentOutcomeRecord(values[0], values[1], PrescriptionStatus.valueOf(values[2]));
                if (record.getPatientId().equals(patientId) && record.getMedicineName().equalsIgnoreCase(medicineName)) {
                    record.setStatus(status);
                    updated = true;
                }
                records.add(record);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Write back the updated records to the CSV
        if (updated) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(CSV_FILE))) {
                for (AppointmentOutcomeRecord record : records) {
                    pw.println(record.getPatientId() + "," + record.getMedicineName() + "," + record.getStatus());
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        return updated;
    }

    /**
     * View all appointment outcome records.
     */
    @Override
    public List<AppointmentOutcomeRecord> viewAppointmentOutcomeRecords() {
        List<AppointmentOutcomeRecord> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                records.add(new AppointmentOutcomeRecord(values[0], values[1], PrescriptionStatus.valueOf(values[2])));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;
    }

    /**
     * Update prescription status using prescription ID.
     */
    @Override
    public void updatePrescriptionStatus(String prescriptionId) {
        // Implement logic to update status based on prescriptionId if required.
        // You can map prescriptionId to patientId and medicineName if needed.
        System.out.println("Prescription status updated for ID: " + prescriptionId);
    }

    /**
     * View the medication inventory.
     */
    @Override
    public void viewMedicationInventory() {
        // You can implement logic to view inventory here.
        System.out.println("Displaying medication inventory...");
    }

    /**
     * Submit a replenishment request based on medicine name.
     */
    @Override
    public void submitReplenishmentRequest(String medicationName) {
        // Implement logic to request replenishment of a medication
        System.out.println("Replenishment request submitted for: " + medicationName);
    }

    /**
     * Submit a replenishment request for a pharmacist with a specific medicine and quantity.
     */
    @Override
    public void submitReplenishmentRequest(Pharmacist pharmacist, String medicineName, int quantity) {
        // Implement logic to submit replenishment request by the pharmacist
        System.out.println("Pharmacist " + pharmacist.getName() + " requested " + quantity + " units of " + medicineName);
    }
}
