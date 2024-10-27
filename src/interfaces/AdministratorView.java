package interfaces;

import models.Staff;
import models.Appointment;
import models.Inventory;
import java.util.List;

public interface AdministratorView {
    void displayListOfStaff(List<Staff> staffList);
    void displayAppointments(List<Appointment> appointments);
    void displayInventory(List<Inventory> inventory);
}


