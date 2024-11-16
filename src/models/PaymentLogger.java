package models;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * The `PaymentLogger` class provides functionality to log and retrieve payment details
 * associated with appointments in a hospital management system.
 * Payment logs are stored in a CSV file (`payments.csv`).
 */
public class PaymentLogger {
    private static final String PAYMENT_LOG_FILE = "payments.csv";

    /**
     * Logs a payment to the payment log file.
     *
     * @param appointmentId   The ID of the appointment for which the payment was made.
     * @param amountPaid      The amount paid for the service.
     * @param serviceProvided The description of the service provided.
     */
    public static void logPayment(String appointmentId, double amountPaid, String serviceProvided) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(PAYMENT_LOG_FILE, true))) {
            // Format the log entry with the appointment ID, amount paid, service, and timestamp
            String logEntry = String.format("%s,%.2f,%s,%d", appointmentId, amountPaid, serviceProvided, System.currentTimeMillis());
            writer.write(logEntry);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error logging payment: " + e.getMessage());
        }
    }

    /**
     * Retrieves all logged payments from the payment log file.
     *
     * @return A list of payment log entries, where each entry is a line from the log file.
     */
    public static List<String> getLoggedPayments() {
        List<String> payments = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(PAYMENT_LOG_FILE))) {
            String line;
            // Read each line from the log file and add it to the list
            while ((line = reader.readLine()) != null) {
                payments.add(line);
            }
        } catch (FileNotFoundException e) {
            // FileNotFoundException is ignored since it will be created upon the first payment log
        } catch (IOException e) {
            System.out.println("Error reading payment log: " + e.getMessage());
        }
        return payments;
    }
}


