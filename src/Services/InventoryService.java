package services;

import interfaces.IInventoryService;
import models.Inventory;
import models.InventoryDisplay;
import enums.ReplenishmentStatus;
import enums.InventoryStatus;
import stores.InventoryDataStore;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryService implements IInventoryService {
    private final InventoryDataStore inventoryDataStore;
    private static final String CSV_FILE_PATH = "data\\Inventory.csv";
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

    private int calculateReplenishmentAmount(int currentStock, int lowLevelAlert) {
        return (lowLevelAlert * 2) - currentStock; // Example: double the low-level threshold
    }

    // Approve replenishment request
    public boolean approveReplenishmentRequest(String medicineName) {
        List<Inventory> inventoryDataList = inventoryDataStore.getInventoryList();

        for (Inventory item : inventoryDataList) {
            if (item.getMedicineName().equalsIgnoreCase(medicineName) &&
                item.getReplenishmentStatus() == ReplenishmentStatus.PENDING) {

                int replenishedAmount = calculateReplenishmentAmount(item.getCurrentStock(), item.getLowLevelAlert());

                // Update the stock and status
                item.setCurrentStock(item.getCurrentStock() + replenishedAmount);
                item.setReplenishmentStatus(ReplenishmentStatus.REPLENISHED);
                
                saveDataToCSV();
                System.out.println("Replenishment approved and completed for: " + medicineName);
                return true;
            }
        }
        
        System.out.println("Replenishment request not found or already processed for: " + medicineName);
        return false;
    }

    public boolean decreaseMedicineStock(String medicineName, int quantity) {
        List<Inventory> inventoryDataList = inventoryDataStore.getInventoryList();

        for (Inventory data : inventoryDataList) {
            if (data.getMedicineName().equalsIgnoreCase(medicineName)) {
                if (data.getCurrentStock() >= quantity) {
                    data.setCurrentStock(data.getCurrentStock() - quantity);
                    saveDataToCSV();
                    System.out.println("Stock decreased for medicine: " + medicineName + ". New stock: " + data.getCurrentStock());
                    return true;
                } else {
                    System.out.println("Not enough stock for medicine: " + medicineName + ". Current stock: " + data.getCurrentStock());
                    return false;
                }
            }
        }
        System.out.println("Medicine not found: " + medicineName);
        return false;
    }

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

    public List<InventoryDisplay> getInventoryDisplay() {
        List<Inventory> inventoryList = inventoryDataStore.getInventoryList();

        return inventoryList.stream()
                .map(inventory -> new InventoryDisplay(
                        inventory.getMedicineName(),
                        inventory.getCurrentStock(),
                        determineInventoryStatus(inventory)
                ))
                .collect(Collectors.toList());
    }

    private InventoryStatus determineInventoryStatus(Inventory inventory) {
        return inventory.getCurrentStock() <= inventory.getLowLevelAlert()
                ? InventoryStatus.LOWSTOCK
                : InventoryStatus.INSTOCK;
    }

    @Override
    public void updateInventory(Inventory updatedInventory) {
        List<Inventory> inventoryDataList = inventoryDataStore.getInventoryList();

        for (Inventory data : inventoryDataList) {
            if (data.getMedicineName().equalsIgnoreCase(updatedInventory.getMedicineName())) {
                data.setCurrentStock(updatedInventory.getCurrentStock());
                data.setReplenishmentStatus(updatedInventory.getReplenishmentStatus());
                saveDataToCSV();
                System.out.println("Inventory updated for medicine: " + data.getMedicineName());
                break;
            }
        }
    }

    @Override
    public List<Inventory> getInventoryList() {
        return inventoryDataStore.getInventoryList();
    }

    @Override
    public void addMedication(Inventory newMedication) {
        List<Inventory> inventoryList = inventoryDataStore.getInventoryList();
        inventoryList.add(newMedication);
        saveDataToCSV();
        System.out.println("Medication added: " + newMedication.getMedicineName());
    }

    @Override
    public boolean removeMedication(String medicineName) {
        List<Inventory> inventoryList = inventoryDataStore.getInventoryList();
        boolean removed = inventoryList.removeIf(item -> item.getMedicineName().equalsIgnoreCase(medicineName));
        if (removed) {
            saveDataToCSV();
            System.out.println("Medication removed: " + medicineName);
        } else {
            System.out.println("Medication not found: " + medicineName);
        }
        return removed;
    }

    @Override
    public boolean updateStockLevel(String medicineName, int quantity) {
        List<Inventory> inventoryList = inventoryDataStore.getInventoryList();
        for (Inventory item : inventoryList) {
            if (item.getMedicineName().equalsIgnoreCase(medicineName)) {
                item.setCurrentStock(quantity);
                saveDataToCSV();
                System.out.println("Stock level updated for: " + medicineName);
                return true;
            }
        }
        System.out.println("Medication not found: " + medicineName);
        return false;
    }

    @Override
    public boolean updateLowStockAlert(String medicineName, int lowStockLevel) {
        List<Inventory> inventoryList = inventoryDataStore.getInventoryList();
        for (Inventory item : inventoryList) {
            if (item.getMedicineName().equalsIgnoreCase(medicineName)) {
                item.setLowLevelAlert(lowStockLevel);
                saveDataToCSV();
                System.out.println("Low stock alert updated for: " + medicineName);
                return true;
            }
        }
        System.out.println("Medication not found: " + medicineName);
        return false;
    }
}


