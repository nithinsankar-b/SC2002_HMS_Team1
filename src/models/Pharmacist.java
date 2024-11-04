package models;

import enums.UserRole;
import models.User;

/**
 * Represents a pharmacist in the healthcare system.
 * This class extends the User class and adds specific attributes related to pharmacists.
 */
public class Pharmacist extends User {

    private String name; // The name of the pharmacist
    private String contactInformation; // Contact information for the pharmacist

    /**
     * Constructs a Pharmacist object with the specified user details and pharmacist-specific information.
     *
     * @param user The User object containing hospitalID and password.
     * @param name The name of the pharmacist.
     * @param contactInformation The contact information for the pharmacist.
     */
    public Pharmacist(User user, String name, String contactInformation) {
        super(user.getHospitalID(), user.getPassword(), UserRole.PHARMACIST);
        this.name = name; // Initialize with User data and set role to PHARMACIST
        this.contactInformation = contactInformation; // Set contact information
    }

    // Getters

    /**
     * Returns the name of the pharmacist.
     *
     * @return The name of the pharmacist.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the contact information of the pharmacist.
     *
     * @return The contact information of the pharmacist.
     */
    public String getContactInformation() {
        return contactInformation;
    }

    // Setter

    /**
     * Sets the contact information for the pharmacist.
     *
     * @param contactInformation The contact information to set.
     */
    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    /**
     * Returns a string representation of the Pharmacist object.
     *
     * @return A string containing the pharmacist details.
     */
    @Override
    public String toString() {
        return "Pharmacist [ID=" + getHospitalID() + ", Name=" + name + ", Contact Information=" + contactInformation + ", Role=" + getRole() + "]";
    }
}
