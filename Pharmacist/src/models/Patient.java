package models;

public class Patient {


	    private String patientId;
	    private String name;

	    public Patient(String patientId, String name) {
	        this.patientId = patientId;
	        this.name = name;
	    }

	    // Getters and setters
	    public String getPatientId() {
	        return patientId;
	    }

	    public void setPatientId(String patientId) {
	        this.patientId = patientId;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }
	}



