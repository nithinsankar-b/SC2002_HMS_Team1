package interfaces;

import Models.User;
import Models.Appointment;
import Models.Inventory;
import Models.ReplenishmentRequest;
import java.util.List;

public interface IProjectAdmService {
    // Staff-related methods
    void addStaff(User staffMember);
    void removeStaff(User staffMember);
    void updateStaff(User staffMember);
    List<User> getAllStaff();

    // Appointment-related methods
    List<Appointment> viewAppointments();

    // Inventory-related methods
    void updateStockLevel(String medicineName, int quantity);
    void updateLowStockAlert(String medicineName, int lowStockLevel);
    void approveReplenishmentRequest(ReplenishmentRequest request);
    Inventory getInventory();
}
