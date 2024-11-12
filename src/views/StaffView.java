package views;

import models.Staff;
import models.Appointment;
import src.models.Inventory;
import java.util.List;
import interfaces.IAdministratorView;

/**
 * The StaffView class implements the IAdministratorView interface and provides
 * methods for displaying the hospital staff list. Other methods related to
 * inventory and appointments are not applicable for this view.
 */
public class StaffView implements IAdministratorView {

    /**
     * Displays the list of hospital staff members.
     *
     * @param staffList The list of Staff objects to be displayed.
     */
    @Override
    public void displayListOfStaff(List<Staff> staffList) {
        System.out.println("\n-- Hospital Staff List --");
        if (staffList == null || staffList.isEmpty()) {
            System.out.println("No staff members to display.");
        } else {
            System.out.println("Staff Members:");
            for (Staff staff : staffList) {
                System.out.println("Staff ID: " + staff.getId() + 
                                   ", Name: " + staff.getName() + 
                                   ", Gender: " + staff.getGender() + 
                                   ", Role: " + staff.getRole() + 
                                   ", Age: " + staff.getAge());
            }
        }
    }

    /**
     * This method is not applicable for StaffView as it does not handle inventory.
     *
     * @param inventory The list of Inventory objects (not used).
     */
    @Override
    public void displayInventory(List<Inventory> inventory) {
        // Not applicable for StaffView
    }

    /**
     * This method is not applicable for StaffView as it does not handle appointments.
     *
     * @param appointments The list of Appointment objects (not used).
     */
    @Override
    public void displayAppointments(List<Appointment> appointments) {
        // Not applicable for StaffView
    }

    /**
     * This method is currently not implemented.
     */
    @Override
    public void displayMenu() {
        throw new UnsupportedOperationException("Unimplemented method 'displayMenu'");
    }

    /**
     * This method is currently not implemented.
     *
     * @return This method does not return a value.
     */
    @Override
    public int getMenuChoice() {
        throw new UnsupportedOperationException("Unimplemented method 'getMenuChoice'");
    }
}







