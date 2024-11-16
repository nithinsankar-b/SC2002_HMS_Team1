package views;

import services.MedicalRecordService;

/**
 * The MedicalRecordsView class provides a view for displaying medical records of patients.
 * This class interacts with the MedicalRecordService to fetch and display records.
 */
public class MedicalRecordsView {

    private final MedicalRecordService medicalRecordService;

    /**
     * Constructor for initializing the MedicalRecordsView with a MedicalRecordService.
     *
     * @param medicalRecordService The service for managing medical records.
     */
    public MedicalRecordsView(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    /**
     * Displays the medical records for a specific patient by their patient ID.
     * Fetches the records from the MedicalRecordService and prints them.
     *
     * @param patientID The ID of the patient whose medical records need to be displayed.
     */
    public void printMedicalRecords(String patientID) {
        System.out.println("Displaying Medical Records for Patient ID: " + patientID);
        // Create a new instance of MedicalRecordService to fetch and print records
        MedicalRecordService medicalRecordService = new MedicalRecordService();
        medicalRecordService.printMedicalRecord(patientID);
    }
}

