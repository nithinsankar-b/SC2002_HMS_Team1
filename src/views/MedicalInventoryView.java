package views;

import models.InventoryDisplay;
import services.InventoryService;

import java.util.List;

public class MedicalInventoryView {
    private final InventoryService inventoryService; // Service dependency

    public MedicalInventoryView(InventoryService inventoryService) {
        this.inventoryService = inventoryService; // Initialize the service
    }

    public void display(List<InventoryDisplay> inventory) {
        if (inventory.isEmpty()) {
            System.out.println("No medications available in inventory.");
            return;
        }

        System.out.println("Medication Inventory:");
        System.out.printf("%-20s %-15s %-10s %-20s\n", "Name", "Quantity", "Status", "Replenishment Status");
        System.out.println("--------------------------------------------------------------------");

        for (InventoryDisplay medication : inventory) {
            System.out.printf("%-20s %-15d %-10s %-20s\n",
                medication.getName(),
                medication.getQuantity(),
                medication.getStatus(),
                medication.getReplenishmentStatus()); // Display Replenishment Status
        }
    }

    // Method to display an error message
    public void showErrorMessage(String message) {
        System.out.println(message);
    }

    // Method to display a success message
    public void showSuccessMessage(String message) {
        System.out.println(message);
    }

    
}
