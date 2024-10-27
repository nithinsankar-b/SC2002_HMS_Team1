
package views;

import models.Staff;
import models.Inventory;
import models.Appointment;
import java.util.List;
import java.util.Scanner;
import interfaces.AdministratorView;

public class ManageHospStaff implements AdministratorView {

    private Scanner scanner = new Scanner(System.in);

    // Method to display the Hospital Staff Management menu
    public void displayMenu() {
        System.out.println("\n-- Manage Hospital Staff --");
        System.out.println("1. Add or Update Staff");
        System.out.println("2. Remove Staff");
        System.out.println("3. View Staff List");
        System.out.println("4. Return to Main Menu");
        System.out.print("Choose an option: ");
    }

    public int getMenuChoice() {
        int choice = scanner.nextInt();
        scanner.nextLine();
        return choice;
    }

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
        scanner.nextLine();
        return new Staff(staffID, name, role, gender, age);
    }

    public String getStaffIDForRemoval() {
        System.out.print("Enter Staff ID to remove: ");
        return scanner.nextLine();
    }

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

    @Override
    public void displayAppointments(List<Appointment> appointments) {
        // Not applicable for ManageHospStaff
    }

    @Override
    public void displayInventory(List<Inventory> inventory) {
        // Not applicable for ManageHospStaff
    }
}





