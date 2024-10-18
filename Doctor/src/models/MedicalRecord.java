package models;

import java.time.LocalDate;

public class MedicalRecord {
    private String patientId;
    private String doctorId;
    private String diagnosis;
    private String prescriptions;
    private String treatmentPlan;
    private LocalDate lastUpdated;

    public MedicalRecord(String patientId, String doctorId, String diagnosis,
                        String prescriptions, String treatmentPlan, LocalDate lastUpdated) {
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.diagnosis = diagnosis;
        this.prescriptions = prescriptions;
        this.treatmentPlan = treatmentPlan;
        this.lastUpdated = lastUpdated;
    }

    // Getters and Setters

    public String getPatientId() {
        return patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
        updateLastUpdated();
    }

    public String getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(String prescriptions) {
        this.prescriptions = prescriptions;
        updateLastUpdated();
    }

    public String getTreatmentPlan() {
        return treatmentPlan;
    }

    public void setTreatmentPlan(String treatmentPlan) {
        this.treatmentPlan = treatmentPlan;
        updateLastUpdated();
    }

    public LocalDate getLastUpdated() {
        return lastUpdated;
    }

    private void updateLastUpdated() {
        this.lastUpdated = LocalDate.now();
    }

    @Override
    public String toString() {
        return "PatientID: " + patientId +
               " | Diagnosis: " + diagnosis +
               " | Prescriptions: " + prescriptions +
               " | Treatment Plan: " + treatmentPlan +
               " | Last Updated: " + lastUpdated;
    }
}
