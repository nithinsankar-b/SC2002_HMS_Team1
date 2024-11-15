package services;

import interfaces.IInventoryService;
import models.Appointment;
import models.Inventory;
import models.InventoryDisplay;
import enums.ReplenishmentStatus;
import enums.InventoryStatus;
import enums.MedicationStatus;
import stores.InventoryDataStore;
import services.AppointmentService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.Scanner;
/**
 * The InventoryService class provides methods for managing medication inventory,
 * including loading and saving inventory data from/to a CSV file, submitting
 * replenishment requests, updating stock levels, and viewing current inventory.
 */
public class InventoryService implements IInventoryService {

    private final InventoryDataStore inventoryDataStore;
    private static final String CSV_FILE_PATH = "data/inventory.csv";
    private static final String DELIMITER = ",";
    private final AppointmentService appointmentService;
    
   

    /**
     * Constructs an InventoryService instance with the specified InventoryDataStore.
     * Initializes the inventory data by loading it from a CSV file.
     *
     * @param store the InventoryDataStore to be used by this service
     */
    public InventoryService(InventoryDataStore store) {
        this.inventoryDataStore = store;
        this.appointmentService = new AppointmentService();
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
                    : ReplenishmentStatus.APPROVED.toString();
                String line = String.join(DELIMITER,
                        item.getMedicineName(),
                        String.valueOf(item.getCurrentStock()),
                        String.valueOf(item.getLowLevelAlert()),
                        replenishmentStatus);
                bw.write(line);
                bw.newLine();
            }
            
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

        // Create a Scanner object to accept user input for medicine names
        Scanner scanner = new Scanner(System.in);

        // Ask for medicine names input, assuming they are comma-separated
        System.out.println("The Low Stock Medications are:-");
        for (Inventory data : inventoryDataList){
            if(data.getCurrentStock()<=data.getLowLevelAlert()) {
                System.out.println(data.getMedicineName() + " - " + data.getCurrentStock());
            }
        }

            System.out.println("Enter the list of medicines (comma-separated) to check and submit replenishment requests:");
        String input = scanner.nextLine();

        // Split the input string into individual medicine names
        String[] requestedMedicines = input.split(",");

        // Iterate through the requested medicines
        for (String medicineName : requestedMedicines) {
            medicineName = medicineName.trim(); // Remove any extra spaces
            
            // Search for the medicine in the inventory list
            boolean found = false;
            for (Inventory data : inventoryDataList) {
                if (data.getMedicineName().equalsIgnoreCase(medicineName)) {
                    found = true;

                    // Check if the current stock is less than or equal to the low-level alert
                    if (data.getCurrentStock() <= data.getLowLevelAlert()) {
                        // Set the replenishment status to PENDING
                        data.setReplenishmentStatus(ReplenishmentStatus.PENDING);
                        System.out.println("Replenishment request submitted for: " + data.getMedicineName());
                    } else {
                        // Print if stock is sufficient
                        System.out.println("Stock level is sufficient for: " + data.getMedicineName());
                    }
                    break; // Stop searching once the medicine is found
                }
            }

            if (!found) {
                // Print if the medicine is not found in the inventory list
                System.out.println("Medicine not found in the inventory: " + medicineName);
            }
        }

        // Save the updated data to CSV after processing all requested medicines
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
    /*public void updateStock(String medicineName, int quantity, String appointmentID) {
        List<Inventory> inventoryDataList = inventoryDataStore.getInventoryList();
        Appointment appointment = appointmentService.getAppointmentById(appointmentID); 

        for (Inventory data : inventoryDataList) {
            if (data.getMedicineName().equalsIgnoreCase(medicineName) && appointment.getMedicationStatus() == MedicationStatus.PENDING) {
            	
              if(data.getCurrentStock() - quantity < 0)
              {
                System.out.println("Not enough medicine in the inventory");
                return;
              }
              else {
                data.setCurrentStock(data.getCurrentStock() - quantity);
                appointment.setMedicationStatus(MedicationStatus.DISPENSED);
                System.out.println("Updated stock for medicine: " + medicineName + ". New stock: " + data.getCurrentStock());

                // Check if stock falls below the low-level alert and update status
                if (data.getCurrentStock() <= data.getLowLevelAlert()) {
                    data.setInventoryStatus(InventoryStatus.LOWSTOCK);
                    System.out.println("Stock for " + medicineName + " is now LOWSTOCK.");
                }
                saveDataToCSV();
                appointmentService.saveAppointmentsToCSV();
                appointmentService.loadAppointmentsFromCSV();
                loadDataFromCSV();// Save updated data to CSV
                return;
            }
            }
            else {
            	if(!data.getMedicineName().equalsIgnoreCase(medicineName)) {
            		System.out.println("Medication does not exist.");
            	}
            	else {
            		System.out.println("Medication already dispensed.");
            	}
            	
            }
        }
        System.out.println("Medicine not found: " + medicineName);
    }*/
    public void updateStock(String medicineName, int quantity, String appointmentID) {
        List<Inventory> inventoryDataList = inventoryDataStore.getInventoryList();
        Appointment appointment = appointmentService.getAppointmentById(appointmentID);
        boolean medicineFound = false;

        // Loop through inventory to find the specified medicine
        for (Inventory data : inventoryDataList) {
            if (data.getMedicineName().equalsIgnoreCase(medicineName)) {
                medicineFound = true;

                // Check if the medication has already been dispensed for this appointment
                if (appointment.getMedicationStatus() == MedicationStatus.DISPENSED) {
                    System.out.println("Medication " + data.getMedicineName()+ " already dispensed for appointment ID: " + appointmentID);
                    return;
                }

                // Check for sufficient stock
                if (data.getCurrentStock() < quantity) {
                    System.out.println("Not enough medicine in the inventory for: " + medicineName);
                    return;
                }
                System.out.println(quantity);
                // Update stock and set medication status to DISPENSED
                data.setCurrentStock(data.getCurrentStock() - quantity);
                appointment.setMedicationStatus(MedicationStatus.DISPENSED);
                System.out.println("Updated stock for medicine: " + medicineName + ". New stock: " + data.getCurrentStock());

                // Check if stock falls below the low-level alert and update status
                if (data.getCurrentStock() <= data.getLowLevelAlert()) {
                    data.setInventoryStatus(InventoryStatus.LOWSTOCK);
                    System.out.println("Stock for " + medicineName + " is now LOWSTOCK.");
                }

                // Save updates to CSV files
                saveDataToCSV();
                appointmentService.saveAppointmentsToCSV();
                return;
            }
        }

        // If no matching medicine is found
        if (!medicineFound) {
            System.out.println("Medicine not found: " + medicineName);
        }
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
                item.setReplenishmentStatus(ReplenishmentStatus.APPROVED);
                
                saveDataToCSV();
                System.out.println("Replenishment approved and completed for: " + medicineName);
                return true;
            }
        }
        
        System.out.println("Replenishment request not found or already processed for: " + medicineName);
        return false;
    }
    
    public List<InventoryDisplay> getLowStockInventory() {
        List<InventoryDisplay> lowStockInventory = new ArrayList<>();

        // Get low stock items from the InventoryDataStore
        for (Inventory item : inventoryDataStore.getLowStockInventory()) {
            // Determine inventory status (Low or Sufficient)
            InventoryStatus status = InventoryDisplay.getInventoryStatus(item.getCurrentStock(), item.getLowLevelAlert());

            InventoryDisplay displayItem = new InventoryDisplay(
                    item.getMedicineName(),
                    item.getCurrentStock(),
                    status, // Set the inventory status (Low or Sufficient)
                    item.getReplenishmentStatus()
            );
            lowStockInventory.add(displayItem);
        }

        return lowStockInventory;
    }

}
