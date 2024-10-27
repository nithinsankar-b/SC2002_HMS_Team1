package controllers;

import views.ManageHospStaff;
import views.ViewAndManageInventory;
import views.AppointmentsView;
import models.Staff;
import services.AppointmentService;
import services.InventoryService;
import services.ProjectAdminService;
import stores.InventoryDataStore;
import interfaces.IAppointmentService;
import models.Administrator;
import java.util.List;
import java.util.Scanner;
import models.Inventory;
import interfaces.IInventoryService;

public class AdministratorController {

    private final ProjectAdminService adminService;
    private final AppointmentService appointmentService;
    private final ManageHospStaff staffView;
    private final AppointmentsView appointmentsView;
    private final ViewAndManageInventory inventoryView;
    private final Scanner scanner = new Scanner(System.in);  // Initialize here directly

    // Constructor to initialize services and views
    public AdministratorController(AppointmentService appointmentService) {
        // Initialize InventoryDataStore and InventoryService
        InventoryDataStore inventoryDataStore = new InventoryDataStore();
        IInventoryService inventoryService = new InventoryService(inventoryDataStore);

        // Initialize ProjectAdminService with Administrator and inventoryService
        this.adminService = new ProjectAdminService(new Administrator("admin01", "password", null), inventoryService);
        this.appointmentService = appointmentService;

        // Pass the initialized inventoryService to ViewAndManageInventory
        this.inventoryView = new ViewAndManageInventory(inventoryService);

        // Initialize other views
        this.staffView = new ManageHospStaff();
        this.appointmentsView = new AppointmentsView(appointmentService);
    }

    // Start the administrator session and present the main menu
    public void start() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            // Display main menu options
            System.out.println("==== Administrator Menu ====");
            System.out.println("1. Manage Hospital Staff");
            System.out.println("2. Manage Inventory");
            System.out.println("3. View Appointments");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");
            
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline character

            // Handle user choice
            switch (choice) {
                case 1:
                    manageHospitalStaff();  // Call staff management section
                    break;
                case 2:
                    manageInventory();  // Call inventory management section
                    break;
                case 3:
                    manageAppointments();  // Call appointments management section
                    break;
                case 4:
                    exit = true;
                    System.out.println("Exiting Administrator session...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Manage hospital staff: show menu with add, remove, and view staff options
    public void manageHospitalStaff() {
        boolean exit = false;
    
        while (!exit) {
            staffView.displayMenu();  // Show the staff menu
            int choice = staffView.getMenuChoice();  // Get the user's choice
    
            switch (choice) {
                case 1: // Add or update staff
                    Staff newStaff = staffView.getStaffDetails();  // Get new staff details from view
                    adminService.addOrUpdateStaff(newStaff);  // Add or update the staff via the service
                    System.out.println("Staff added/updated successfully.");
                    break;
    
                case 2: // Remove staff
                    String staffId = staffView.getStaffIDForRemoval();  // Get the staff ID to remove
                    boolean removed = adminService.removeStaff(staffId);  // Attempt to remove staff
                    if (removed) {
                        System.out.println("Staff removed successfully.");
                    }
                    // No need to print anything else as "Staff not found" is handled in removeStaff
                    break;
    
                case 3: // View all staff
                    List<Staff> staffList = adminService.getAllStaff();  // Get all staff from the service
                    staffView.displayListOfStaff(staffList);  // Display the list of staff
                    break;
    
                case 4: // Return to main menu
                    exit = true;
                    break;
    
                default:
                    System.out.println("Invalid choice, please try again.");
            }
        }
    }

    public void manageInventory() {
        boolean exit = false;
    
        while (!exit) {
            inventoryView.displayMenu();  // Display menu
            int choice = inventoryView.getMenuChoice();  // Get user input
    
            switch (choice) {
                case 1: // Add new medication
                    Inventory newMedicine = inventoryView.getNewMedicationDetails();
                    adminService.addMedication(newMedicine);
                    System.out.println("Medication added successfully.");
                    break;
    
                case 2: // Remove medication
                    String medicineName = inventoryView.getMedicineNameForRemoval();
                    
                    // Remove medication and check if it was successful
                    if (adminService.removeMedication(medicineName)) {
                        System.out.println("Medication removed successfully.");
                    } else {
                        System.out.println("Medicine does not exist.");
                    }
                    break;
    
                case 3: // Update stock
                    medicineName = inventoryView.getMedicineName();
                    
                    // Update stock and check if it was successful
                    int quantity = inventoryView.getMedicineQuantity();
                    if (adminService.updateStockLevel(medicineName, quantity)) {
                        System.out.println("Stock updated.");
                    } else {
                        System.out.println("Medicine does not exist.");
                    }
                    break;
    
                case 4: // Update low stock alert
                    medicineName = inventoryView.getMedicineName();
                    
                    // Update low stock alert and check if it was successful
                    int lowStockAlertLevel = inventoryView.getLowStockAlertLevel();
                    if (adminService.updateLowStockAlert(medicineName, lowStockAlertLevel)) {
                        System.out.println("Low stock alert updated.");
                    } else {
                        System.out.println("Medicine does not exist.");
                    }
                    break;
    
                    case 5: // Approve replenishment request
                    System.out.print("Enter the medicine name for replenishment approval: ");
                    String medicineReplenish = scanner.nextLine();  // Use the entered medicine name
                    
                    // Call the approveReplenishmentRequest method with the specified medicine name
                    boolean success = adminService.approveReplenishmentRequest(medicineReplenish);
                    
                    // Check if the replenishment was approved successfully
                    if (success) {
                        System.out.println("Replenishment request approved and stock updated for: " + medicineReplenish);
                    } else {
                        System.out.println("Replenishment request not found or already processed for: " + medicineReplenish);
                    }
                    break;
    
                case 6: // View inventory
                    List<Inventory> inventoryList = adminService.getAllInventory();
                    inventoryView.displayInventory(inventoryList);
                    break;
    
                case 7:
                    exit = true;
                    break;
    
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
    
    // Manage appointments: view scheduled appointments
    public void manageAppointments() {
        appointmentsView.viewScheduledAppointments();  // Use AppointmentsView to display appointments
    }
}









   



