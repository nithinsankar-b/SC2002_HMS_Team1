package stores;

import models.Inventory;
import enums.MedicationStatus;
import enums.ReplenishmentStatus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The {@code InventoryDataStore} class is responsible for managing the inventory data.
 * It loads inventory information from a CSV file, provides methods to manipulate inventory items,
 * and saves updated inventory data back to a CSV file.
 */
public class InventoryDataStore {

    private List<Inventory> inventoryList; // List to store inventory items

    /**
     * Constructs an {@code InventoryDataStore} and initializes the inventory list by loading data from a CSV file.
     */
    public InventoryDataStore() {
        this.inventoryList = new ArrayList<>();
        loadInventoryFromCSV(); // Load inventory data from CSV on initialization
    }
    
    /**
     * Loads inventory data from a CSV file into the inventory list.
     * Each line in the file should contain the medicine name, current stock, low stock alert,
     * and the replenishment status.
     */
    private void loadInventoryFromCSV() {
        String line = "";
        try (BufferedReader br = new BufferedReader(new FileReader("data/inventory.csv"))) {
            boolean isFirstLine = true; // Flag to skip the header

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip the header line
                    continue; // Move to the next line
                }

                String[] values = line.split(",");
                if (values.length >= 4) {
                    String medicineName = values[0].trim();
                    int currentStock = Integer.parseInt(values[1].trim());
                    int lowLevelAlert = Integer.parseInt(values[2].trim());
                    ReplenishmentStatus replenishmentStatus;

                    try {
                        replenishmentStatus = ReplenishmentStatus.valueOf(values[3].trim().toUpperCase());
                    } catch (IllegalArgumentException e) {
                        System.out.println("Invalid ReplenishmentStatus in line: " + line + ". Defaulting to PENDING.");
                        replenishmentStatus = ReplenishmentStatus.PENDING; // Default value
                    }

                    inventoryList.add(new Inventory(medicineName, currentStock, lowLevelAlert, replenishmentStatus));
                } else {
                    System.out.println("Skipping invalid line: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.out.println("Error parsing number from line: " + line);
        }
    }

    /**
     * Returns the list of inventory items.
     *
     * @return a list of {@code Inventory} objects
     */
    public List<Inventory> getInventoryList() {
        return inventoryList;
    }

    /**
     * Sets the inventory list to a new list of inventory items.
     *
     * @param inventoryList the new list of {@code Inventory} objects
     */
    public void setInventoryList(List<Inventory> inventoryList) {
        this.inventoryList = inventoryList;
    }

    /**
     * Adds a new inventory item to the inventory list.
     *
     * @param inventory the {@code Inventory} item to be added
     */
    public void addInventory(Inventory inventory) {
        inventoryList.add(inventory);
    }

    /**
     * Writes the current inventory data to a specified CSV file.
     *
     * @param inventoryCsvPath the path to the CSV file where inventory data will be saved
     */
    public void writeInventoryToCSV(String inventoryCsvPath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inventoryCsvPath))) {
            // Write header
            writer.write("MedicineName,CurrentStock,LowStockAlert,ReplenishmentStatus");
            writer.newLine();

            // Write each inventory item to the CSV
            for (Inventory item : inventoryList) {
                writer.write(item.getMedicineName() + "," +
                             item.getCurrentStock() + "," +
                             item.getLowLevelAlert() + "," +
                             item.getReplenishmentStatus());
                writer.newLine();
            }

            System.out.println("Inventory data successfully written to " + inventoryCsvPath);

        } catch (IOException e) {
            System.err.println("Error writing inventory data to CSV: " + e.getMessage());
        }
    }

}

