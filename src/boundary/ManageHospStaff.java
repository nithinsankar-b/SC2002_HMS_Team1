package boundary;

import models.User;
import models.Inventory;
import models.Appointment;
import java.util.Scanner;
import java.util.List;
import interfaces.AdministratorView;

public class ManageHospStaff implements AdministratorView {

    private Scanner scanner = new Scanner(System.in);

    // Method to collect staff details for adding/updating staff
    public User getStaffDetails() {
        System.out.print("Enter Staff ID: ");
        String staffID = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        System.out.print("Enter Role (DOCTOR/PHARMACIST): ");
        String role = scanner.nextLine();
        System.out.print("Enter Gender: ");
        String gender = scanner.nextLine();
        System.out.print("Enter Age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline
        return new User(staffID, password, role, gender, age);
    }

    // Method to collect the staff ID for removing a staff member
    public String getStaffIDForRemoval() {
        System.out.print("Enter Staff ID to remove: ");
        return scanner.nextLine();
    }

    // Display the list of staff
    @Override
    public void displayListOfStaff(List<User> staffList) {
        System.out.println("\n-- Staff List --");
        for (User staff : staffList) {
            System.out.println("Staff ID: " + staff.getUserID() + ", Role: " + staff.getRole());
        }
    }

    // These methods are not applicable for staff management, so we leave them empty
    @Override
    public void displayAppointments(List<Appointment> appointments) {
        // Not applicable for managing staff
    }

    @Override
    public void displayInventory(Inventory inventory) {
        // Not applicable for managing staff
    }
}


