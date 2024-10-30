package models;

import enums.InventoryStatus;
import enums.ReplenishmentStatus;

public class InventoryDisplay {
    private String name;
    private int quantity;
    private InventoryStatus status;
    private ReplenishmentStatus replenishmentStatus; // New field for ReplenishmentStatus

    public InventoryDisplay(String name, int quantity, InventoryStatus status, ReplenishmentStatus replenishmentStatus) {
        this.name = name;
        this.quantity = quantity;
        this.status = status;
        this.replenishmentStatus = replenishmentStatus; // Initialize the new field
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public InventoryStatus getStatus() {
        return status;
    }

    public ReplenishmentStatus getReplenishmentStatus() { // Getter for the new field
        return replenishmentStatus;
    }
}
