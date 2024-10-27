
package views;

import models.Staff;
import models.Appointment;
import models.Inventory;
import java.util.List;
import interfaces.AdministratorView;

public class StaffView implements AdministratorView {

    @Override
    public void displayListOfStaff(List<Staff> staffList) {
        System.out.println("Staff Members:");
        for (Staff staff : staffList) {
            System.out.println("Staff ID: " + staff.getName() + ", Role: " + staff.getRole());
        }
    }

    @Override
    public void displayInventory(List<Inventory> inventory) {
        // Not applicable for StaffView
    }

    @Override
    public void displayAppointments(List<Appointment> appointments) {
        // Not applicable for StaffView
    }
}






