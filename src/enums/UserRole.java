package enums;

/**
 * Enum representing the roles of users in the system.
 * Each role corresponds to a specific type of user and their associated permissions.
 */
public enum UserRole {
    /**
     * Represents a patient user, who can view and manage their appointments and medical records.
     */
    PATIENT,

    /**
     * Represents a doctor user, who can manage appointments, view patient records, and update medical information.
     */
    DOCTOR,

    /**
     * Represents a pharmacist user, who can manage medication inventory, process prescriptions, and handle replenishment requests.
     */
    PHARMACIST,

    /**
     * Represents an administrator user, who can oversee the system, manage staff, inventory, and appointments.
     */
    ADMINISTRATOR
}
