package boundary;

import stores.InventoryDataStore;
import models.InventoryData;

import java.util.List;
import java.util.Scanner;

public class ViewMedInventory {

    private final InventoryDataStore inventoryDataStore; // Corrected the variable name

    public ViewMedInventory(InventoryDataStore inventoryDataStore) {
        this.inventoryDataStore = inventoryDataStore;
    }

    /**
     * Displays the medicine inventory to the user and provides options to interact.
     */
    public void displayMedInventory() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Medicine Inventory ===");

            // Fetch the current inventory data
            List<InventoryData> inventoryList = inventoryDataStore.getInventoryList(); // Corrected reference

            if (inventoryList.isEmpty()) {
                System.out.println("No medicines available in the inventory.");
            } else {
                for (InventoryData data : inventoryList) {
                    System.out.println(data);
                }
            }

            System.out.println("\nOptions:");
            System.out.println("1. Submit Replenishment Request");
            System.out.println("2. Check Specific Medicine Stock");
            System.out.println("3. Return to Main Menu");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    submitReplenishmentRequest(scanner);
                    break;
                case "2":
                    checkSpecificMedicineStock(scanner);
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Prompts the user to submit a replenishment request for a specific medicine.
     *
     * @param scanner The Scanner object for user input.
     */
    private void submitReplenishmentRequest(Scanner scanner) {
        System.out.print("Enter the name of the medicine for replenishment: ");
        String medicineName = scanner.nextLine().trim();
        inventoryDataStore.submitReplenishmentRequest(medicineName); // This method needs to be implemented in InventoryDataStore
    }

    /**
     * Prompts the user to check the stock level of a specific medicine.
     *
     * @param scanner The Scanner object for user input.
     */
    private void checkSpecificMedicineStock(Scanner scanner) {
        System.out.print("Enter the name of the medicine to check stock: ");
        String medicineName = scanner.nextLine().trim();

        // Fetch the current inventory data
        List<InventoryData> inventoryList = inventoryDataStore.getInventoryList(); 

        boolean found = false;
        for (InventoryData data : inventoryList) {
            if (data.getMedicineName().equalsIgnoreCase(medicineName)) {
                System.out.println(data);
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Medicine not found in the inventory.");
        }
    }
}
