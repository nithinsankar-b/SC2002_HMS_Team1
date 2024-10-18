package services;

import models.MedicalRecord;
import stores.MedicalRecordDataStore;
import java.util.List;

public class MedicalRecordService {
    private final MedicalRecordDataStore medicalRecordDataStore;

    public MedicalRecordService(MedicalRecordDataStore dataStore) {
        this.medicalRecordDataStore = dataStore;
    }

    /**
     * Retrieves medical records for all patients under a specific doctor's care.
     *
     * @param doctorId The ID of the doctor.
     * @return A list of MedicalRecord objects.
     */
    public List<MedicalRecord> getRecordsForDoctor(String doctorId) {
        return medicalRecordDataStore.getRecordsForDoctor(doctorId);
    }

    /**
     * Updates a specific field in a patient's medical record.
     *
     * @param patientId    The ID of the patient.
     * @param field        The field to update (diagnosis, prescriptions, treatmentPlan).
     * @param newValue     The new value for the field.
     */
    public void updateMedicalRecord(String patientId, String field, String newValue) {
        medicalRecordDataStore.updateMedicalRecord(patientId, field, newValue);
        System.out.println("Medical record updated successfully.");
    }
}
