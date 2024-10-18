package Models;

import enums.UserRole;

// Patient class represents a patient in HMS
// Extends from parent class User to inherit basic user attributes

public class Patient {
    // Attributes
    private String patientID;
    private Boolean isRegistered;

    // Constructor
    public Student(String patientID, String email, String password, boolean isRegistered){
        // Inherit User methods
        super(patientID, name, email, password);
        super.setRole(User.Role.PATIENT);
        this.patientID = patientID;
        this.isRegistered = isRegistered;
    }

    // Getters
    public boolean getIsRegistered(){
        return isRegistered;
    }

    public String getPatientID(){
        return patientID;
    }

    // Setters
    public void setIsRegistered(){
        this.isRegistered = true;
    }

}
