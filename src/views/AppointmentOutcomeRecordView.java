package views;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AppointmentOutcomeRecordView {
    private final String csvFilePath = "data/appointment.csv"; // Adjust path as needed

    public void loadAndPrintAppointments() {
        //System.out.println("Loading appointments from: " + csvFilePath);

        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line = br.readLine(); // Read the header line
            if (line != null) {
                //System.out.println("Headers: " + line); // Print headers for reference
            }

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");

                if (fields.length == 10) { // Ensure we have all 10 fields
                    //System.out.println("\nAppointment Details:");
                    if(!fields[4].trim().equals("COMPLETED"))
                    {
                        continue;
                    }
                    System.out.println("Appointment ID      : " + fields[0].replace("\"", "").trim());
                    System.out.println("Patient ID          : " + fields[1].trim());
                    System.out.println("Doctor ID           : " + fields[2].trim());
                    System.out.println("Appointment Date and Time: " + fields[3].trim());
                    System.out.println("Status              : " + fields[4].trim());
                    System.out.println("Consultation Notes  : " + fields[5].trim());
                    System.out.println("Service Provided    : " + fields[6].trim());



                    // Medications and quantities
                    String medications = fields[7].trim();
                    String quantities = fields[8].trim();

                    if (!medications.isEmpty() && !quantities.isEmpty()) {
                        String[] medicationList = medications.split(";"); // Split by semicolon for medications
                        String[] quantityList = quantities.split(";"); // Split by pipe for quantities

                        System.out.println("Medications:");
                        for (int i = 0; i < medicationList.length; i++) {
                            String medication = medicationList[i].trim();
                            String quantity = (i < quantityList.length) ? quantityList[i].trim() : "N/A";

                            System.out.println("- Name: " + medication);
                            System.out.println("  Quantity: " + quantity);
                        }
                        System.out.println("Medication Status   : "+fields[9].trim());
                    } else {
                        System.out.println("Medications: No medications listed");
                    }
                    System.out.println("----------------------------------------");
                    // Medication Status (Optional - If you need to print it)
                    // System.out.println("Medication Status   : " + fields[9].trim());
                } else {
                    System.out.println("Warning: Skipping line due to unexpected format -> " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        AppointmentOutcomeRecordView view = new AppointmentOutcomeRecordView();
        view.loadAndPrintAppointments();
    }
}
