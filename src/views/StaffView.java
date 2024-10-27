
package views;

import models.Staff;
import models.Appointment;
import models.Inventory;
import java.util.List;
import interfaces.IAdministratorView;

public class StaffView implements IAdministratorView {

    @Override
    public void displayListOfStaff(List<Staff> staffList) {
        System.out.println("\n-- Hospital Staff List --");
        if (staffList == null || staffList.isEmpty()) {
            System.out.println("No staff members to display.");
        } else {
            System.out.println("Staff Members:");
            for (Staff staff : staffList) {
                System.out.println("Staff ID: " + staff.getId() + ", Name: " + staff.getName() + ", Role: " + staff.getRole());
            }
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

    @Override
    public void displayMenu() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'displayMenu'");
    }

    @Override
    public int getMenuChoice() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getMenuChoice'");
    }
}






