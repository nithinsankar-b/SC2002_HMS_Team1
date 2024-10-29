package services;

import models.Administrator;
import models.Inventory;
import models.Staff;
import stores.StaffDataStore;
import interfaces.IInventoryService;
import interfaces.IProjectAdmService;

import java.io.IOException;
import java.util.List;

public class ProjectAdminService implements IProjectAdmService {
    private Administrator administrator;
    private StaffDataStore staffDataStore;
    private IInventoryService inventoryService;

    private static final String STAFF_CSV_PATH = "data/Staff_List.csv";

    // Constructor for the service, initializing with an Administrator and injected InventoryService
    public ProjectAdminService(Administrator administrator, IInventoryService inventoryService) {
        this.administrator = administrator;
        this.staffDataStore = new StaffDataStore();
        this.inventoryService = inventoryService;  // Dependency injection for inventoryService

        try {
            // Load CSV files for staff
            staffDataStore.loadStaffFromCSV(STAFF_CSV_PATH);  // Load staff data from CSV
        } catch (IOException e) {
            System.err.println("Error loading data: " + e.getMessage());
        }
    }

    // ---------------------- Staff Management ----------------------

    @Override
    public void addOrUpdateStaff(Staff staffMember) {
        staffDataStore.addOrUpdateStaff(staffMember);  // Add or update staff
        try {
            staffDataStore.writeStaffToCSV(STAFF_CSV_PATH);  // Save to CSV
            System.out.println("Staff member added/updated: " + staffMember.getId());
        } catch (IOException e) {
            System.err.println("Error saving staff data: " + e.getMessage());
        }
    }

    @Override
    public boolean removeStaff(String staffId) {
        if (!staffDataStore.getStaffList().containsKey(staffId)) {
            System.out.println("Staff not found for ID: " + staffId);
            return false;  // Staff not found
        }
        staffDataStore.removeStaff(staffId);  // Remove staff by ID
        try {
            staffDataStore.writeStaffToCSV(STAFF_CSV_PATH);  // Save to CSV after removal
            System.out.println("Staff member removed: " + staffId);
            return true;
        } catch (IOException e) {
            System.err.println("Error saving staff data: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Staff> getAllStaff() {
        return List.copyOf(staffDataStore.getStaffList().values());
    }

    // ---------------------- Inventory Management ----------------------

    // Update stock level for a given medicine
    public boolean updateStockLevel(String medicineName, int quantity) {
        List<Inventory> inventoryList = inventoryService.getInventoryList(); // Use inventoryService

        for (Inventory item : inventoryList) {
            if (item.getMedicineName().equalsIgnoreCase(medicineName)) {
                item.setCurrentStock(quantity);
                inventoryService.updateInventory(item);  // Update through inventoryService
                System.out.println("Updated stock level for " + medicineName);
                return true;
            }
        }
        System.out.println("Medicine not found: " + medicineName);
        return false;
    }

    // Update low stock alert level for a medicine
    public boolean updateLowStockAlert(String medicineName, int lowStockLevel) {
        List<Inventory> inventoryList = inventoryService.getInventoryList(); // Use inventoryService
        for (Inventory item : inventoryList) {
            if (item.getMedicineName().equalsIgnoreCase(medicineName)) {
                item.setLowLevelAlert(lowStockLevel);
                inventoryService.updateInventory(item);  // Update through inventoryService
                System.out.println("Updated low stock alert for " + medicineName);
                return true;
            }
        }
        System.out.println("Medicine not found: " + medicineName);
        return false;
    }

    public void addMedication(Inventory newMedication) {
        inventoryService.addMedication(newMedication);  // Delegate to inventoryService
        System.out.println("Medication added successfully: " + newMedication.getMedicineName());
    }

    public boolean removeMedication(String medicineName) {
        boolean removed = inventoryService.removeMedication(medicineName);  // Delegate to inventoryService
        if (removed) {
            System.out.println("Medication removed successfully: " + medicineName);
        } else {
            System.out.println("Medicine does not exist: " + medicineName);
        }
        return removed;
    }

    public List<Inventory> getAllInventory() {
        return inventoryService.getInventoryList();  // Delegate to inventoryService
    }

    public boolean approveReplenishmentRequest(String medicineName) {
        // Call the approveReplenishmentRequest method in inventoryService
        return inventoryService.approveReplenishmentRequest(medicineName);
    }
}





