package models;
import enums.PrescriptionStatus;

public class AppointmentOutcomeRecord {




	    private String patientId;
	    private String medicineName;
	    private PrescriptionStatus status;

	    public AppointmentOutcomeRecord(String patientId, String medicineName, PrescriptionStatus status) {
	        this.patientId = patientId;
	        this.medicineName = medicineName;
	        this.status = status;
	    }

	    // Getters and Setters
	    public String getPatientId() {
	        return patientId;
	    }

	    public void setPatientId(String patientId) {
	        this.patientId = patientId;
	    }

	    public String getMedicineName() {
	        return medicineName;
	    }

	    public void setMedicineName(String medicineName) {
	        this.medicineName = medicineName;
	    }

	    public PrescriptionStatus getStatus() {
	        return status;
	    }

	    public void setStatus(PrescriptionStatus status) {
	        this.status = status;
	    }

	    @Override
	    public String toString() {
	        return "AppointmentOutcomeRecord{" +
	                "patientId='" + patientId + '\'' +
	                ", medicineName='" + medicineName + '\'' +
	                ", status=" + status +
	                '}';
	    }
	}



