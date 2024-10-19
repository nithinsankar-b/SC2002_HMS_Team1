package boundary;

import models.Inventory;
import models.Appointment;
import models.User;
import interfaces.AdministratorView;
import java.util.Scanner;

public class ViewAndManageInventory implements AdministratorView {

    private Scanner scanner = new Scanner(System.in);

    public String getMedicineName() {
        System.out.print("Enter medicine name: ");
        return scanner.nextLine();
    }

    public int getMedicineQuantity() {
        System.out.print("Enter quantity: ");
        return scanner.nextInt();
    }

    @Override
    public void displayInventory(Inventory inventory) {
        System.out.println("\n-- Inventory --");
        System.out.println("Medicine Name: " + inventory.getMedicineName());
        System.out.println("Stock Level: " + inventory.getStockLevel());
        System.out.println("Low Stock Alert Level: " + inventory.getLowStockAlertLevel());
    }

    @Override
    public void displayListOfStaff(List<User> staffList) {
    }

    @Override
    public void displayAppointments(List<Appointment> appointments) {
    }
}












