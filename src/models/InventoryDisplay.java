package models;

import enums.InventoryStatus;

public class InventoryDisplay {
    private String name;
    private int quantity;
    private InventoryStatus status;

    public InventoryDisplay(String name, int quantity, InventoryStatus status) {
        this.name = name;
        this.quantity = quantity;
        this.status = status;
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
}
