package models;

import enums.UserRole;
import java.time.LocalDate;
import models.User;

// Inherits User class
public class Patient extends User {
    private String name;
    private LocalDate dateOfBirth;
    private String gender;
    private String bloodType;
    private String contactInformation; // Email address
    private boolean isRegistered; // Indicates if the patient is registered
    private String patientID;

    public Patient(String hospitalID, String password, UserRole role,String name, LocalDate dateOfBirth, String gender, String bloodType, String contactInformation) {// UserRole set to PATIENT
        super(hospitalID, password, role);
        // Initialize attributes based on the CSV file and other patient-specific data
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.bloodType = bloodType;
        this.contactInformation = contactInformation;
        this.isRegistered = true; // Initialize the registration status
    }

    // Getters
    // To get PatientID use the method defined in User getHospitalID();

    public String getName() {
        return name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getContactInformation() {
        return contactInformation; // Get email address
    }

    public boolean getIsRegistered() {
        return isRegistered;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation; // Set email address
    }

    public void setIsRegistered(boolean isRegistered) {
        this.isRegistered = isRegistered;
    }
}
