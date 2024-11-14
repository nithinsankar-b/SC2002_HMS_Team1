package interfaces;

import models.Inventory;
import java.util.List;

/**
 * The IInventoryService interface defines the methods for managing the hospital's inventory,
 * including adding, updating, and removing medications, as well as handling stock levels
 * and replenishment requests.
 */
public interface IInventoryService {

    /**
     * Retrieves the list of all inventory items.
     *
     * @return A list of Inventory objects representing the current inventory.
     */
    List<Inventory> getInventoryList();

    /**
     * Updates an existing inventory item.
     *
     * @param updatedInventory The updated Inventory object.
     */
    void updateInventory(Inventory updatedInventory);

    /**
     * Adds a new medication to the inventory.
     *
     * @param newMedication The new Inventory object representing the medication to be added.
     */
    void addMedication(Inventory newMedication);

    /**
     * Removes a medication from the inventory based on its name.
     *
     * @param medicineName The name of the medication to be removed.
     * @return True if the medication was successfully removed, false otherwise.
     */
    boolean removeMedication(String medicineName);

    /**
     * Updates the stock level for a specific medication.
     *
     * @param medicineName The name of the medication.
     * @param quantity     The new stock quantity.
     * @return True if the stock level was successfully updated, false otherwise.
     */
    boolean updateStockLevel(String medicineName, int quantity);

    /**
     * Updates the low stock alert level for a specific medication.
     *
     * @param medicineName The name of the medication.
     * @param lowStockLevel The new low stock alert level.
     * @return True if the low stock alert level was successfully updated, false otherwise.
     */
    boolean updateLowStockAlert(String medicineName, int lowStockLevel);

    /**
     * Submits a replenishment request for a specific medication.
     */
    void submitReplenishmentRequest();

    /**
     * Approves a replenishment request for a specific medication.
     *
     * @param medicineName The name of the medication for which the replenishment request is approved.
     * @return True if the replenishment request was successfully approved, false otherwise.
     */
    boolean approveReplenishmentRequest(String medicineName);
}





