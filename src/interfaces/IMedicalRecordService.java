package interfaces;

import models.MedicalRecord;

public interface IMedicalRecordService {
	
    void addNewDiagnosisPrescription(String patientID, String newDiagnosis, String newPrescription);
    MedicalRecord getMedicalRecord(String patientID);
    void printMedicalRecord(String patientID);
    
}
