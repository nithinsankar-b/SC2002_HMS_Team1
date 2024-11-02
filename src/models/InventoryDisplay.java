package models;

import enums.InventoryStatus;
import enums.ReplenishmentStatus;

public class InventoryDisplay {
    private String name;
    private int quantity;
    private InventoryStatus status;
    private ReplenishmentStatus replenishmentStatus; // Add this field


    public InventoryDisplay(String name, int quantity, InventoryStatus status, ReplenishmentStatus replenishmentStatus) {
        this.name = name;
        this.quantity = quantity;
        this.status = status;
        this.replenishmentStatus = replenishmentStatus;
        
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }

    public InventoryStatus getStatus() {
        return status;
    }
    public ReplenishmentStatus getReplenishmentStatus() { // Optional getter for ReplenishmentStatus
        return replenishmentStatus;
    }
}
