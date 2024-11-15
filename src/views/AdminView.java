package views;

import interfaces.IAdministratorView;
import models.Staff;
import models.Appointment;
import models.Inventory;
import services.ProjectAdminService;
import controllers.AdministratorController;

import java.util.List;
import java.util.Scanner;

/**
 * The AdminView class implements the IAdministratorView interface and provides
 * methods for displaying administrator-related views and gathering user input.
 */
public class AdminView implements IAdministratorView {

    private final Scanner scanner = new Scanner(System.in);
    private final AdministratorController adminController;
    private final ProjectAdminService adminService;

    /**
     * Constructs an AdminView with the specified AdministratorController and ProjectAdminService.
     *
     * @param adminController The controller managing administrator actions.
     * @param adminService    The service handling administrative operations.
     */
    public AdminView(AdministratorController adminController, ProjectAdminService adminService) {
        this.adminController = adminController;
        this.adminService = adminService;
    }

    /**
     * Displays the administrator's main menu.
     */
    public void displayMenu() {
        System.out.println("==== Administrator Menu ====");
        System.out.println("1. Manage Hospital Staff");
        System.out.println("2. Manage Inventory");
        System.out.println("3. View Appointments");
        System.out.println("4. Change Password");
        System.out.println("5. Log Out");
        System.out.print("Enter your choice: ");
    }

    @Override
    public void displayListOfStaff(List<Staff> staffList) {
        System.out.println("Hospital Staff List:");
        if (staffList.isEmpty()) {
            System.out.println("No staff members found.");
        } else {
            for (Staff staff : staffList) {
                System.out.println("Staff ID: " + staff.getId() + ", Name: " + staff.getName() +
                                   ", Role: " + staff.getRole() + ", Gender: " + staff.getGender() +
                                   ", Age: " + staff.getAge());
            }
        }
    }

    // Implementing displayAppointments from AdministratorView
    @Override
    public void displayAppointments(List<Appointment> appointments) {
        if (appointments.isEmpty()) {
            System.out.println("No appointments scheduled.");
        } else {
            System.out.println("Scheduled Appointments:");
            for (Appointment appointment : appointments) {
                System.out.println("Appointment ID: " + appointment.getAppointmentId());
                System.out.println("Patient ID: " + appointment.getPatientId());
                System.out.println("Doctor ID: " + appointment.getDoctorId());
                System.out.println("Date & Time: " + appointment.getAppointmentDateTime());
                System.out.println("Status: " + appointment.getStatus()); // Added line for status
                System.out.println("-------------------------------");
            }
        }
    }

    @Override
    public void displayInventory(List<Inventory> inventory) {
        System.out.println("Inventory List:");
        for (Inventory item : inventory) {
            System.out.println("Medicine: " + item.getMedicineName() +
                               ", Stock: " + item.getCurrentStock() +
                               ", Low Stock Alert: " + item.getLowLevelAlert() +
                               ", Replenishment Status: " + item.getReplenishmentStatus());
        }
    }

    /**
     * Retrieves the user's menu choice from the administrator's main menu.
     *
     * @return The selected menu option as an integer.
     */
    public int getMenuChoice() {
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline
        return choice;
    }

    /**
     * Gathers details for a new or updated staff member.
     *
     * @return A new Staff object with the provided details.
     */
    public Staff getStaffDetails() {
        System.out.print("Enter Staff ID: ");
        String staffID = scanner.nextLine();
    
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();
    
        // Validate Role with first letter capitalized
        String role;
        while (true) {
            System.out.print("Enter Role (Doctor/Pharmacist): "); // Exclude Administrator
            role = scanner.nextLine().trim();
            role = role.substring(0, 1).toUpperCase() + role.substring(1).toLowerCase();
            if (role.equals("Doctor") || role.equals("Pharmacist")) { // Only allow Doctor or Pharmacist
                break;
            } else {
                System.out.println("Invalid input. Role must be Doctor or Pharmacist.");
            }
        }
    
        // Validate Gender with first letter capitalized
        String gender;
        while (true) {
            System.out.print("Enter Gender (Male/Female): ");
            gender = scanner.nextLine().trim();
            gender = gender.substring(0, 1).toUpperCase() + gender.substring(1).toLowerCase();
            if (gender.equals("Male") || gender.equals("Female")) {
                break;
            } else {
                System.out.println("Invalid input. Gender must be Male or Female.");
            }
        }
    
        System.out.print("Enter Age: ");
        int age = scanner.nextInt();
        scanner.nextLine();  // Consume newline
    
        return new Staff(staffID, name, role, gender, age);
    }
    

    /**
     * Prompts the user to enter a staff ID for removal.
     *
     * @return The staff ID entered by the user.
     */
    public String getStaffIDForRemoval() {
        System.out.print("Enter Staff ID to remove: ");
        return scanner.nextLine();
    }

    /**
     * Gathers details for a new medication to be added to the inventory.
     *
     * @return A new Inventory object with the provided details.
     */
    public Inventory getNewMedicationDetails() {
        System.out.print("Enter Medicine Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Initial Stock Level: ");
        int stock = scanner.nextInt();

        System.out.print("Enter Low Stock Alert Level: ");
        int lowStockAlert = scanner.nextInt();

        scanner.nextLine(); // Consume newline
        return new Inventory(name, stock, lowStockAlert, null); // Default null for status
    }

    /**
     * Prompts the user to enter a medicine name for removal from the inventory.
     *
     * @return The medicine name entered by the user.
     */
    public String getMedicineNameForRemoval() {
        System.out.print("Enter Medicine Name to Remove: ");
        return scanner.nextLine();
    }

    /**
     * Prompts the user to enter a medicine name.
     *
     * @return The medicine name entered by the user.
     */
    public String getMedicineName() {
        System.out.print("Enter Medicine Name: ");
        return scanner.nextLine();
    }

    /**
     * Prompts the user to enter a quantity for a medication.
     *
     * @return The quantity entered by the user.
     */
    public int getMedicineQuantity() {
        System.out.print("Enter Medicine Quantity: ");
        return scanner.nextInt();
    }

    /**
     * Prompts the user to enter a new low stock alert level for a medication.
     *
     * @return The low stock alert level entered by the user.
     */
    public int getLowStockAlertLevel() {
        System.out.print("Enter the new low stock alert level: ");
        return scanner.nextInt();
    }

    /**
     * Gathers old and new passwords for changing the administrator's password.
     *
     * @param hospitalID The ID of the hospital for which the password is being changed.
     */
    public void displayChangePassword(String hospitalID) {
        System.out.print("Enter Current Password: ");
        String oldPassword = scanner.nextLine();

        System.out.print("Enter New Password: ");
        String newPassword = scanner.nextLine();

        boolean isSuccess = adminController.changePassword(hospitalID, oldPassword, newPassword);
        if (isSuccess) {
            System.out.println("Password changed successfully!");
        } else {
            System.out.println("Failed to change password. Please check your current password.");
        }
    }

    public String getRequestIdForReplenishment() {
        System.out.print("Enter Request ID: ");
        return scanner.nextLine();
    }
    
}



