package interfaces;

import models.MedicalRecord;

/**
 * Interface for managing medical records in the system.
 * This interface defines methods for adding new diagnoses and prescriptions,
 * retrieving a patient's medical record, and printing the details of a medical record.
 */
public interface IMedicalRecordService {

    /**
     * Adds a new diagnosis and prescription to the medical record of a specified patient.
     *
     * @param patientID      The unique identifier of the patient.
     * @param newDiagnosis   The new diagnosis to be added to the patient's medical record.
     * @param newPrescription The new prescription to be added to the patient's medical record.
     */
    void addNewDiagnosisPrescription(String patientID, String newDiagnosis, String newPrescription);

    /**
     * Retrieves the medical record of a specified patient.
     *
     * @param patientID The unique identifier of the patient.
     * @return The medical record of the patient, or {@code null} if no record is found.
     */
    MedicalRecord getMedicalRecord(String patientID);

    /**
     * Prints the medical record of a specified patient to the console.
     *
     * @param patientID The unique identifier of the patient whose record is to be printed.
     */
    void printMedicalRecord(String patientID);
}

