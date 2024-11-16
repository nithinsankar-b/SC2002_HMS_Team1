package enums;

/**
 * Enum representing the various statuses an appointment can have.
 */
public enum AppointmentStatus {
    /**
     * Indicates that the appointment is pending approval.
     */
    PENDING,

    /**
     * Indicates that the appointment has been confirmed.
     */
    CONFIRMED,

    /**
     * Indicates that the appointment has been completed.
     */
    COMPLETED,

    /**
     * Indicates that the appointment has been canceled.
     */
    CANCELLED

    // Uncomment and use if a "RESCHEDULED" status is needed in the future.
    // RESCHEDULED
}
