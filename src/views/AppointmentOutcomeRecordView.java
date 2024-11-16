package views;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * A view class to load and display appointment outcome records from a CSV file.
 */
public class AppointmentOutcomeRecordView {
    private final String csvFilePath = "data/appointment.csv"; // Adjust path as needed

    /**
     * Loads and prints completed appointments with their details, including medications and quantities.
     * Only displays records with COMPLETED status and non-PENDING medication status.
     */
    public void loadAndPrintAppointments() {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line = br.readLine(); // Read the header line
            if (line != null) {
                // System.out.println("Headers: " + line); // Optional: print headers for reference
            }

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");

                if (fields.length == 10) { // Ensure the line has the expected 10 fields
                    if (!fields[4].trim().equals("COMPLETED") || fields[9].trim().equals("PENDING")) {
                        continue; // Skip non-COMPLETED or PENDING medication records
                    }

                    System.out.println("Appointment ID      : " + fields[0].replace("\"", "").trim());
                    System.out.println("Patient ID          : " + fields[1].trim());
                    System.out.println("Doctor ID           : " + fields[2].trim());
                    System.out.println("Appointment Date and Time: " + fields[3].trim());
                    System.out.println("Status              : " + fields[4].trim());
                    System.out.println("Consultation Notes  : " + fields[5].trim());
                    System.out.println("Service Provided    : " + fields[6].trim());

                    // Parse medications and quantities
                    String medications = fields[7].trim();
                    String quantities = fields[8].trim();

                    if (!medications.isEmpty() && !quantities.isEmpty()) {
                        String[] medicationList = medications.split(";");
                        String[] quantityList = quantities.split(";");

                        System.out.println("Medications:");
                        for (int i = 0; i < medicationList.length; i++) {
                            String medication = medicationList[i].trim();
                            String quantity = (i < quantityList.length) ? quantityList[i].trim() : "N/A";

                            System.out.println("- Name: " + medication);
                            System.out.println("  Quantity: " + quantity);
                        }
                        System.out.println("Medication Status   : " + fields[9].trim());
                    } else {
                        System.out.println("Medications: No medications listed");
                    }

                    System.out.println("----------------------------------------");
                } else {
                    System.out.println("Warning: Skipping line due to unexpected format -> " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    /**
     * Loads and prints pending medication appointments with COMPLETED status.
     *
     * @return true if pending appointments are found and printed, false otherwise.
     */
    public boolean loadAndPrintPendingAppointments() {
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line = br.readLine(); // Read the header line
            boolean found = false;

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");

                if (fields.length == 10) {
                    if (!fields[9].trim().equals("PENDING") || !fields[4].trim().equals("COMPLETED")) {
                        continue; // Skip non-pending or non-completed records
                    }
                    found = true;

                    // Print appointment details
                    System.out.println("Appointment ID      : " + fields[0].replace("\"", "").trim());
                    System.out.println("Patient ID          : " + fields[1].trim());
                    System.out.println("Doctor ID           : " + fields[2].trim());
                    System.out.println("Appointment Date and Time: " + fields[3].trim());
                    System.out.println("Status              : " + fields[4].trim());
                    System.out.println("Consultation Notes  : " + fields[5].trim());
                    System.out.println("Service Provided    : " + fields[6].trim());

                    // Parse medications and quantities
                    String medications = fields[7].trim();
                    String quantities = fields[8].trim();

                    if (!medications.isEmpty() && !quantities.isEmpty()) {
                        String[] medicationList = medications.split(";");
                        String[] quantityList = quantities.split(";");

                        System.out.println("Medications:");
                        for (int i = 0; i < medicationList.length; i++) {
                            String medication = medicationList[i].trim();
                            String quantity = (i < quantityList.length) ? quantityList[i].trim() : "N/A";

                            System.out.println("- Name: " + medication);
                            System.out.println("  Quantity: " + quantity);
                        }
                    } else {
                        System.out.println("Medications: No medications listed");
                    }

                    System.out.println("Medication Status   : " + fields[9].trim());
                    System.out.println("----------------------------------------");
                } else {
                    System.out.println("Warning: Skipping line due to unexpected format -> " + line);
                }
            }
            return found;
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Main method for testing the AppointmentOutcomeRecordView functionality.
     *
     * @param args command-line arguments (not used).
     */
    public static void main(String[] args) {
        AppointmentOutcomeRecordView view = new AppointmentOutcomeRecordView();
        view.loadAndPrintAppointments();
    }
}

