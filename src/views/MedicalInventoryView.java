package views;

import models.InventoryDisplay;
import services.InventoryService;

import java.util.List;

/**
 * The {@code MedicalInventoryView} class provides a user interface for displaying the medical inventory.
 * It interacts with the {@code InventoryService} to fetch inventory details and present them to the user.
 */
public class MedicalInventoryView {
    private final InventoryService inventoryService; // Service dependency

    /**
     * Constructs a {@code MedicalInventoryView} with the specified inventory service.
     *
     * @param inventoryService the service used to manage inventory operations
     */
    public MedicalInventoryView(InventoryService inventoryService) {
        this.inventoryService = inventoryService; // Initialize the service
    }

    /**
     * Displays the list of medications in the inventory.
     *
     * @param inventory the list of {@code InventoryDisplay} objects representing the medications
     */
    public void display(List<InventoryDisplay> inventory) {
        if (inventory.isEmpty()) {
            System.out.println("No medications available in inventory.");
            return;
        }

        System.out.println("Medication Inventory:");
        System.out.printf("%-20s %-15s %-10s \n", "Name", "Quantity", "Status");
        System.out.println("--------------------------------------------------------------------");

        for (InventoryDisplay medication : inventory) {
            System.out.printf("%-20s %-15d %-10s \n",
                medication.getName(),
                medication.getQuantity(),
                medication.getStatus()
                ); 
        }
    }

    /**
     * Displays an error message to the user.
     *
     * @param message the error message to be displayed
     */
    public void showErrorMessage(String message) {
        System.out.println(message);
    }

    /**
     * Displays a success message to the user.
     *
     * @param message the success message to be displayed
     */
    public void showSuccessMessage(String message) {
        System.out.println(message);
    }
}
