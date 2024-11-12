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

    public double calculateTotalBill(String appointmentId) {
        Billing billing = billingRecords.get(appointmentId);
        if (billing == null) {
            System.err.println("No billing record found for appointment ID: " + appointmentId);
            return 0.0;
        }

        // Call getAppointmentById to retrieve the Appointment
        Appointment appointment = Appointment.getAppointmentById(appointmentId); // Assuming this method exists
        if (appointment == null) {
            System.err.println("No appointment found for appointment ID: " + appointmentId);
            return 0.0;
        }

        double totalBill = 0.0;
        String doctorId = appointment.getDoctorId();

        for (int i = 0; i < appointment.getMedications().size(); i++) {
            Medication medication = appointment.getMedications().get(i);
            int quantity = appointment.getQuantities().get(i);

            MedicineConsultation medCon = medicineConsultations.get(medication.getName());

            if (medCon != null && medCon.getDoctorId().equals(doctorId)) {
                double medicineCost = (medCon.getMedicinePriceForTen() / 10) * quantity;
                totalBill += medicineCost;
            }
        }

        // Add consultation fee
        for (MedicineConsultation medCon : medicineConsultations.values()) {
            if (medCon.getDoctorId().equals(doctorId)) {
                totalBill += medCon.getConsultationFee();
                break;
            }
        }

        billing.setTotalAmount(totalBill);
        saveBillingRecords();
        return totalBill;
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
