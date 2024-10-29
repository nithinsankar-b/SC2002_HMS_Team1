package interfaces;

import models.Staff;
import models.Inventory;
import java.util.List;

public interface IProjectAdmService {
    
    // Staff Management
    void addOrUpdateStaff(Staff staffMember);  // Add or update a staff member
    boolean removeStaff(String staffId);          // Remove a staff member by ID
    List<Staff> getAllStaff();                 // Retrieve all staff members

    // Inventory Management
    boolean updateStockLevel(String medicineName, int quantity);  // Update stock level for a specific medicine
    boolean updateLowStockAlert(String medicineName, int lowStockLevel);  // Update the low stock alert level for a medicine
    boolean removeMedication(String medicineName);  // Remove a medication from the inventory
    List<Inventory> getAllInventory();    // Retrieve all inventory items
}




