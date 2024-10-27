package views;

import interfaces.IAdministratorView;
import models.Staff;
import models.Appointment;
import models.Inventory;
import java.util.List;
import java.util.Scanner;

public class AdminView implements IAdministratorView {
    private final Scanner scanner = new Scanner(System.in);

    public void displayMenu() {
        System.out.println("==== Administrator Menu ====");
        System.out.println("1. Manage Hospital Staff");
        System.out.println("2. Manage Inventory");
        System.out.println("3. View Appointments");
        System.out.println("4. Exit");
        System.out.print("Enter your choice: ");
    }

    @Override
    public void displayListOfStaff(List<Staff> staffList) {
        System.out.println("Hospital Staff List:");
        for (Staff staff : staffList) {
            System.out.println("Staff ID: " + staff.getId() + ", Name: " + staff.getName() +
                               ", Role: " + staff.getRole() + ", Gender: " + staff.getGender() +
                               ", Age: " + staff.getAge());
        }
    }

    @Override
    public void displayAppointments(List<Appointment> appointments) {
        System.out.println("Appointments List:");
        for (Appointment appointment : appointments) {
            System.out.println(appointment);  // Assumes Appointment has a suitable toString() method
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

    public int getMenuChoice() {
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline
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
        scanner.nextLine();  // Consume newline
        return new Staff(staffID, name, role, gender, age);
    }

    public String getStaffIDForRemoval() {
        System.out.print("Enter Staff ID to remove: ");
        return scanner.nextLine();
    }

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

    public String getMedicineNameForRemoval() {
        System.out.print("Enter Medicine Name to Remove: ");
        return scanner.nextLine();
    }

    public String getMedicineName() {
        System.out.print("Enter Medicine Name: ");
        return scanner.nextLine();
    }

    public int getMedicineQuantity() {
        System.out.print("Enter Medicine Quantity: ");
        return scanner.nextInt();
    }

    public int getLowStockAlertLevel() {
        System.out.print("Enter the new low stock alert level: ");
        return scanner.nextInt();
    }
}

