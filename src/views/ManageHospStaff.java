package views;

import models.Staff;
import models.Inventory;
import models.Appointment;
import java.util.List;
import java.util.Scanner;
import interfaces.IAdministratorView;

/**
 * The ManageHospStaff class implements the IAdministratorView interface and provides
 * methods for managing hospital staff, including adding, updating, and removing staff,
 * as well as displaying the staff list.
 */
public class ManageHospStaff implements IAdministratorView {

    private Scanner scanner = new Scanner(System.in);

    /**
     * Displays the Hospital Staff Management menu.
     */
    @Override
    public void displayMenu() {
        System.out.println("\n-- Manage Hospital Staff --");
        System.out.println("1. Add or Update Staff");
        System.out.println("2. Remove Staff");
        System.out.println("3. View Staff List");
        System.out.println("4. Return to Main Menu");
        System.out.print("Choose an option: ");
    }

    /**
     * Retrieves the user's menu choice from the Hospital Staff Management menu.
     *
     * @return The selected menu option as an integer.
     */
    @Override
    public int getMenuChoice() {
        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline
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
        System.out.print("Enter Role (DOCTOR/PHARMACIST): ");
        String role = scanner.nextLine();
        System.out.print("Enter Gender: ");
        String gender = scanner.nextLine();
        System.out.print("Enter Age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline
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
     * Displays the list of hospital staff members.
     *
     * @param staffList The list of Staff objects to be displayed.
     */
    @Override
    public void displayListOfStaff(List<Staff> staffList) {
        System.out.println("\n-- Staff List --");
        if (staffList == null || staffList.isEmpty()) {
            System.out.println("No staff members to display.");
        } else {
            for (Staff staff : staffList) {
                System.out.println("Staff ID: " + staff.getId() + ", Name: " + staff.getName() +
                                   ", Role: " + staff.getRole() + ", Gender: " + staff.getGender() +
                                   ", Age: " + staff.getAge());
            }
        }
    }

    /**
     * This method is not applicable for ManageHospStaff as it does not handle appointments.
     *
     * @param appointments The list of Appointment objects (not used).
     */
    @Override
    public void displayAppointments(List<Appointment> appointments) {
        // Not applicable for ManageHospStaff
    }

    /**
     * This method is not applicable for ManageHospStaff as it does not handle inventory.
     *
     * @param inventory The list of Inventory objects (not used).
     */
    @Override
    public void displayInventory(List<Inventory> inventory) {
        // Not applicable for ManageHospStaff
    }
}






