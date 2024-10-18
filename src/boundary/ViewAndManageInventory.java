package boundary;

import models.Inventory;
import models.User;
import models.Appointment;
import java.util.Scanner;
import interfaces.AdministratorView;

public class ViewAndManageInventory implements AdministratorView {

    private Scanner scanner = new Scanner(System.in);

    // Collect input from the user to update the inventory
    public String getMedicineName() {
        System.out.print("Enter Medicine Name: ");
        return scanner.nextLine();
    }

    public int getMedicineQuantity() {
        System.out.print("Enter Quantity: ");
        return scanner.nextInt();
    }

    public int getLowStockAlertLevel() {
        System.out.print("Enter Low Stock Alert Level: ");
        return scanner.nextInt();
    }

    // Display inventory
    @Override
    public void displayInventory(Inventory inventory) {
        System.out.println("\n-- Inventory --");
        inventory.displayStock();  // Assuming `displayStock()` is implemented in Inventory
    }

    // These methods are not applicable for inventory management, so we leave them empty
    @Override
    public void displayListOfStaff(List<User> staffList) {
        // Not applicable for inventory
    }

    @Override
    public void displayAppointments(List<Appointment> appointments) {
        // Not applicable for inventory
    }
}












