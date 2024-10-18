package boundary;

import interfaces.IPharmacistView;
import models.AppointmentOutcomeRecord;
import models.Patient;
//import models.Prescription;
import enums.PrescriptionStatus;

import java.util.List;
import java.util.Scanner;

public class ViewAppointmentOutcome {

    private final IPharmacistView pharmacistView;

    public ViewAppointmentOutcome(IPharmacistView pharmacistView) {
        this.pharmacistView = pharmacistView;
    }

    /**
     * Displays the patient details, medicines, and appointment outcome.
     */
    public void displayAppointmentOutcome() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n=== View Appointment Outcome ===");
        System.out.print("Enter the Patient ID to view details: ");
        String patientId = scanner.nextLine().trim();

        // Fetch patient and appointment outcome details
        Patient patient = pharmacistView.getPatientDetails(patientId);
        if (patient == null) {
            System.out.println("No patient found with the provided ID.");
            scanner.close();
            return;
        }

        List<AppointmentOutcomeRecord> records = pharmacistView.getAppointmentRecords(patientId);
        System.out.println("\nPatient Name: " + patient.getName());
        System.out.println("Medicines Prescribed:");

        for (AppointmentOutcomeRecord record : records) {
            System.out.println("Medicine: " + record.getMedicineName() + 
                               ", Status: " + record.getStatus());
        }

        System.out.println("\nDo you want to change the status of a prescription? (yes/no)");
        String choice = scanner.nextLine().trim();

        if (choice.equalsIgnoreCase("yes")) {
            System.out.print("Enter the medicine name: ");
            String medicineName = scanner.nextLine().trim();

            boolean updated = pharmacistView.updatePrescriptionStatus(patientId, medicineName, PrescriptionStatus.DISPENSED);
            if (updated) {
                System.out.println("The prescription status for " + medicineName + " has been updated to DISPENSED.");
            } else {
                System.out.println("Failed to update the status. Medicine might not be found.");
            }
        }
        scanner.close();
    }
}
