package views;

import services.MedicalRecordService;

public class ViewMedicalRecords  {
    private final MedicalRecordService medicalRecordService;
    
    public ViewMedicalRecords(MedicalRecordService medicalRecordService) {
    	this.medicalRecordService = medicalRecordService;
    }
    
    //Viewing of MedicalRecords
    public void printMedicalRecords(String patientID) {
    	System.out.println("Displaying Medical Records for Patient ID: " + patientID);
    	MedicalRecordService m=new MedicalRecordService();
    	m.printMedicalRecord(patientID);
    }
}
