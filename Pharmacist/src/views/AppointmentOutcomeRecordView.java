package views;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AppointmentOutcomeRecordView {
    private final String csvFilePath = "C:\\Users\\HP\\OneDrive\\Documents\\SC2002\\appointment.csv"; // Adjust path as needed

    public void loadAndPrintAppointments() {
        System.out.println("Loading appointments from: " + csvFilePath);
        
        try (BufferedReader br = new BufferedReader(new FileReader(csvFilePath))) {
            String line = br.readLine(); // Read the header line
            if (line != null) {
                System.out.println("Headers: " + line); // Print headers for reference
            }
            
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                
                if (fields.length == 10) { // Ensure we have all 10 fields
                    System.out.println("\nAppointment Details:");
                    System.out.println("Appointment ID      : " + fields[0].replace("\"", "").trim());
                    System.out.println("Patient ID          : " + fields[1].trim());
                    System.out.println("Doctor ID           : " + fields[2].trim());
                    System.out.println("Appointment DateTime: " + fields[3].trim());
                    System.out.println("Status              : " + fields[4].trim());
                    System.out.println("Consultation Notes  : " + fields[5].trim());
                    System.out.println("Service Provided    : " + fields[6].trim());
                    
                    System.out.println("Medications         : " + fields[7].trim());
                    System.out.println("Quantity            : " + fields[8].trim()); // New quantity field
                    System.out.println("Medication Status   : " + fields[9].trim());
                    
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
