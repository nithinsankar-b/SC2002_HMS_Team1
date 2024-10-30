package models;
import enums.UserRole;

import models.User;

public class Doctor extends User {
	
    private String name;
    private String contactInformation; 

    // Constructor using User object to initialize hospitalID, password, and contact information
    public Doctor(User user, String name, String contactInformation) {
        super(user.getHospitalID(), user.getPassword(), UserRole.DOCTOR);
        this.name = name; // Initialize with User data and set role to Doctor
        this.contactInformation = contactInformation; // Set contact information
    }

    // Getter for the Doctor's name
    public String getName() {
        return name;
    }

    // Getter for contact information
    public String getContactInformation() {
        return contactInformation;
    }

    // Setter for contact information
    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    // Optional: You may want to add a method to return Doctor details
    @Override
    public String toString() {
        return "Doctor [ID=" + getHospitalID() + ", Name=" + name + ", Contact Information=" + contactInformation + ", Role=" + getRole() + "]";
    }

    
}
