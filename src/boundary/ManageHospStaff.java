package boundary;

import models.User;
import models.Inventory;
import models.Appointment;
import java.util.Scanner;
import java.util.List;
import interfaces.AdministratorView;

public class ManageHospStaff implements AdministratorView {

    private Scanner scanner = new Scanner(System.in);

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
        scanner.nextLine(); 
        return new User(staffID, password, role, gender, age);
    }

    public String getStaffIDForRemoval() {
        System.out.print("Enter Staff ID to remove: ");
        return scanner.nextLine();
    }

    @Override
    public void displayListOfStaff(List<User> staffList) {
        System.out.println("\n-- Staff List --");
        for (User staff : staffList) {
            System.out.println("Staff ID: " + staff.getUserID() + ", Role: " + staff.getRole());
        }
    }

    @Override
    public void displayAppointments(List<Appointment> appointments) {
    }

    @Override
    public void displayInventory(Inventory inventory) {
    }
}


