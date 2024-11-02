package views;

import services.MedicalRecordService;


public class PatientMedicalRecordView  {



    private final MedicalRecordService medicalRecordService;
    
    public PatientMedicalRecordView(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;    }
        //Viewing of MedicalRecords
    public void printMedicalRecords(String patientID) {        System.out.println("Displaying Medical Records for Patient ID: " + patientID);
        MedicalRecordService m=new MedicalRecordService();        m.printMedicalRecord(patientID);
    }}