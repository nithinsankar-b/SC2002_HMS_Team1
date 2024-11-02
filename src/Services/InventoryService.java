package services;

import interfaces.IInventoryService;
import models.Inventory;
import models.InventoryDisplay;
import enums.ReplenishmentStatus;
import enums.InventoryStatus;
import enums.MedicationStatus;
import stores.InventoryDataStore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

public class InventoryService implements IInventoryService {
    private final InventoryDataStore inventoryDataStore;
    private static final String CSV_FILE_PATH = "data/inventory.csv";
    private static final String DELIMITER = ",";

    public InventoryService(InventoryDataStore store) {
        this.inventoryDataStore = store;
        loadDataFromCSV(); // Load inventory data from CSV on initialization
    }

    // Save inventory data to CSV
    public void saveDataToCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            bw.write("Medicine Name,Current Stock,Low Level Alert,Replenishment Status");
            bw.newLine();
            for (Inventory item : inventoryDataStore.getInventoryList()) {
                String line = String.join(DELIMITER,
                        item.getMedicineName(),
                        String.valueOf(item.getCurrentStock()),
                        String.valueOf(item.getLowLevelAlert()),
                        item.getReplenishmentStatus().toString());
                bw.write(line);
                bw.newLine();
            }
            System.out.println("Inventory data saved to CSV successfully.");
        } catch (IOException e) {
            System.out.println("Error saving the inventory CSV file: " + e.getMessage());
        }
    }

    // Load inventory data from CSV
    public void loadDataFromCSV() {
        String line;
        List<Inventory> inventoryList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            br.readLine(); // Skip the header line
            while ((line = br.readLine()) != null) {
                String[] inventoryData = line.split(DELIMITER);

                if (inventoryData.length == 4) {
                    String medicineName = inventoryData[0].trim();
                    int currentStock = Integer.parseInt(inventoryData[1].trim());
                    int lowLevelAlert = Integer.parseInt(inventoryData[2].trim());
                    ReplenishmentStatus replenishmentStatus = ReplenishmentStatus.valueOf(inventoryData[3].trim());

                    Inventory inventoryItem = new Inventory(medicineName, currentStock, lowLevelAlert, replenishmentStatus);
                    inventoryList.add(inventoryItem);
                }
            }
            inventoryDataStore.setInventoryList(inventoryList);
            System.out.println("Inventory data loaded from CSV successfully.");
        } catch (IOException | IllegalArgumentException e) {
            System.out.println("Error reading inventory CSV file: " + e.getMessage());
        }
    }
    @Override
    public void submitReplenishmentRequest(String medicineName) {
        List<Inventory> inventoryDataList = inventoryDataStore.getInventoryList();

        for (Inventory data : inventoryDataList) {
            if (data.getMedicineName().equalsIgnoreCase(medicineName)) {
                if (data.getCurrentStock() <= data.getLowLevelAlert()) {
                    // Set the ReplenishmentStatus to PENDING
                    data.setReplenishmentStatus(ReplenishmentStatus.PENDING);
                    saveDataToCSV();
                    System.out.println("Replenishment request submitted for: " + medicineName);
                } else {
                    System.out.println("Stock level is sufficient for: " + medicineName);
                }
                break;
            }
        }
    }

    // Calculate how much to replenish based on the current stock and low-level alert
    private int calculateReplenishmentAmount(int currentStock, int lowLevelAlert) {
        return (lowLevelAlert * 2) - currentStock; // Example: Double the low-level threshold
    }

    // Method to update stock for a medication
    public void updateStock(String medicineName, int quantity) {
        List<Inventory> inventoryDataList = inventoryDataStore.getInventoryList();

        for (Inventory data : inventoryDataList) {
            if (data.getMedicineName().equalsIgnoreCase(medicineName)) {
                data.setCurrentStock(data.getCurrentStock() - quantity);
                System.out.println("Updated stock for medicine: " + medicineName + ". New stock: " + data.getCurrentStock());

                // Check if stock falls below the low-level alert and update status
                if (data.getCurrentStock() <= data.getLowLevelAlert()) {
                    data.setInventoryStatus(InventoryStatus.LOWSTOCK);
                    System.out.println("Stock for " + medicineName + " is now LOWSTOCK.");
                }
                saveDataToCSV(); // Save updated data to CSV
                return;
            }
        }
        System.out.println("Medicine not found: " + medicineName);
    }


    // Method to view current medication inventory
    public void viewMedicationInventory() {
        List<Inventory> inventoryDataList = inventoryDataStore.getInventoryList();

        if (inventoryDataList.isEmpty()) {
            System.out.println("No medications in inventory.");
            return;
        }

        System.out.println("Current Medication Inventory:");
        for (Inventory item : inventoryDataList) {
            System.out.println("Medicine Name: " + item.getMedicineName() +
                    " | Current Stock: " + item.getCurrentStock() +
                    " | Low Level Alert: " + item.getLowLevelAlert() +
                    " | Replenishment Status: " + item.getReplenishmentStatus());
        }
    }

    // Method to fetch inventory display list
    public List<InventoryDisplay> getInventoryDisplay() {
        List<Inventory> inventoryList = inventoryDataStore.getInventoryList();

        return inventoryList.stream()
                .map(inventory -> new InventoryDisplay(
                        inventory.getMedicineName(),
                        inventory.getCurrentStock(),
                        determineInventoryStatus(inventory),
                        inventory.getReplenishmentStatus() // Include ReplenishmentStatus
                ))
                .collect(Collectors.toList());
    }

    // Determine InventoryStatus based on stock levels
    private InventoryStatus determineInventoryStatus(Inventory inventory) {
        return inventory.getCurrentStock() <= inventory.getLowLevelAlert()
                ? InventoryStatus.LOWSTOCK
                : InventoryStatus.INSTOCK;
    }
}