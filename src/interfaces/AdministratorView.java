package interfaces;

import Models.User;
import Models.Appointment;
import Models.Inventory;
import java.util.List;

public interface AdministratorView {
    // Method to display a list of staff
    void displayListOfStaff(List<User> staffList);

    // Method to display a list of appointments
    void displayAppointments(List<Appointment> appointments);

    // Method to display inventory details
    void displayInventory(Inventory inventory);
}
