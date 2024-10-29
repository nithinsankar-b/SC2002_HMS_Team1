package controllers;
import views.AdminView;
import services.AppointmentService;
import services.InventoryService;
import services.ProjectAdminService;
import stores.InventoryDataStore;
import interfaces.IInventoryService;
import models.Administrator;
import models.Appointment;
import models.Inventory;
import models.Staff;
import java.util.List;

public class AdministratorController {

    private final ProjectAdminService adminService;
    private final AppointmentService appointmentService;
    private final AdminView adminView;
    private final InventoryService inventoryService;  // Add this field

    public AdministratorController(AppointmentService appointmentService, ProjectAdminService adminService) {
        this.appointmentService = appointmentService;
        this.adminService = adminService;

        // Initialize InventoryService
        InventoryDataStore inventoryDataStore = new InventoryDataStore();
        this.inventoryService = new InventoryService(inventoryDataStore);

        this.adminView = new AdminView(this, adminService);  // Pass `this` and `adminService` to AdminView
    }


    public void start() {
        boolean exit = false;

        while (!exit) {
            adminView.displayMenu();
            int choice = adminView.getMenuChoice();

            switch (choice) {
                case 1:
                    manageHospitalStaff();
                    break;
                case 2:
                    manageInventory();
                    break;
                case 3:
                    manageAppointments();
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

    public void manageHospitalStaff() {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n-- Manage Hospital Staff --");
            System.out.println("1. Add or Update Staff");
            System.out.println("2. Remove Staff");
            System.out.println("3. View Staff List");
            System.out.println("4. Return to Main Menu");
            System.out.print("Choose an option: ");
            
            int choice = adminView.getMenuChoice();

            switch (choice) {
                case 1:
                    Staff newStaff = adminView.getStaffDetails();
                    adminService.addOrUpdateStaff(newStaff);
                    System.out.println("Staff added/updated successfully.");
                    break;

                case 2:
                    String staffId = adminView.getStaffIDForRemoval();
                    boolean removed = adminService.removeStaff(staffId);
                    if (removed) {
                        System.out.println("Staff removed successfully.");
                    } else {
                        System.out.println("Staff not found.");
                    }
                    break;

                case 3:
                    List<Staff> staffList = adminService.getAllStaff();
                    adminView.displayListOfStaff(staffList);
                    break;

                case 4:
                    exit = true;  // Return to main menu
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public void manageInventory() {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n-- Inventory Management --");
            System.out.println("1. Add New Medication");
            System.out.println("2. Remove Medication");
            System.out.println("3. Update Stock");
            System.out.println("4. Update Low Stock Alert");
            System.out.println("5. Approve Replenishment Request");
            System.out.println("6. View Inventory");
            System.out.println("7. Return to Main Menu");
            System.out.print("Choose an option: ");
            
            int choice = adminView.getMenuChoice();

            switch (choice) {
                case 1:
                    Inventory newMedicine = adminView.getNewMedicationDetails();
                    inventoryService.addMedication(newMedicine);
                    System.out.println("Medication added successfully.");
                    break;

                case 2:
                    String medicineName = adminView.getMedicineNameForRemoval();
                    if (inventoryService.removeMedication(medicineName)) {
                        System.out.println("Medication removed successfully.");
                    } else {
                        System.out.println("Medicine not found.");
                    }
                    break;

                case 3:
                    medicineName = adminView.getMedicineName();
                    int quantity = adminView.getMedicineQuantity();
                    if (inventoryService.updateStockLevel(medicineName, quantity)) {
                        System.out.println("Stock updated.");
                    } else {
                        System.out.println("Medicine not found.");
                    }
                    break;

                case 4:
                    medicineName = adminView.getMedicineName();
                    int lowStockAlertLevel = adminView.getLowStockAlertLevel();
                    if (inventoryService.updateLowStockAlert(medicineName, lowStockAlertLevel)) {
                        System.out.println("Low stock alert updated.");
                    } else {
                        System.out.println("Medicine not found.");
                    }
                    break;

                case 5:
                    String medicineReplenish = adminView.getMedicineName();
                    boolean success = inventoryService.approveReplenishmentRequest(medicineReplenish);
                    if (success) {
                        System.out.println("Replenishment request approved and stock updated.");
                    } else {
                        System.out.println("Replenishment request not found or already processed.");
                    }
                    break;

                case 6:
                    inventoryService.viewMedicationInventory();  // This should display the inventory
                    break;

                case 7:
                    exit = true;  // Return to main menu
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    public void manageAppointments() {
        List<Appointment> appointments = appointmentService.viewScheduledAppointments();
        adminView.displayAppointments(appointments);
    }
}












   



