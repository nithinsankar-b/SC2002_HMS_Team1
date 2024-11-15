package services;

import models.Administrator;
import models.Inventory;
import models.Staff;
import models.User;
import stores.StaffDataStore;
import interfaces.IInventoryService;
import interfaces.IProjectAdmService;
import services.UserService;

import java.io.*;
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
    private static final String DOCTOR_CSV_PATH = "data/doctor.csv";
    private static final String PHARMACIST_CSV_PATH = "data/pharmacist.csv";

    /**
     * Constructs a ProjectAdminService with the specified Administrator and IInventoryService.
     * Loads staff data from a CSV file upon initialization.
     *
     * @param administrator   The administrator managing the service.
     * @param inventoryService The inventory service to manage inventory-related operations.
     * @param userService The user service to manage user-related operations.
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
        
        // Update the respective doctor or pharmacist CSV
        if (role.equalsIgnoreCase("Doctor")) {
            addOrUpdateDoctorCSV(staffMember);
        } else if (role.equalsIgnoreCase("Pharmacist")) {
            addOrUpdatePharmacistCSV(staffMember);
        }
        
    } catch (IOException e) {
        System.err.println("Error saving staff data: " + e.getMessage());
    }
}


    private void addOrUpdateDoctorCSV(Staff staffMember) {
        updateOrAppendToCSV("data/doctor.csv", staffMember);
    }
    
    private void addOrUpdatePharmacistCSV(Staff staffMember) {
        updateOrAppendToCSV("data/pharmacist.csv", staffMember);
    }
    private void updateOrAppendToCSV(String csvPath, Staff staffMember) {
        File inputFile = new File(csvPath);
        File tempFile = new File("temp.csv");
    
        boolean updated = false;
    
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
    
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(staffMember.getId() + ",")) {
                    // Replace the line with the updated information for the staff member
                    writer.write(staffMember.getId() + "," + staffMember.getName() + ",default@example.com");
                    writer.newLine();
                    updated = true;
                } else {
                    // Copy the existing line
                    writer.write(line);
                    writer.newLine();
                }
            }
    
            // If the staff member was not found in the existing file, append as a new line
            if (!updated) {
                writer.write(staffMember.getId() + "," + staffMember.getName() + ",default@example.com");
                writer.newLine();
            }
    
        } catch (IOException e) {
            System.err.println("Error updating/adding to " + csvPath + ": " + e.getMessage());
        }
    
        // Replace the original file with the updated temporary file
        if (!inputFile.delete()) {
            System.err.println("Could not delete original file");
        }
        if (!tempFile.renameTo(inputFile)) {
            System.err.println("Could not rename temp file to original file name");
        }
    }
        

    private void updateCSV(String csvPath, Staff staffMember) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvPath, true))) {
            writer.write(staffMember.getId() + "," + staffMember.getName() + ",default@example.com");
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to " + csvPath + ": " + e.getMessage());
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
        // Check if the staff member is an Administrator
        User user = userService.getUserById(staffId);
        if (user != null && user.getRole() == UserRole.ADMINISTRATOR) {
            System.out.println("Error: Administrator cannot be removed.");
            return false;
        }
    
        // Proceed with removal if the staff member is not an Administrator
        if (!staffDataStore.getStaffList().containsKey(staffId)) {
            System.out.println("Staff not found for ID: " + staffId);
            return false;
        }
    
        // Determine the role of the staff member to delete from the appropriate CSV
        Staff staffMember = staffDataStore.getStaffList().get(staffId);
        staffDataStore.removeStaff(staffId); // Simply call the method without assignment
        userService.removeUser(staffId); // Remove from UserService as well
    
        boolean removed = false; // Initialize the removed flag
    
        try {
            staffDataStore.writeStaffToCSV(STAFF_CSV_PATH);
    
            // Remove from the specific CSV file based on role
            if (staffMember.getRole().equalsIgnoreCase("Doctor")) {
                removed = removeFromCSV("data/doctor.csv", staffId);
            } else if (staffMember.getRole().equalsIgnoreCase("Pharmacist")) {
                removed = removeFromCSV("data/pharmacist.csv", staffId);
            }
    
            if (removed) {
                System.out.println("Staff member removed: " + staffId);
            } else {
                System.out.println("Failed to remove staff from specific CSV.");
            }
    
        } catch (IOException e) {
            System.err.println("Error saving staff data: " + e.getMessage());
            return false;
        }
    
        return removed;
    }
    

    private boolean removeFromCSV(String csvPath, String staffId) {
        File inputFile = new File(csvPath);
        File tempFile = new File("temp.csv");
    
        boolean found = false;
    
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
    
            String line;
            while ((line = reader.readLine()) != null) {
                // Check if this line contains the ID to remove
                if (line.startsWith(staffId + ",")) {
                    found = true; // Mark that we found and skipped this ID
                    continue; // Skip this line to effectively remove it
                }
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error processing file: " + e.getMessage());
            return false;
        }
    
        // Ensure the temporary file can replace the original file
        if (found) {
            if (inputFile.delete()) {
                if (!tempFile.renameTo(inputFile)) {
                    System.err.println("Could not rename temp file to original file name.");
                    return false;
                }
            } else {
                System.err.println("Could not delete original file.");
                return false;
            }
        } else {
            // Clean up the temporary file if the ID wasn't found
            tempFile.delete();
            System.out.println("Staff ID not found: " + staffId);
        }
    
        return found;
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






