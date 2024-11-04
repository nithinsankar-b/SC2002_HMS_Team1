package models;

import enums.UserRole;
import models.User;

/**
 * Represents a doctor in the healthcare system, extending the User class.
 * This class contains information specific to doctors, including their name and contact information.
 */
public class Doctor extends User {
	
    private String name;
    private String contactInformation; 

    /**
     * Constructs a new Doctor object using the specified User object.
     *
     * @param user The User object containing hospital ID and password.
     * @param name The name of the doctor.
     * @param contactInformation The contact information of the doctor.
     */
    public Doctor(User user, String name, String contactInformation) {
        super(user.getHospitalID(), user.getPassword(), UserRole.DOCTOR);
        this.name = name; // Initialize with User data and set role to Doctor
        this.contactInformation = contactInformation; // Set contact information
    }

    /**
     * Returns the name of the doctor.
     *
     * @return The name of the doctor.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the contact information of the doctor.
     *
     * @return The contact information of the doctor.
     */
    public String getContactInformation() {
        return contactInformation;
    }

    /**
     * Sets the contact information for the doctor.
     *
     * @param contactInformation The contact information to set.
     */
    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    /**
     * Returns a string representation of the doctor, including their ID, name, contact information, and role.
     *
     * @return A string representation of the doctor.
     */
    @Override
    public String toString() {
        return "Doctor [ID=" + getHospitalID() + ", Name=" + name + ", Contact Information=" + contactInformation + ", Role=" + getRole() + "]";
    }
}
