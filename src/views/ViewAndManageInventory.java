package views;

import models.Appointment;
import models.Inventory;
import models.Staff;
import models.SubmitReplenishmentRequest;
import interfaces.IAdministratorView;
import interfaces.IInventoryService;
import enums.ReplenishmentStatus;

import java.util.List;
import java.util.Scanner;

public class ViewAndManageInventory implements IAdministratorView {
    private final Scanner scanner = new Scanner(System.in);
    private final IInventoryService inventoryService;

    public ViewAndManageInventory(IInventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    // Method to display the menu and get user input
    public void displayMenu() {
        System.out.println("\n-- Inventory Management --");
        System.out.println("1. Add New Medication");
        System.out.println("2. Remove Medication");
        System.out.println("3. Update Stock");
        System.out.println("4. Update Low Stock Alert");
        System.out.println("5. Approve Replenishment Request");
        System.out.println("6. View Inventory");
        System.out.println("7. Return to Main Menu");
        System.out.print("Choose an option: ");
    }

    // Method to get the user's menu choice
    public int getMenuChoice() {
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume the newline character
        return choice;
    }

// Method to get the details of a new medication
public Inventory getNewMedicationDetails() {
    System.out.print("Enter Medicine Name: ");
    String name = scanner.nextLine();
    
    System.out.print("Enter Initial Stock Level: ");
    int stock = scanner.nextInt();
    
    System.out.print("Enter Low Stock Alert Level: ");
    int lowStockAlert = scanner.nextInt();
    
    scanner.nextLine(); // Consume the newline left-over from nextInt()

    // Loop until a valid replenishment status is entered or default to PENDING
    ReplenishmentStatus replenishmentStatus = null;
    while (replenishmentStatus == null) {
        System.out.print("Enter Replenishment Status (e.g., REPLENISHED, PENDING): ");
        String replenishmentStatusStr = scanner.nextLine().toUpperCase();

        try {
            replenishmentStatus = ReplenishmentStatus.valueOf(replenishmentStatusStr);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid Replenishment Status. Defaulting to PENDING.");
            replenishmentStatus = ReplenishmentStatus.PENDING; // Default value if input is invalid
        }
    }

    return new Inventory(name, stock, lowStockAlert, replenishmentStatus);
}


    // Method to add new medication
    public void addNewMedication() {
        Inventory newMedication = getNewMedicationDetails();
        inventoryService.addMedication(newMedication);
        System.out.println("Medication added: " + newMedication.getMedicineName());
    }

    // Method to remove a medication by name
    public void removeMedication() {
        System.out.print("Enter Medicine Name to Remove: ");
        String medicineName = scanner.nextLine();
        boolean success = inventoryService.removeMedication(medicineName);
        if (success) {
            System.out.println("Medication removed successfully.");
        } else {
            System.out.println("Medication not found.");
        }
    }

    // Method to update the stock level of a medication
    public void updateStockLevel() {
        System.out.print("Enter Medicine Name: ");
        String medicineName = scanner.nextLine();
        System.out.print("Enter New Stock Quantity: ");
        int quantity = scanner.nextInt();
        boolean success = inventoryService.updateStockLevel(medicineName, quantity);
        if (success) {
            System.out.println("Stock level updated successfully.");
        } else {
            System.out.println("Medication not found.");
        }
    }

    // Method to update the low stock alert level
    public void updateLowStockAlert() {
        System.out.print("Enter Medicine Name: ");
        String medicineName = scanner.nextLine();
        System.out.print("Enter New Low Stock Alert Level: ");
        int lowStockAlert = scanner.nextInt();
        boolean success = inventoryService.updateLowStockAlert(medicineName, lowStockAlert);
        if (success) {
            System.out.println("Low stock alert level updated successfully.");
        } else {
            System.out.println("Medication not found.");
        }
    }



    // Method to display inventory details
    @Override
    public void displayInventory(List<Inventory> inventoryList) {
        System.out.println("=== Inventory ===");
        if (inventoryList.isEmpty()) {
            System.out.println("No inventory available.");
        } else {
            for (Inventory inventory : inventoryList) {
                System.out.println("Medicine: " + inventory.getMedicineName() +
                                   ", Stock: " + inventory.getCurrentStock() +
                                   ", Low Stock Alert: " + inventory.getLowLevelAlert() +
                                   ", Replenishment Status: " + inventory.getReplenishmentStatus());
            }
        }
    }

    // Not used in this view, but required by the interface

    // Method to display all inventory items
    public void viewInventory() {
        List<Inventory> inventoryList = inventoryService.getInventoryList();
        displayInventory(inventoryList);
    }

    @Override
    public void displayListOfStaff(List<Staff> staffList) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'displayListOfStaff'");
    }

    @Override
    public void displayAppointments(List<Appointment> appointments) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'displayAppointments'");
    }
        // Get the name of the medicine to remove
        public String getMedicineNameForRemoval() {
            System.out.print("Enter Medicine Name to Remove: ");
            return scanner.nextLine();
        }
        public String getMedicineName() {
            System.out.print("Enter Medicine Name: ");
            return scanner.nextLine();
        }
        public int getMedicineQuantity() {
            System.out.print("Enter Medicine Quantity: ");
            return scanner.nextInt();
        }
        public int getLowStockAlertLevel() {
            System.out.print("Enter the new low stock alert level: ");
            return scanner.nextInt();
        }
}















