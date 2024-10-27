package interfaces;

import models.Staff;
import models.Appointment;
import models.Inventory;
import java.util.List;

public interface IAdministratorView {
    // Method to display the administrator's main menu
    void displayMenu();
    
    // Display list of staff members
    void displayListOfStaff(List<Staff> staffList);
    
    // Display list of appointments
    void displayAppointments(List<Appointment> appointments);
    
    // Display inventory list
    void displayInventory(List<Inventory> inventory);
    
    // Method to get the user's choice from the menu
    int getMenuChoice();
}




