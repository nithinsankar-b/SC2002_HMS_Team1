package models;

import enums.MedicationStatus;

/**
 * Represents a medication item in the healthcare system.
 * This class holds the details of a medication including its name,
 * quantity available, and its current status.
 */
public class Medication {
    private String name; // The name of the medication
    private int quantity; // The quantity available
    private MedicationStatus status; // The current status of the medication

    /**
     * Constructs a Medication object with the specified details.
     *
     * @param name The name of the medication.
     * @param quantity The quantity of the medication available.
     * @param status The current status of the medication.
     */
    public Medication(String name, int quantity, MedicationStatus status) {
        this.name = name;
        this.quantity = quantity;
        this.status = status;
    }

    // Getters and Setters

    /**
     * Returns the name of the medication.
     *
     * @return The name of the medication.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the medication.
     *
     * @param name The name to set for the medication.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the quantity of the medication available.
     *
     * @return The quantity of the medication.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of the medication.
     *
     * @param quantity The quantity to set for the medication.
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Returns the current status of the medication.
     *
     * @return The status of the medication.
     */
    public MedicationStatus getStatus() {
        return status;
    }

    /**
     * Sets the current status of the medication.
     *
     * @param status The status to set for the medication.
     */
    public void setStatus(MedicationStatus status) {
        this.status = status;
    }

    /**
     * Returns a string representation of the Medication object.
     *
     * @return A string containing the medication details.
     */
    @Override
    public String toString() {
        return "Medication{" +
                "name='" + name + '\'' +
                ", quantity=" + quantity +
                ", status=" + status +
                '}';
    }

    public void setMedicationStatus(enums.MedicationStatus medicationStatus) {

    }
}
