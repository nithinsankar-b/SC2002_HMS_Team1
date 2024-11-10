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
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;

/**
 * The InventoryService class provides methods for managing medication inventory,
 * including loading and saving inventory data from/to a CSV file, submitting
 * replenishment requests, updating stock levels, and viewing current inventory.
 */
public class InventoryService implements IInventoryService {

    private final InventoryDataStore inventoryDataStore;
    private static final String CSV_FILE_PATH = "data/inventory.csv";
    private static final String DELIMITER = ",";

    /**
     * Constructs an InventoryService instance with the specified InventoryDataStore.
     * Initializes the inventory data by loading it from a CSV file.
     *
     * @param store the InventoryDataStore to be used by this service
     */
    public InventoryService(InventoryDataStore store) {
        this.inventoryDataStore = store;
        loadDataFromCSV(); // Load inventory data from CSV on initialization
    }

    /**
     * Saves the current inventory data to a CSV file.
     * The file will include headers for medicine name, current stock,
     * low level alert, and replenishment status.
     */
    public void saveDataToCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            bw.write("Medicine Name,Current Stock,Low Level Alert,Replenishment Status");
            bw.newLine();
            for (Inventory item : inventoryDataStore.getInventoryList()) {
                String replenishmentStatus = item.getReplenishmentStatus() != null 
                    ? item.getReplenishmentStatus().toString() 
                    : ReplenishmentStatus.REPLENISHED.toString();
                String line = String.join(DELIMITER,
                        item.getMedicineName(),
                        String.valueOf(item.getCurrentStock()),
                        String.valueOf(item.getLowLevelAlert()),
                        replenishmentStatus);
                bw.write(line);
                bw.newLine();
            }
            System.out.println("Inventory data saved to CSV successfully.");
        } catch (IOException e) {
            System.out.println("Error saving the inventory CSV file: " + e.getMessage());
        }
    }

    /**
     * Loads inventory data from a CSV file and populates the InventoryDataStore.
     * The CSV file is expected to contain headers followed by rows of inventory data.
     */
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

    /**
     * Submits a replenishment request for the specified medicine.
     * If the current stock of the medicine is below or equal to the low level alert,
     * the replenishment status is set to PENDING and the data is saved to CSV.
     *
     *  the name of the medicine to submit a replenishment request for
     */
    @Override
    public void submitReplenishmentRequest() {
        List<Inventory> inventoryDataList = inventoryDataStore.getInventoryList();

        // Iterate through the inventory data list
        for (Inventory data : inventoryDataList) {
            // Check if the current stock is less than or equal to the low-level alert
            if (data.getCurrentStock() <= data.getLowLevelAlert()) {
                // Set the replenishment status to PENDING
                data.setReplenishmentStatus(ReplenishmentStatus.PENDING);
                System.out.println("Replenishment request submitted for: " + data.getMedicineName());
            } else {
                // Print if stock is sufficient
                System.out.println("Stock level is sufficient for: " + data.getMedicineName());
            }
        }

        // Save the updated data to CSV after processing all inventory items
        saveDataToCSV();
    }

    /**
     * Calculates the amount to replenish based on the current stock and the low-level alert.
     *
     * @param currentStock the current stock level of the medicine
     * @param lowLevelAlert the low level alert threshold
     * @return the amount to replenish
     */
    private int calculateReplenishmentAmount(int currentStock, int lowLevelAlert) {
        return (lowLevelAlert * 2) - currentStock; // Example: Double the low-level threshold
    }

    /**
     * Updates the stock for the specified medicine by reducing the current stock by the given quantity.
     * If the stock falls below the low-level alert, the inventory status is updated to LOWSTOCK.
     *
     * @param medicineName the name of the medicine to update stock for
     * @param quantity the quantity to deduct from the current stock
     */
    public void updateStock(String medicineName, int quantity) {
        List<Inventory> inventoryDataList = inventoryDataStore.getInventoryList();

        for (Inventory data : inventoryDataList) {
            if (data.getMedicineName().equalsIgnoreCase(medicineName)) {
                data.setCurrentStock(data.getCurrentStock() - quantity);
                System.out.println("Updated stock for medicine: " + medicineName + ". New stock: " + data.getCurrentStock());

                if (data.getCurrentStock() <= data.getLowLevelAlert()) {
                    data.setInventoryStatus(InventoryStatus.LOWSTOCK);
                    System.out.println("Stock for " + medicineName + " is now LOWSTOCK.");
                }
                saveDataToCSV();
                return;
            }
        }
        System.out.println("Medicine not found: " + medicineName);
    }

    /**
     * Displays the current medication inventory, listing each medicine's name, current stock,
     * low-level alert, and replenishment status. If no medications are in inventory, a message is shown.
     */
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

    /**
     * Fetches a list of InventoryDisplay objects representing the current medication inventory.
     * Each InventoryDisplay includes the medicine name, current stock, inventory status, and replenishment status.
     *
     * @return a list of InventoryDisplay objects
     */
    public List<InventoryDisplay> getInventoryDisplay() {
        List<Inventory> inventoryList = inventoryDataStore.getInventoryList();

        return inventoryList.stream()
                .map(inventory -> new InventoryDisplay(
                        inventory.getMedicineName(),
                        inventory.getCurrentStock(),
                        determineInventoryStatus(inventory),
                        inventory.getReplenishmentStatus()
                ))
                .collect(Collectors.toList());
    }

    /**
     * Determines the InventoryStatus based on the current stock level.
     * If the current stock is less than or equal to the low-level alert, the status is LOWSTOCK;
     * otherwise, it is INSTOCK.
     *
     * @param inventory the Inventory object to check
     * @return the InventoryStatus for the given inventory
     */
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

    /**
     * Approves a replenishment request for a specified medication.
     *
     * @param medicineName The name of the medication.
     * @return True if the request was approved and stock updated, false otherwise.
     */
    public boolean approveReplenishmentRequest(String medicineName) {
        List<Inventory> inventoryDataList = inventoryDataStore.getInventoryList();

        for (Inventory item : inventoryDataList) {
            if (item.getMedicineName().equalsIgnoreCase(medicineName) &&
                item.getReplenishmentStatus() == ReplenishmentStatus.PENDING) {

                int replenishedAmount = calculateReplenishmentAmount(item.getCurrentStock(), item.getLowLevelAlert());

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
}
