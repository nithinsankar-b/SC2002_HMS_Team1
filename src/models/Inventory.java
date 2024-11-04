package models;

import enums.InventoryStatus;
import enums.ReplenishmentStatus;

/**
 * Represents the inventory of a medicine in the healthcare system.
 * This class contains information about the medicine's stock levels, 
 * alert thresholds, and replenishment status.
 */
public class Inventory {

    private String medicineName;
    private int currentStock;
    private int lowLevelAlert;
    private ReplenishmentStatus replenishmentStatus;
    private InventoryStatus inventoryStatus;

    /**
     * Constructs an Inventory object with the specified parameters.
     *
     * @param medicineName The name of the medicine.
     * @param currentStock The current stock level of the medicine.
     * @param lowLevelAlert The stock level that triggers a low stock alert.
     * @param replenishmentStatus The current status of the replenishment request.
     */
    public Inventory(String medicineName, int currentStock, int lowLevelAlert, ReplenishmentStatus replenishmentStatus) {
        this.medicineName = medicineName;
        this.currentStock = currentStock;
        this.lowLevelAlert = lowLevelAlert;
        this.replenishmentStatus = replenishmentStatus;
        this.inventoryStatus = inventoryStatus; // Note: This might need to be initialized explicitly
    }

    // Getters and setters

    /**
     * Returns the name of the medicine.
     *
     * @return The name of the medicine.
     */
    public String getMedicineName() {
        return medicineName;
    }

    /**
     * Sets the name of the medicine.
     *
     * @param medicineName The name of the medicine to set.
     */
    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    /**
     * Returns the current stock level of the medicine.
     *
     * @return The current stock level.
     */
    public int getCurrentStock() {
        return currentStock;
    }

    /**
     * Sets the current stock level of the medicine.
     *
     * @param currentStock The current stock level to set.
     */
    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    /**
     * Returns the low level alert threshold for the medicine.
     *
     * @return The low level alert threshold.
     */
    public int getLowLevelAlert() {
        return lowLevelAlert;
    }

    /**
     * Sets the low level alert threshold for the medicine.
     *
     * @param lowLevelAlert The low level alert threshold to set.
     */
    public void setLowLevelAlert(int lowLevelAlert) {
        this.lowLevelAlert = lowLevelAlert;
    }

    /**
     * Returns the current status of the replenishment request for the medicine.
     *
     * @return The replenishment status.
     */
    public ReplenishmentStatus getReplenishmentStatus() {
        return replenishmentStatus;
    }

    /**
     * Sets the replenishment status for the medicine.
     *
     * @param replenishmentStatus The replenishment status to set.
     */
    public void setReplenishmentStatus(ReplenishmentStatus replenishmentStatus) {
        this.replenishmentStatus = replenishmentStatus;
    }

    /**
     * Sets the inventory status for the medicine.
     *
     * @param inventoryStatus The inventory status to set.
     */
    public void setInventoryStatus(InventoryStatus inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
    }
}
