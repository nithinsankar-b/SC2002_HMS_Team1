package interfaces;

import models.Staff;
import models.Appointment;
import models.Inventory;
import java.util.List;

/**
 * The IAdministratorView interface defines the methods that the administrator view
 * must implement to interact with the administrator and display various information.
 */
public interface IAdministratorView {

    /**
     * Displays the administrator's main menu.
     */
    void displayMenu();

    /**
     * Displays the list of hospital staff members.
     *
     * @param staffList The list of staff to be displayed.
     */
    void displayListOfStaff(List<Staff> staffList);

    /**
     * Displays the list of scheduled appointments.
     *
     * @param appointments The list of appointments to be displayed.
     */
    void displayAppointments(List<Appointment> appointments);

    /**
     * Displays the inventory list, showing medications and their details.
     *
     * @param inventory The list of inventory items to be displayed.
     */
    void displayInventory(List<Inventory> inventory);

    /**
     * Retrieves the user's choice from the displayed menu.
     *
     * @return The user's menu choice as an integer.
     */
    int getMenuChoice();
}





