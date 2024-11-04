package models;

import enums.InventoryStatus;
import enums.ReplenishmentStatus;

/**
 * The InventoryDisplay class is used for displaying inventory information
 * in a user-friendly format, including the name, quantity, status, and
 * replenishment status of the medication.
 */
public class InventoryDisplay {

    private String name;
    private int quantity;
    private InventoryStatus status;
    private ReplenishmentStatus replenishmentStatus;

    /**
     * Constructs an InventoryDisplay object with the specified details.
     *
     * @param name                The name of the medication.
     * @param quantity            The quantity of the medication available in stock.
     * @param status              The current inventory status of the medication.
     * @param replenishmentStatus The current replenishment status of the medication.
     */
    public InventoryDisplay(String name, int quantity, InventoryStatus status, ReplenishmentStatus replenishmentStatus) {
        this.name = name;
        this.quantity = quantity;
        this.status = status;
        this.replenishmentStatus = replenishmentStatus;
    }

    /**
     * Retrieves the name of the medication.
     *
     * @return The name of the medication.
     */
    public String getName() {
        return name;
    }

    /**
     * Retrieves the quantity of the medication available in stock.
     *
     * @return The stock quantity of the medication.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Retrieves the current inventory status of the medication.
     *
     * @return The inventory status.
     */
    public InventoryStatus getStatus() {
        return status;
    }

    /**
     * Retrieves the current replenishment status of the medication.
     *
     * @return The replenishment status.
     */
    public ReplenishmentStatus getReplenishmentStatus() {
        return replenishmentStatus;
    }
}
