package models;

import enums.UserRole;

// Inherits User class
public class Pharmacist extends User {

    private String name;
    private String contactInformation; 

    // Constructor using User object to initialize hospitalID, password, and contact information
    public Pharmacist(User user, String name, String contactInformation) {
        super(user.getHospitalID(), user.getPassword(), UserRole.PHARMACIST);
        this.name = name; // Initialize with User data and set role to PHARMACIST
        this.contactInformation = contactInformation; // Set contact information
    }

    // Getter for the pharmacist's name
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

    // Optional: You may want to add a method to return pharmacist details
    @Override
    public String toString() {
        return "Pharmacist [ID=" + getHospitalID() + ", Name=" + name + ", Contact Information=" + contactInformation + ", Role=" + getRole() + "]";
    }

    
}
