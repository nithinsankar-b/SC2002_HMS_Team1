package interfaces;

import models.User;
import models.Appointment;
import models.Inventory;
import java.util.List;

public interface AdministratorView {
    void displayListOfStaff(List<User> staffList);
    void displayAppointments(List<Appointment> appointments);
    void displayInventory(Inventory inventory);
}


