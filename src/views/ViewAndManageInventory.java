package views;

import models.Appointment;
import src.models.Inventory;
import models.Staff;
import interfaces.IAdministratorView;
import interfaces.IInventoryService;
import enums.ReplenishmentStatus;

import java.util.List;
import java.util.Scanner;

/**
 * The ViewAndManageInventory class implements the IAdministratorView interface
 * and provides methods for viewing and managing the inventory, including adding,
 * removing, updating stock levels, and handling replenishment requests.
 */
public class ViewAndManageInventory implements IAdministratorView {

    private final Scanner scanner = new Scanner(System.in);
    private final IInventoryService inventoryService;

    /**
     * Constructs a ViewAndManageInventory object with the specified IInventoryService.
     *
     * @param inventoryService The service for managing inventory operations.
     */
    public ViewAndManageInventory(IInventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * Displays the Inventory Management menu.
     */
    @Override
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

    /**
     * Retrieves the user's menu choice from the Inventory Management menu.
     *
     * @return The selected menu option as an integer.
     */
    @Override
    public int getMenuChoice() {
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume the newline character
        return choice;
    }

    /**
     * Gathers details for a new medication to be added to the inventory.
     *
     * @return A new Inventory object with the provided details.
     */
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

    /**
     * Adds a new medication to the inventory.
     */
    public void addNewMedication() {
        Inventory newMedication = getNewMedicationDetails();
        inventoryService.addMedication(newMedication);
        System.out.println("Medication added: " + newMedication.getMedicineName());
    }

    /**
     * Removes a medication from the inventory by its name.
     */
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

    /**
     * Updates the stock level of a specific medication.
     */
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

    /**
     * Updates the low stock alert level for a specific medication.
     */
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

    /**
     * Displays the inventory details.
     *
     * @param inventoryList The list of Inventory objects to be displayed.
     */
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

    /**
     * Displays all inventory items.
     */
    public void viewInventory() {
        List<Inventory> inventoryList = inventoryService.getInventoryList();
        displayInventory(inventoryList);
    }

    // Not used in this view, but required by the interface

    /**
     * This method is not applicable for this view as it does not handle staff management.
     *
     * @param staffList The list of Staff objects (not used).
     */
    @Override
    public void displayListOfStaff(List<Staff> staffList) {
        throw new UnsupportedOperationException("Unimplemented method 'displayListOfStaff'");
    }

    /**
     * This method is not applicable for this view as it does not handle appointments.
     *
     * @param appointments The list of Appointment objects (not used).
     */
    @Override
    public void displayAppointments(List<Appointment> appointments) {
        throw new UnsupportedOperationException("Unimplemented method 'displayAppointments'");
    }

    /**
     * Prompts the user to enter a medicine name for removal.
     *
     * @return The medicine name entered by the user.
     */
    public String getMedicineNameForRemoval() {
        System.out.print("Enter Medicine Name to Remove: ");
        return scanner.nextLine();
    }

    /**
     * Prompts the user to enter a medicine name.
     *
     * @return The medicine name entered by the user.
     */
    public String getMedicineName() {
        System.out.print("Enter Medicine Name: ");
        return scanner.nextLine();
    }

    /**
     * Prompts the user to enter a medicine quantity.
     *
     * @return The quantity entered by the user.
     */
    public int getMedicineQuantity() {
        System.out.print("Enter Medicine Quantity: ");
        return scanner.nextInt();
    }

    /**
     * Prompts the user to enter a new low stock alert level for a medication.
     *
     * @return The low stock alert level entered by the user.
     */
    public int getLowStockAlertLevel() {
        System.out.print("Enter the new low stock alert level: ");
        return scanner.nextInt();
    }
}
















