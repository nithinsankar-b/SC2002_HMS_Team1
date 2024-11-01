package models;

import enums.UserRole;

/**
 * Represents a user in the hospital management system.
 */
public class User {
    private String hospitalID;
    private String password;
    private UserRole role;

    /**
     * Constructs a User with the specified hospital ID, password, and role.
     *
     * @param hospitalID the unique identifier for the user in the hospital system
     * @param password   the password associated with the user's account
     * @param role       the role of the user in the hospital system (e.g., patient, doctor, pharmacist)
     */
    public User(String hospitalID, String password, UserRole role) {
        this.hospitalID = hospitalID;
        this.password = password;
        this.role = role;
    }

    /**
     * Returns the hospital ID of the user.
     *
     * @return the hospital ID
     */
    public String getHospitalID() {
        return hospitalID;
    }

    /**
     * Returns the password of the user.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the role of the user.
     *
     * @return the user role
     */
    public UserRole getRole() {
        return role;
    }

    /**
     * Sets the hospital ID of the user.
     *
     * @param hospitalID the new hospital ID
     */
    public void setHospitalID(String hospitalID) {
        this.hospitalID = hospitalID;
    }

    /**
     * Sets the password of the user.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the role of the user.
     *
     * @param role the new user role
     */
    public void setRole(UserRole role) {
        this.role = role;
    }
}
