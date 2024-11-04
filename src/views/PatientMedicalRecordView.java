package views;

import services.MedicalRecordService;

/**
 * The {@code PatientMedicalRecordView} class provides a user interface for displaying a patient's medical records.
 * It interacts with the {@code MedicalRecordService} to fetch and display the medical records for a given patient.
 */
public class PatientMedicalRecordView {

    private final MedicalRecordService medicalRecordService; // Service dependency

    /**
     * Constructs a {@code PatientMedicalRecordView} with the specified medical record service.
     *
     * @param medicalRecordService the service used to manage medical record operations
     */
    public PatientMedicalRecordView(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    /**
     * Prints the medical records for the specified patient ID.
     *
     * @param patientID the ID of the patient whose medical records are to be displayed
     */
    public void printMedicalRecords(String patientID) {
        System.out.println("Displaying Medical Records for Patient ID: " + patientID);
        medicalRecordService.printMedicalRecord(patientID);
    }
}
