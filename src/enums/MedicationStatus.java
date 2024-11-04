package enums;

/**
 * Enumeration representing the status of a patient's medication.
 * This enum is used to track whether a prescribed medication is awaiting dispensing or has been dispensed.
 */
public enum MedicationStatus {
    
    /**
     * Indicates that the medication is pending and has not yet been dispensed.
     */
    PENDING,

    /**
     * Indicates that the medication has been dispensed to the patient.
     */
    DISPENSED
}
