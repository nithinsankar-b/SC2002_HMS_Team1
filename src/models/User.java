package models;

import enums.UserRole;

/**
 * Represents a user in the hospital system, with attributes for hospital ID, password, and role.
 * The `User` class serves as a base class for more specific types of users, such as patients, doctors, or administrators.
 */
public class User {

    private String hospitalID; // The unique identifier for the user
    private String password;   // The user's password for authentication
    private UserRole role;     // The role of the user (e.g., PATIENT, DOCTOR, PHARMACIST, ADMINISTRATOR)

    /**
     * Constructs a new `User` object with the specified hospital ID, password, and role.
     *
     * @param hospitalID The unique identifier for the user.
     * @param password   The password for the user.
     * @param role       The role of the user (e.g., PATIENT, DOCTOR, ADMINISTRATOR).
     */
    public User(String hospitalID, String password, UserRole role) {
        this.hospitalID = hospitalID;
        this.password = password;
        this.role = role;
    }

    /**
     * Retrieves the hospital ID of the user.
     *
     * @return The hospital ID.
     */
    public String getHospitalID() {
        return hospitalID;
    }

    /**
     * Updates the hospital ID of the user.
     *
     * @param hospitalID The new hospital ID.
     */
    public void setHospitalID(String hospitalID) {
        this.hospitalID = hospitalID;
    }

    /**
     * Retrieves the user's password.
     *
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Updates the user's password.
     *
     * @param password The new password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retrieves the user's role.
     *
     * @return The role of the user (e.g., PATIENT, DOCTOR, PHARMACIST, ADMINISTRATOR).
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Updates the user's role.
     *
     * @param role The new role for the user.
     */
    public void setRole(UserRole role) {
        this.role = role;
    }
}

