package services;

import models.Administrator;
import models.Inventory;
import models.Staff;
import models.User;
import stores.StaffDataStore;
import interfaces.IInventoryService;
import interfaces.IProjectAdmService;
import services.UserService;

import java.io.IOException;
import java.util.List;

import enums.UserRole;

/**
 * The ProjectAdminService class implements the IProjectAdmService interface
 * and provides methods for managing both staff and inventory. It uses dependency
 * injection for inventory management and handles the interaction with data stores.
 */
public class ProjectAdminService implements IProjectAdmService {

    private Administrator administrator;
    private StaffDataStore staffDataStore;
    private IInventoryService inventoryService;
    private UserService userService;

    private static final String STAFF_CSV_PATH = "data/Staff_List.csv";

    /**
     * Constructs a ProjectAdminService with the specified Administrator and IInventoryService.
     * Loads staff data from a CSV file upon initialization.
     *
     * @param administrator   The administrator managing the service.
     * @param inventoryService The inventory service to manage inventory-related operations.
     */
    public ProjectAdminService(Administrator administrator, IInventoryService inventoryService, UserService userService) {
        this.administrator = administrator;
        this.staffDataStore = new StaffDataStore();
        this.inventoryService = inventoryService;
        this.userService = userService;

        try {
            staffDataStore.loadStaffFromCSV(STAFF_CSV_PATH);
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    // ---------------------- Staff Management ----------------------

    /**
     * Adds or updates a staff member in the system.
     *
     * @param staffMember The staff member to be added or updated.
     */
    @Override
public void addOrUpdateStaff(Staff staffMember) {
    // Check if the staff member already exists in StaffDataStore
    Staff existingStaff = staffDataStore.getStaffList().get(staffMember.getId());

    // Determine the role
    String role;
    if (existingStaff != null) {
        // If the staff member exists, preserve the existing role
        role = existingStaff.getRole();
        System.out.println("Updating existing staff. Preserving role: " + role);
    } else {
        // For new staff, validate that the role is either Doctor or Pharmacist
        if (!"Doctor".equalsIgnoreCase(staffMember.getRole()) && !"Pharmacist".equalsIgnoreCase(staffMember.getRole())) {
            System.out.println("Error: Only Doctor or Pharmacist roles can be added.");
            return; // Exit the method if an invalid role is provided for new staff
        }
        role = staffMember.getRole(); // Use the provided role for new staff
        System.out.println("Adding new staff with role: " + role);
    }

    // Create a new Staff object with the preserved or validated role
    Staff staffToSave = new Staff(staffMember.getId(), staffMember.getName(), role, staffMember.getGender(), staffMember.getAge());

    // Add or update the staff member in StaffDataStore
    staffDataStore.addOrUpdateStaff(staffToSave);

    // Add or update the user in UserService
    User existingUser = userService.getUserById(staffMember.getId());
    String password = (existingUser != null) ? existingUser.getPassword() : "password"; // Default password for new users

    // Set the appropriate role for UserService only if the user is new
    UserRole userRole = UserRole.valueOf(role.toUpperCase());
    userService.addUser(new User(staffMember.getId(), password, userRole));

    // Write updated staff list to CSV
    try {
        staffDataStore.writeStaffToCSV(STAFF_CSV_PATH);
        System.out.println("Staff member added/updated: " + staffMember.getId());
    } catch (IOException e) {
        System.err.println("Error saving staff data: " + e.getMessage());
    }
}

    
    

    /**
     * Removes a staff member from the system by their ID.
     *
     * @param staffId The ID of the staff member to be removed.
     * @return True if the staff member was successfully removed, false otherwise.
     */
    @Override
    public boolean removeStaff(String staffId) {
        if (!staffDataStore.getStaffList().containsKey(staffId)) {
            System.out.println("Staff not found for ID: " + staffId);
            return false;
        }

        staffDataStore.removeStaff(staffId);
        userService.removeUser(staffId); // Remove from UserService as well

        try {
            staffDataStore.writeStaffToCSV(STAFF_CSV_PATH);
            System.out.println("Staff member removed: " + staffId);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving staff data: " + e.getMessage());
            return false;
        }
    }

    /**
     * Retrieves a list of all staff members.
     *
     * @return A list of all staff members.
     */
    @Override
    public List<Staff> getAllStaff() {
        return List.copyOf(staffDataStore.getStaffList().values());
    }

    // ---------------------- Inventory Management ----------------------

    /**
     * Updates the stock level for a specific medication.
     *
     * @param medicineName The name of the medication.
     * @param quantity     The new stock level.
     * @return True if the stock level was successfully updated, false otherwise.
     */
    @Override
    public boolean updateStockLevel(String medicineName, int quantity) {
        List<Inventory> inventoryList = inventoryService.getInventoryList();

        for (Inventory item : inventoryList) {
            if (item.getMedicineName().equalsIgnoreCase(medicineName)) {
                item.setCurrentStock(quantity);
                inventoryService.updateInventory(item);
                System.out.println("Updated stock level for " + medicineName);
                return true;
            }
        }
        System.out.println("Medicine not found: " + medicineName);
        return false;
    }

    /**
     * Updates the low stock alert level for a specific medication.
     *
     * @param medicineName  The name of the medication.
     * @param lowStockLevel The new low stock alert level.
     * @return True if the low stock alert level was successfully updated, false otherwise.
     */
    @Override
    public boolean updateLowStockAlert(String medicineName, int lowStockLevel) {
        List<Inventory> inventoryList = inventoryService.getInventoryList();
        for (Inventory item : inventoryList) {
            if (item.getMedicineName().equalsIgnoreCase(medicineName)) {
                item.setLowLevelAlert(lowStockLevel);
                inventoryService.updateInventory(item);
                System.out.println("Updated low stock alert for " + medicineName);
                return true;
            }
        }
        System.out.println("Medicine not found: " + medicineName);
        return false;
    }

    /**
     * Adds a new medication to the inventory.
     *
     * @param newMedication The new medication to be added.
     */
    public void addMedication(Inventory newMedication) {
        inventoryService.addMedication(newMedication);
        System.out.println("Medication added successfully: " + newMedication.getMedicineName());
    }

    /**
     * Removes a medication from the inventory.
     *
     * @param medicineName The name of the medication to be removed.
     * @return True if the medication was successfully removed, false otherwise.
     */
    @Override
    public boolean removeMedication(String medicineName) {
        boolean removed = inventoryService.removeMedication(medicineName);
        if (removed) {
            System.out.println("Medication removed successfully: " + medicineName);
        } else {
            System.out.println("Medicine does not exist: " + medicineName);
        }
        return removed;
    }

    /**
     * Retrieves a list of all inventory items.
     *
     * @return A list of all inventory items.
     */
    @Override
    public List<Inventory> getAllInventory() {
        return inventoryService.getInventoryList();
    }

    /**
     * Approves a replenishment request for a specific medication.
     *
     * @param medicineName The name of the medication.
     * @return True if the replenishment request was successfully approved, false otherwise.
     */
    @Override
    public boolean approveReplenishmentRequest(String medicineName) {
        return inventoryService.approveReplenishmentRequest(medicineName);
    }
}






