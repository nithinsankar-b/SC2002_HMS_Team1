package models;

import enums.InventoryStatus;
import enums.ReplenishmentStatus;

/**
 * The Inventory class represents a medication in the hospital's inventory system.
 * It includes details such as the medicine name, current stock levels, low-level alert thresholds,
 * and the replenishment and inventory statuses.
 */
public class Inventory {

    private String medicineName;
    private int currentStock;
    private int lowLevelAlert;
    private ReplenishmentStatus replenishmentStatus;
    private InventoryStatus inventoryStatus;

    /**
     * Constructs an Inventory object with the specified details.
     *
     * @param medicineName        The name of the medication.
     * @param currentStock        The current stock level of the medication.
     * @param lowLevelAlert       The stock level at which a low stock alert should be triggered.
     * @param replenishmentStatus The current replenishment status of the medication.
     */
    public Inventory(String medicineName, int currentStock, int lowLevelAlert, ReplenishmentStatus replenishmentStatus) {
        this.medicineName = medicineName;
        this.currentStock = currentStock;
        this.lowLevelAlert = lowLevelAlert;
        this.replenishmentStatus = replenishmentStatus != null ? replenishmentStatus : ReplenishmentStatus.REPLENISHED;
    }

    /**
     * Retrieves the name of the medication.
     *
     * @return The name of the medication.
     */
    public String getMedicineName() {
        return medicineName;
    }

    /**
     * Sets the name of the medication.
     *
     * @param medicineName The new name of the medication.
     */
    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    /**
     * Retrieves the current stock level of the medication.
     *
     * @return The current stock level.
     */
    public int getCurrentStock() {
        return currentStock;
    }

    /**
     * Sets the current stock level of the medication.
     *
     * @param currentStock The new stock level.
     */
    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    /**
     * Retrieves the low stock alert level for the medication.
     *
     * @return The low stock alert level.
     */
    public int getLowLevelAlert() {
        return lowLevelAlert;
    }

    /**
     * Sets the low stock alert level for the medication.
     *
     * @param lowLevelAlert The new low stock alert level.
     */
    public void setLowLevelAlert(int lowLevelAlert) {
        this.lowLevelAlert = lowLevelAlert;
    }

    /**
     * Retrieves the current replenishment status of the medication.
     *
     * @return The replenishment status.
     */
    public ReplenishmentStatus getReplenishmentStatus() {
        return replenishmentStatus;
    }

    /**
     * Sets the replenishment status of the medication.
     *
     * @param replenishmentStatus The new replenishment status.
     */
    public void setReplenishmentStatus(ReplenishmentStatus replenishmentStatus) {
        this.replenishmentStatus = replenishmentStatus;
    }

    /**
     * Sets the inventory status of the medication.
     *
     * @param inventoryStatus The new inventory status.
     */
    public void setInventoryStatus(InventoryStatus inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
    }
}


