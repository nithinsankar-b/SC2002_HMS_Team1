package controllers;

import models.Appointment;
import models.Inventory;
import models.InventoryDisplay;
import models.ReplenishmentRequest;
import services.PharmacistService;
import services.UserService;
import stores.InventoryDataStore;
import services.InventoryService;
import services.ReplenishmentService;
import services.AppointmentService;
import views.AppointmentOutcomeRecordView;
import views.MedicalInventoryView;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import enums.MedicationStatus;

/**
 * Controller class for handling pharmacist-related operations.
 * This class coordinates between services and views for the pharmacist to manage 
 * medication inventory, view appointment outcomes, update prescription statuses, 
 * and handle user account management functions such as password changes.
 */
public class PharmacistController {

    private final PharmacistService pharmacistService;
    private final InventoryService inventoryService;
    private final AppointmentService appointmentService;
    private final ReplenishmentService replenishmentService;
    private final MedicalInventoryView medicalInventoryView;
    private final UserService userService;
    private final AppointmentOutcomeRecordView appointmentOutcomeRecordView;
    private final String csvFile = "data/replenishment_requests.csv";

    /**
     * Constructor for PharmacistController.
     *
     * @param pharmacistService    Service for managing pharmacist-specific operations.
     * @param inventoryService     Service for managing inventory.
     * @param appointmentService   Service for handling appointment-related tasks.
     * @param replenishmentService Service for handling replenishment requests.
     */
    public PharmacistController(PharmacistService pharmacistService, 
                                InventoryService inventoryService, 
                                AppointmentService appointmentService,
                                ReplenishmentService replenishmentService) {
        this.pharmacistService = pharmacistService;
        this.inventoryService = inventoryService;
        this.appointmentService = appointmentService;
        this.replenishmentService = replenishmentService;

        // Initialize only necessary views
        this.userService = new UserService();
        this.medicalInventoryView = new MedicalInventoryView(inventoryService);
        this.appointmentOutcomeRecordView = new AppointmentOutcomeRecordView();
    }

    /**
     * Displays the current medication inventory.
     */
    public void viewMedicationInventory() {
        List<InventoryDisplay> inventory = inventoryService.getInventoryDisplay();
        medicalInventoryView.display(inventory);
    }

    /**
     * Submits a replenishment request for low-stock medications.
     * Prompts the user to enter medicine names to request replenishment.
     */
    public void submitReplenishmentRequest() {
        List<InventoryDisplay> lowStockInventory = inventoryService.getLowStockInventory();
        if (lowStockInventory.isEmpty()) {
            System.out.println("No low-stock medications in the inventory.");
            return;
        } else {
            System.out.println("Low-Stock Medications:");
            medicalInventoryView.display(lowStockInventory);
        }

        Scanner scanner = new Scanner(System.in);
        List<String> medicines = new ArrayList<>();
        InventoryDataStore inventoryDataStore = new InventoryDataStore();

        // Load all medicines from the inventory
        List<Inventory> inventoryList = inventoryDataStore.getInventoryList();
        List<String> validMedicines = new ArrayList<>();
        for (Inventory inventory : inventoryList) {
            validMedicines.add(inventory.getMedicineName());
        }

        System.out.println("Enter medicine names for replenishment (type 'done' to finish):");
        while (true) {
            String medicineName = scanner.nextLine().trim();
            if ("done".equalsIgnoreCase(medicineName)) {
                break;
            }
            // Validate if the entered medicine name is valid
            if (validMedicines.contains(medicineName)) {
                medicines.add(medicineName); // Add valid medicine to the list
            } else {
                System.out.println("Invalid medicine name: " + medicineName + ". Please try again.");
            }
        }

        if (!medicines.isEmpty()) {
            replenishmentService.createReplenishmentRequest(medicines);
            System.out.println("Replenishment request submitted successfully.");
        } else {
            System.out.println("No valid medicines were entered for replenishment.");
        }
    }

    /**
     * Displays all replenishment requests, including their statuses.
     */
    public void viewReplenishmentRequests() {
        List<ReplenishmentRequest> requests = replenishmentService.getAllRequests();

        if (requests.isEmpty()) {
            System.out.println("No replenishment requests found.\n");
        } else {
            // Print table header with appropriate formatting
            System.out.printf("%-20s %-20s %20s\n", "Request ID", "Medicine Name", "Replenishment Status");
            System.out.println("---------------------------------------------------------------------------------------------");

            // Open the CSV file for reading
            try (BufferedReader reader = Files.newBufferedReader(Paths.get(csvFile))) {
                String line;
                // Skip the header row
                reader.readLine();
                
                // Read and print each subsequent row
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    if (parts.length >= 3) {
                        String id = parts[0].trim();  // Trim to avoid leading/trailing spaces
                        String medicines = parts[1].trim();
                        String status = parts[2].trim();
                        
                        // Print the request in a nicely formatted row
                        System.out.printf("%-20s %-40s %s\n", 
                                id, 
                                medicines, 
                                status
                                );
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Updates the prescription status for a given appointment.
     *
     * @param appointmentId The ID of the appointment to update.
     */
    public void updatePrescription(String appointmentId) {
        boolean isUpdated = pharmacistService.updatePrescriptionStatus(appointmentId);
        if (isUpdated) {
            System.out.println("Prescription status updated successfully.");
        } else {
            System.err.println("Failed to update prescription status. Please check the appointment ID.");
        }
    }

    /**
     * Displays the outcome records of appointments.
     */
    public void viewAppointmentOutcomeRecords() {
        appointmentOutcomeRecordView.loadAndPrintAppointments();
    }

    /**
     * Changes the password for the pharmacist.
     *
     * @param hospitalID  The hospital ID of the pharmacist.
     * @param oldPassword The old password of the pharmacist.
     * @param newPassword The new password to be set.
     */
    public void changePassword(String hospitalID, String oldPassword, String newPassword) {
        userService.changePassword(hospitalID, oldPassword, newPassword);
    }
}

