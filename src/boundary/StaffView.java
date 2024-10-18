package boundary;

import Models.User;
import Models.Inventory;
import Models.Appointment;
import java.util.List;
import interfaces.AdministratorView;

public class StaffView implements AdministratorView {

    @Override
    public void displayListOfStaff(List<User> staffList) {
        System.out.println("Staff Members:");
        for (User staff : staffList) {
            System.out.println("Staff ID: " + staff.getUserID() + ", Role: " + staff.getRole());
        }
    }

    @Override
    public void displayInventory(Inventory inventory) {
        // Not applicable for StaffView
    }

    @Override
    public void displayAppointments(List<Appointment> appointments) {
        // Not applicable for StaffView
    }
}






