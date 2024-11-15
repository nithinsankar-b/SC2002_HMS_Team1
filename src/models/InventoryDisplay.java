package models;

import enums.InventoryStatus;
import enums.ReplenishmentStatus;

/**
 * Represents the display information for a medicine in the inventory system.
 * This class holds the name, quantity, inventory status, and replenishment status of a medicine.
 */
public class InventoryDisplay {

    private String name;
    private int quantity;
    private InventoryStatus status;
    private ReplenishmentStatus replenishmentStatus;

    /**
     * Constructs an InventoryDisplay object with the specified parameters.
     *
     * @param name The name of the medicine.
     * @param quantity The quantity of the medicine available in the inventory.
     * @param status The current inventory status of the medicine.
     * @param replenishmentStatus The current replenishment status of the medicine.
     */
    public InventoryDisplay(String name, int quantity, InventoryStatus status, ReplenishmentStatus replenishmentStatus) {
        this.name = name;
        this.quantity = quantity;
        this.status = status;
        this.replenishmentStatus = replenishmentStatus;
    }

    // Getters

    /**
     * Returns the name of the medicine.
     *
     * @return The name of the medicine.
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the quantity of the medicine available in the inventory.
     *
     * @return The quantity of the medicine.
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Returns the current inventory status of the medicine.
     *
     * @return The inventory status.
     */
    public InventoryStatus getStatus() {
        return status;
    }

    /**
     * Returns the current replenishment status of the medicine.
     *
     * @return The replenishment status.
     */
    public ReplenishmentStatus getReplenishmentStatus() {
        return replenishmentStatus;
    }

    /**
     * Helper method to determine the inventory status based on stock and alert levels.
     * @param currentStock The current stock level.
     * @param lowLevelAlert The low stock alert level.
     * @return InventoryStatus - whether the item is low or sufficient.
     */
    public static InventoryStatus getInventoryStatus(int currentStock, int lowLevelAlert) {
        return currentStock <= lowLevelAlert ? InventoryStatus.LOWSTOCK : InventoryStatus.INSTOCK;
    }
}
