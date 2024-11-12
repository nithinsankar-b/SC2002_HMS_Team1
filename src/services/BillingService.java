// BillingService.java
package services;

import models.Appointment;
import models.Billing;
import models.Medication;
import models.MedicineConsultation;

import java.io.*;
import java.util.*;

public class BillingService {
    private static final String BILLING_FILE = "data/Billing.csv";
    private static final String MEDICINE_FILE = "data/Medicine_and_Consultation_Prices.csv";
    private Map<String, Billing> billingRecords = new HashMap<>();
    private Map<String, MedicineConsultation> medicineConsultations = new HashMap<>();

    public BillingService() {
        loadBillingRecords();
        loadMedicineConsultations();
    }

    private void loadBillingRecords() {
        File billingFile = new File(BILLING_FILE);
        if (!billingFile.exists()) {
            System.err.println("Billing file not found at path: " + billingFile.getAbsolutePath());
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(billingFile))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                
                String[] fields = line.split(",");

                // Ensure valid line format
                if (fields.length < 6) {
                    System.err.println("Invalid line format: " + line);
                    continue;
                }

                Billing billing = new Billing();
                billing.setInvoiceId(fields[0]);
                billing.setPatientId(fields[1]);
                billing.setDoctorId(fields[2]);

                // Strip double quotes if present around the appointmentId
                String appointmentId = fields[3].replace("\"", "").trim();
                billing.setAppointmentId(appointmentId);

                billing.setTotalAmount(Double.parseDouble(fields[4]));
                billing.setStatus(fields[5]);
                billingRecords.put(billing.getAppointmentId(), billing);
            }
            //System.out.println("Billing records loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error loading billing records: " + e.getMessage());
        }
    }


    private void loadMedicineConsultations() {
        try (BufferedReader reader = new BufferedReader(new FileReader(MEDICINE_FILE))) {
            String line;
            reader.readLine(); // Skip header
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                MedicineConsultation medicineConsultation = new MedicineConsultation();
                medicineConsultation.setMedicine(fields[0]);
                medicineConsultation.setMedicinePriceForTen(Double.parseDouble(fields[1]));
                medicineConsultation.setDoctorId(fields[2]);
                medicineConsultation.setConsultationFee(Double.parseDouble(fields[3]));
                medicineConsultations.put(fields[0], medicineConsultation);
            }
        } catch (IOException e) {
            System.err.println("Error loading medicine consultations: " + e.getMessage());
        }
    }

    public String calculateAndDisplayBill(String appointmentId) {
        Billing billing = billingRecords.get(appointmentId);
        if (billing == null) {
            System.err.println("No billing record found for appointment ID: " + appointmentId);
            return "No billing record found.";
        }

        // Retrieve the appointment by ID
        Appointment appointment = Appointment.getAppointmentById(appointmentId); // Assuming this method exists
        if (appointment == null) {
            System.err.println("No appointment found for appointment ID: " + appointmentId);
            return "No appointment found.";
        }

        // Check if the medicationStatus of the appointment is DISPENSED
        if (appointment.getMedicationStatus() != enums.MedicationStatus.DISPENSED) {
            System.err.println("Medications for this appointment have not been dispensed.");
            return "Medications for this appointment have not been dispensed.";
        }

        double totalBill = 0.0;
        String doctorId = appointment.getDoctorId();
        StringBuilder billDetails = new StringBuilder("Bill Details:\n\n");

        // Medication breakdown
        billDetails.append("Medications:\n");
        for (int i = 0; i < appointment.getMedications().size(); i++) {
            Medication medication = appointment.getMedications().get(i);
            int quantity = appointment.getQuantities().get(i);

            // Find the consultation details for the medication
            MedicineConsultation medCon = medicineConsultations.get(medication.getName());
            if (medCon != null && medCon.getDoctorId().equals(doctorId)) {
                double medicineCost = (medCon.getMedicinePriceForTen() / 10) * quantity;
                totalBill += medicineCost;

                // Add details of the medication to the bill
                billDetails.append(String.format("  - %s: $%.2f (Quantity: %d)\n", medication.getName(), medicineCost, quantity));
            }
        }

        // Consultation fee
        double consultationFee = 0.0;
        for (MedicineConsultation medCon : medicineConsultations.values()) {
            if (medCon.getDoctorId().equals(doctorId)) {
                consultationFee = medCon.getConsultationFee();
                totalBill += consultationFee;
                break;
            }
        }
        billDetails.append(String.format("\nConsultation Fee: $%.2f\n", consultationFee));

        // Final total
        billDetails.append(String.format("\nTotal Bill Amount: $%.2f\n", totalBill));

        // Update the billing record and save
        billing.setTotalAmount(totalBill);
        saveBillingRecords();

        // Return the bill details as a formatted string
        return billDetails.toString();
    }


    public boolean payBill(String appointmentId) {
        Billing billing = billingRecords.get(appointmentId);
        if (billing == null || !"UNPAID".equalsIgnoreCase(billing.getStatus())) {
            return false;
        }
        billing.setStatus("PAID");
        saveBillingRecords();
        return true;
    }

    private void saveBillingRecords() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(BILLING_FILE))) {
            writer.write("InvoiceID,PatientID,DoctorID,AppointmentID,Total_Amount,Status\n");
            for (Billing billing : billingRecords.values()) {
                writer.write(String.format("\"%s\",\"%s\",\"%s\",\"%s\",%.2f,\"%s\"\n",
                        billing.getInvoiceId(), billing.getPatientId(), billing.getDoctorId(),
                        billing.getAppointmentId(), billing.getTotalAmount(), billing.getStatus()));
            }
        } catch (IOException e) {
            System.err.println("Error saving billing records: " + e.getMessage());
        }
    }

}
