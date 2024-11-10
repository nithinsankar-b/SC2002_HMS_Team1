package services;

import models.Billing;
import models.Billing.BillingStatus;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class BillingService {
    private static final String CSV_FILE = "billing.csv";

    // Load all billing records from the CSV file
    public List<Billing> loadBillingData() {
        List<Billing> billingRecords = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(CSV_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                billingRecords.add(Billing.fromCsvString(line));
            }
        } catch (IOException e) {
            System.out.println("Error reading billing data: " + e.getMessage());
        }
        return billingRecords;
    }

    // Save billing records to the CSV file
    public void saveBillingData(List<Billing> billingRecords) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CSV_FILE))) {
            for (Billing billing : billingRecords) {
                writer.write(billing.toCsvString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving billing data: " + e.getMessage());
        }
    }

    // Add a new billing record
    public void addBillingRecord(Billing billing) {
        List<Billing> billingRecords = loadBillingData();
        billingRecords.add(billing);
        saveBillingData(billingRecords);
    }

    // Get billing records by status
    public List<Billing> getBillingByStatus(String patientId, BillingStatus status) {
        List<Billing> filteredRecords = new ArrayList<>();
        for (Billing billing : loadBillingData()) {
            if (billing.getPatientId().equals(patientId) && billing.getStatus() == status) {
                filteredRecords.add(billing);
            }
        }
        return filteredRecords;
    }

    // Update a bill to 'Paid' status
    public boolean markBillAsPaid(String invoiceId) {
        List<Billing> billingRecords = loadBillingData();
        boolean updated = false;
        for (Billing billing : billingRecords) {
            if (billing.getInvoiceId().equals(invoiceId) && billing.getStatus() == BillingStatus.UNPAID) {
                billing.setStatus(BillingStatus.PAID);
                updated = true;
                break;
            }
        }
        if (updated) saveBillingData(billingRecords);
        return updated;
    }
}