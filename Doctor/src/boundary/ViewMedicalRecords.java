package boundary;

import interfaces.IDoctorView;
import models.Doctor;
import models.MedicalRecord;
import services.MedicalRecordService;
import stores.MedicalRecordDataStore;

import java.util.List;
import java.util.Scanner;

public class ViewMedicalRecords implements IDoctorView {
    private final MedicalRecordService medicalRecordService;

    public ViewMedicalRecords(MedicalRecordService medicalRecordService,
                              MedicalRecordDataStore medicalRecordDataStore) {
        this.medicalRecordService = medicalRecordService;
    }

    @Override
    public void displayDoctorView(Object obj) {
        if (!(obj instanceof Doctor)) {
            System.out.println("Invalid object passed to ViewMedicalRecords.");
            return;
        }

        Doctor doctor = (Doctor) obj;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Medical Records ===");
            System.out.print("Enter Patient ID to view medical records (or type 'exit' to return): ");
            String patientId = scanner.nextLine().trim();

            if (patientId.equalsIgnoreCase("exit")) {
                return;
            }

            List<MedicalRecord> records = medicalRecordService.getRecordsForDoctor(doctor.getHospitalID());
            boolean found = false;

            for (MedicalRecord record : records) {
                if (record.getPatientId().equalsIgnoreCase(patientId)) {
                    System.out.println(record);
                    found = true;
                }
            }

            if (!found) {
                System.out.println("No medical records found for Patient ID: " + patientId);
            } else {
                // Optionally, allow updating medical records
                System.out.println("\nOptions:");
                System.out.println("1. Update Diagnosis");
                System.out.println("2. Update Prescriptions");
                System.out.println("3. Update Treatment Plan");
                System.out.println("4. Return");
                System.out.print("Enter choice: ");
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        updateMedicalRecord(scanner, patientId, "diagnosis");
                        break;
                    case "2":
                        updateMedicalRecord(scanner, patientId, "prescriptions");
                        break;
                    case "3":
                        updateMedicalRecord(scanner, patientId, "treatmentPlan");
                        break;
                    case "4":
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
        }
    }

    /**
     * Handles updating a specific field in a patient's medical record.
     *
     * @param scanner   The Scanner object for user input.
     * @param patientId The ID of the patient.
     * @param field     The field to update ("diagnosis", "prescriptions", "treatmentPlan").
     */
    private void updateMedicalRecord(Scanner scanner, String patientId, String field) {
        System.out.print("Enter new " + field + ": ");
        String newValue = scanner.nextLine().trim();

        System.out.print("Confirm update? (yes/no): ");
        String confirmation = scanner.nextLine().trim();

        if (confirmation.equalsIgnoreCase("yes") || confirmation.equalsIgnoreCase("y")) {
            medicalRecordService.updateMedicalRecord(patientId, field, newValue);
        } else {
            System.out.println("Update canceled.");
        }
    }
}
