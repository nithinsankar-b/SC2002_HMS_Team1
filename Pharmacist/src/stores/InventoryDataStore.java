package stores;

import models.Inventory;
import enums.ReplenishmentStatus;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InventoryDataStore {

    private List<Inventory> inventoryList;

    public InventoryDataStore() {
        this.inventoryList = new ArrayList<>();
        loadInventoryFromCSV();
    }
    
    private void loadInventoryFromCSV() {
        String line="";
        try (BufferedReader br = new BufferedReader(new FileReader("C:\\Users\\HP\\OneDrive\\Documents\\inventory.csv"))) {
            boolean isFirstLine = true; // Flag to skip the header

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip the header line
                    continue; // Move to the next line
                }

                String[] values = line.split(",");
                // Ensure values have the correct length
                if (values.length >= 4) {
                    String medicineName = values[0].trim(); // Trim whitespace
                    int currentStock = Integer.parseInt(values[1].trim()); // Trim whitespace
                    int lowLevelAlert = Integer.parseInt(values[2].trim()); // Trim whitespace
                    ReplenishmentStatus replenishmentStatus;
                    try {
                        replenishmentStatus = ReplenishmentStatus.REPLENISHED;
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

    // Initialize inventory list
    public List<Inventory> getInventoryList() {
        return inventoryList;
    }

    public void setInventoryList(List<Inventory> inventoryList) {
        this.inventoryList = inventoryList;
    }

    // Add individual inventory item
    public void addInventory(Inventory inventory) {
        inventoryList.add(inventory);
    }
}
