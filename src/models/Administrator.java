package models;

import enums.UserRole;
import java.util.HashMap;

/**
 * The Administrator class represents a user with administrative privileges in the hospital system.
 * It extends the User class and includes additional functionality for managing hospital staff.
 */
public class Administrator extends User {

    private HashMap<String, Staff> hospitalStaff;

    /**
     * Constructs an Administrator with the specified hospital ID, password, and role.
     * Initializes the hospital staff management system.
     *
     * @param hospitalID The ID of the hospital.
     * @param password   The administrator's password.
     * @param role       The role of the user (expected to be ADMINISTRATOR).
     */
    public Administrator(String hospitalID, String password, UserRole role) {
        super(hospitalID, password, role);
        super.setRole(UserRole.ADMINISTRATOR);
        this.hospitalStaff = new HashMap<>();
    }

    /**
     * Retrieves the map of hospital staff managed by the administrator.
     *
     * @return A HashMap containing staff IDs as keys and Staff objects as values.
     */
    public HashMap<String, Staff> getHospitalStaff() {
        return hospitalStaff;
    }

    /**
     * Sets the hospital staff map for the administrator.
     *
     * @param hospitalStaff A HashMap containing staff IDs as keys and Staff objects as values.
     */
    public void setHospitalStaff(HashMap<String, Staff> hospitalStaff) {
        this.hospitalStaff = hospitalStaff;
    }
}


















