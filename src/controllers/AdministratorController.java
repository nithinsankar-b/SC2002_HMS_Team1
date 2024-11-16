package controllers;

import models.*;
import services.*;
import views.AdminView;
import stores.InventoryDataStore;
import interfaces.IInventoryService;

import java.util.List;
import java.util.Scanner;

import enums.StatusEnum;
import views.MedicalInventoryView;

/**
 * The AdministratorController class handles the core functionalities
 * for the administrator, including managing staff, inventory, and appointments.
 */
public class AdministratorController {

    private final ProjectAdminService adminService;
    private final AppointmentService appointmentService;
    private final AdminView adminView;
    private final InventoryService inventoryService;
    private final UserService userService;
    private final ReplenishmentService replenishmentService;
    private String loggedInHospitalID;

    /**
     * Constructor for AdministratorController.
     *
     * @param appointmentService  Service to manage appointments.
     * @param adminService        Service to manage administrative tasks.
     * @param userService         Service to handle user-related operations.
     */
    public AdministratorController(AppointmentService appointmentService, ProjectAdminService adminService, UserService userService, PatientService patientService) {
        this.appointmentService = appointmentService;
        this.adminService = adminService;
        this.userService = userService;
        this.replenishmentService = new ReplenishmentService();
        InventoryDataStore inventoryDataStore = new InventoryDataStore();
        this.inventoryService = new InventoryService(inventoryDataStore);

        this.adminView = new AdminView(this, adminService, userService, patientService);
    }

    /**
     * Starts the administrator session and displays the main menu.
     *
     * @param loggedInHospitalID The hospital ID of the logged-in administrator.
     */
    public void start(String loggedInHospitalID) {
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
                    adminView.displayChangePassword(loggedInHospitalID);
                    break;
                case 5:
                    exit = true;
                    System.out.println("Exiting Administrator session...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Manages hospital staff, including adding, updating, and removing staff.
     */
    public void manageHospitalStaff() {
        boolean exit = false;

        while (!exit) {
            System.out.println("===========================================");
            System.out.println("-- Manage Hospital Staff --");
            System.out.println("===========================================");
            System.out.println("1. Add or Update Staff");
            System.out.println("2. Remove Staff");
            System.out.println("3. View Staff List");
//            System.out.println("4. Manage Patients");
            System.out.println("4. User Password Recovery");
            System.out.println("5. Return to Main Menu");
            System.out.print("Choose an option: ");

            int choice = adminView.getMenuChoice();

            switch (choice) {
                case 1:
                    Staff newStaff = adminView.getStaffDetails();
                    if (newStaff != null) {
                        adminService.addOrUpdateStaff(newStaff);
                        System.out.println("Staff added/updated successfully.");
                    } else{
                        continue;
                    }

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
//                case 4:
//                    managePatients();
//                    break;
                case 4:
                    String userId = adminView.getUserIDForPassword();
                    String plaintextPassword = userService.getPlaintextPassword(userId);
                    if (plaintextPassword != null) {
                        System.out.println("Password: " + plaintextPassword);
                    } else {
                        System.out.println("User not found or unable to retrieve password.");
                    }
                    break;
                case 5:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    /**
     * Manages inventory, including adding, updating, and removing medications.
     */
    public void manageInventory() {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n-- Inventory Management --");
            System.out.println("1. Add New Medication");
            System.out.println("2. Remove Medication");
            System.out.println("3. Update Stock");
            System.out.println("4. Update Low Stock Alert");
            System.out.println("5. Approve Replenishment Request");
            System.out.println("6. View Pending Replenishment Requests");
            System.out.println("7. Decline Replenishment Request");
            System.out.println("8. View Inventory");
            System.out.println("9. Return to Main Menu");;
            
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
                    viewPendingReplenishmentRequests();
                    // Get the request ID from the user
                    String requestId = adminView.getRequestIdForReplenishment();

                    // Approve the request in ReplenishmentService and get the medicine names
                    List<String> medicineNames = replenishmentService.approveRequest(requestId);

                    // If valid medicine names are returned, update the inventory stock for each medicine
                    if (medicineNames != null && !medicineNames.isEmpty()) {
                        boolean allUpdatesSuccessful = true;

                        for (String medName : medicineNames) {
                            boolean updateSuccess = inventoryService.approveReplenishmentRequest(medName);

                            if (!updateSuccess) {
                                allUpdatesSuccessful = false;
                                System.out.println("Failed to update inventory for medicine: " + medName);
                            }
                        }

                        if (allUpdatesSuccessful) {
                            System.out.println("Replenishment request approved and inventory updated successfully.");
                        } else {
                            System.out.println("Replenishment request partially updated. Check inventory for details.");
                        }
                    } else {
                        System.out.println("Approval failed. Request ID not found in ReplenishmentService.");
                    }
                    break;

                    case 6:
                    viewPendingReplenishmentRequests();
                    break;
            
                case 7:
                    declineReplenishmentRequest();
                    break;
            
                case 8:
                    MedicalInventoryView medicalInventoryView = new MedicalInventoryView(inventoryService);
                    List<InventoryDisplay> inventory = inventoryService.getInventoryDisplay();
                    medicalInventoryView.display(inventory);
                    break;
            
                case 9:
                    exit = true;
                    break;
            
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    // Add a new method for managing patients
    public void managePatients() {
        boolean exit = false;

        while (!exit) {
            System.out.println("===========================================");
            System.out.println("-- Manage Patients --");
            System.out.println("===========================================");
            System.out.println("1. Add New Patient");
            System.out.println("2. View Patient List");
            System.out.println("3. Remove Patient");
            System.out.println("4. Return to Main Menu");
            System.out.print("Choose an option: ");

            int choice = adminView.getMenuChoice();

            switch (choice) {
                case 1:
                    Patient newPatient = adminView.getPatientDetails();
                    if (newPatient != null) {
                        adminService.addOrUpdatePatient(newPatient);
                    }
                    break;
                case 2:
                    List<Patient> patientList = adminService.getAllPatients();
                    adminView.displayPatientList(patientList);
                    break;
                case 3:
                    System.out.print("Enter Patient ID to remove: ");
                    String patientIdToDelete = new Scanner(System.in).nextLine();
                    boolean deleted = adminService.removePatient(patientIdToDelete);
                    if (deleted) {
                        System.out.println("Patient removed successfully.");
                    } else {
                        System.out.println("Failed to remove patient.");
                    }
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }


    /**
     * Manages appointments, displaying the list of scheduled appointments.
     */
    public void manageAppointments() {
        List<Appointment> appointments = appointmentService.viewScheduledAppointments();
        adminView.displayAppointments(appointments);
    }

    /**
     * Changes the password for the administrator.
     *
     * @param hospitalID   The hospital ID of the administrator.
     * @param oldPassword  The current password.
     * @param newPassword  The new password.
     * @return True if the password was successfully changed, false otherwise.
     */
    public boolean changePassword(String hospitalID, String oldPassword, String newPassword) {
        return userService.changePassword(hospitalID, oldPassword, newPassword);
    }

    private void viewPendingReplenishmentRequests() {
        List<ReplenishmentRequest> pendingRequests = replenishmentService.getRequestsByStatus(StatusEnum.PENDING);
        
        if (pendingRequests.isEmpty()) {
            System.out.println("No pending replenishment requests found.");
        } else {
            System.out.println("Pending Replenishment Requests:");
            for (ReplenishmentRequest request : pendingRequests) {
                System.out.println("Request ID: " + request.getId() + ", Medicines: " + request.getMedicines() + ", Status: " + request.getStatus());
            }
        }
    }
    

private void declineReplenishmentRequest() {
    Scanner sc = new Scanner(System.in);
    System.out.print("Enter Request ID to decline: ");
    String requestId = sc.nextLine();

    boolean success = replenishmentService.rejectRequest(requestId);
    if (success) {
        System.out.println("Replenishment request declined successfully.");
    } else {
        System.out.println("Failed to decline replenishment request. Request ID not found.");
    }
}


}













   



