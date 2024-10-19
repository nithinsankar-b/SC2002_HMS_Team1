package models;

import enums.MedicationStatus;

public class Medication {
    private String name;
    private int quantity; // Quantity of medication prescribed
    private MedicationStatus status; // Pending or Dispensed

    public Medication(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
        this.status = MedicationStatus.PENDING; // Default status
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public MedicationStatus getStatus() {
        return status;
    }

    public void setStatus(MedicationStatus status) {
        this.status = status;
    }
}
