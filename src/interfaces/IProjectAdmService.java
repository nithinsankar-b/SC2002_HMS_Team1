package interfaces;

import models.Staff;
import models.Inventory;
import java.util.List;

/**
 * The IProjectAdmService interface defines the methods for managing both
 * hospital staff and inventory, including adding, updating, and removing staff
 * and inventory items.
 */
public interface IProjectAdmService {

    // Staff Management

    /**
     * Adds or updates a staff member's information.
     *
     * @param staffMember The Staff object containing the staff member's details.
     */
    void addOrUpdateStaff(Staff staffMember);

    /**
     * Removes a staff member based on their ID.
     *
     * @param staffId The ID of the staff member to be removed.
     * @return True if the staff member was successfully removed, false otherwise.
     */
    boolean removeStaff(String staffId);

    /**
     * Retrieves a list of all hospital staff members.
     *
     * @return A list of Staff objects representing all staff members.
     */
    List<Staff> getAllStaff();

    // Inventory Management

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
     * @param medicineName  The name of the medication.
     * @param lowStockLevel The new low stock alert level.
     * @return True if the low stock alert level was successfully updated, false otherwise.
     */
    boolean updateLowStockAlert(String medicineName, int lowStockLevel);

    /**
     * Removes a medication from the inventory based on its name.
     *
     * @param medicineName The name of the medication to be removed.
     * @return True if the medication was successfully removed, false otherwise.
     */
    boolean removeMedication(String medicineName);

    /**
     * Retrieves a list of all inventory items.
     *
     * @return A list of Inventory objects representing all inventory items.
     */
    List<Inventory> getAllInventory();

    /**
     * Approves a replenishment request for a specific medication.
     *
     * @param medicineName The name of the medication.
     * @return True if the replenishment request was successfully approved, false otherwise.
     */
    boolean approveReplenishmentRequest(String medicineName);
}






