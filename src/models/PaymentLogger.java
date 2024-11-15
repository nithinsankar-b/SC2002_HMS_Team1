package models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PaymentLogger {
    private static final String PAYMENT_LOG_FILE = "payments.csv";

    // Log a payment
    public static void logPayment(String appointmentId, double amountPaid, String serviceProvided) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PAYMENT_LOG_FILE, true))) {
            String logEntry = String.format("%s,%.2f,%s,%d", appointmentId, amountPaid, serviceProvided, System.currentTimeMillis());
            writer.write(logEntry);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error logging payment: " + e.getMessage());
        }
    }

    // Read all logged payments
    public static List<String> getLoggedPayments() {
        List<String> payments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PAYMENT_LOG_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                payments.add(line);
            }
        } catch (FileNotFoundException e) {
            //System.out.println("No payment log file found. It will be created when the first payment is logged.");
        } catch (IOException e) {
            System.out.println("Error reading payment log: " + e.getMessage());
        }
        return payments;
    }
}

