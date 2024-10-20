package controllers;

import interfaces.IProjectAdmService;
import boundary.ManageHospStaff;
import boundary.AppointmentsView;
import boundary.ViewAndManageInventory;
import models.User;
import models.Appointment;
import models.Inventory;
import models.ReplenishmentRequest;
import java.util.Scanner;
import java.util.List;

public class AdministratorController {

    private IProjectAdmService projectAdmService;
    private ManageHospStaff staffView;
    private AppointmentsView appointmentView;
    private ViewAndManageInventory inventoryView;
    private Scanner scanner = new Scanner(System.in);

    // Constructor
    public AdministratorController(IProjectAdmService projectAdmService,
                                   ManageHospStaff staffView,
                                   AppointmentsView appointmentView,
                                   ViewAndManageInventory inventoryView) {
        this.projectAdmService = projectAdmService;
        this.staffView = staffView;
        this.appointmentView = appointmentView;
        this.inventoryView = inventoryView;
    }

    // Main menu for Administrator
    public void start() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n-- Administrator Menu --");
            System.out.println("1. View and Manage Hospital Staff");
            System.out.println("2. View Appointments Details");
            System.out.println("3. View and Manage Medication Inventory");
            System.out.println("4. Approve Replenishment Requests");
            System.out.println("5. Logout");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    manageHospitalStaff();
                    break;
                case 2:
                    viewAppointments();
                    break;
                case 3:
                    manageInventory();
                    break;
                case 4:
                    approveReplenishmentRequests();
                    break;
                case 5:
                    exit = true;
                    System.out.println("Logging out...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    // Manage Hospital Staff (Add/Remove/View)
    private void manageHospitalStaff() {
        System.out.println("\n-- Manage Hospital Staff --");
        System.out.println("1. Add Staff");
        System.out.println("2. Remove Staff");
        System.out.println("3. View Staff List");
        System.out.print("Choose an option: ");
        int choice = scanner.nextInt();
        switch (choice) {
            case 1:
                addStaff();
                break;
            case 2:
                removeStaff();
                break;
            case 3:
                staffView.displayListOfStaff(projectAdmService.getAllStaff());
                break;
            default:
                System.out.println("Invalid option.");
        }
    }

    // Adding a new staff member
    private void addStaff() {
        User staff = staffView.getStaffDetails();  // Collect input from staffView
        projectAdmService.addStaff(staff);         // Call service layer to add staff
        staffView.displayListOfStaff(projectAdmService.getAllStaff());  // Display updated staff list
    }

    // Removing a staff member
    private void removeStaff() {
        String staffID = staffView.getStaffIDForRemoval();  // Collect input for staff ID
        projectAdmService.removeStaff(new User(staffID, "", "", "", 0));  // Call service layer to remove staff
        staffView.displayListOfStaff(projectAdmService.getAllStaff());  // Display updated staff list
    }

    // View the list of appointments
    private void viewAppointments() {
        List<Appointment> appointments = projectAdmService.viewAppointments();
        appointmentView.displayAppointments(appointments);  // Show appointments using the view
    }

    // Manage the medication inventory
    private void manageInventory() {
        String medicineName = inventoryView.getMedicineName();  // Collect input from user
        int quantity = inventoryView.getMedicineQuantity();     // Collect input from user
        projectAdmService.updateStockLevel(medicineName, quantity);  // Update inventory via service layer
        Inventory updatedInventory = projectAdmService.getInventory();  // Get updated inventory
        inventoryView.displayInventory(updatedInventory);  // Display updated inventory
    }

    // Approve replenishment requests
    private void approveReplenishmentRequests() {
        System.out.print("Enter medicine name for replenishment request approval: ");
        scanner.nextLine();  // Consume newline
        String medicineName = scanner.nextLine();
        System.out.print("Enter requested quantity: ");
        int quantity = scanner.nextInt();

        ReplenishmentRequest request = new ReplenishmentRequest(medicineName, quantity);
        projectAdmService.approveReplenishmentRequest(request);  // Approve the request
    }
}






   



