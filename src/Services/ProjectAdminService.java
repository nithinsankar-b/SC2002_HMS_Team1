package services;

import models.Administrator;
import models.User;
import models.Appointment;
import models.ReplenishmentRequest;
import models.Inventory;
import java.util.List;
import java.util.ArrayList;

public class ProjectAdminService implements IProjectAdmService {
    private Administrator administrator;

    public ProjectAdminService(Administrator administrator) {
        this.administrator = administrator;
    }

    @Override
    public void addStaff(User staffMember) {
        administrator.getHospitalStaff().put(staffMember.getUserID(), staffMember);
        System.out.println("Staff member added: " + staffMember.getUserID());
    }

    @Override
    public void removeStaff(User staffMember) {
        User removed = administrator.getHospitalStaff().remove(staffMember.getUserID());
        if (removed != null) {
            System.out.println("Staff member removed: " + staffMember.getUserID());
        } else {
            System.out.println("Staff member not found: " + staffMember.getUserID());
        }
    }

    @Override
    public void updateStaff(User staffMember) {
        if (administrator.getHospitalStaff().containsKey(staffMember.getUserID())) {
            administrator.getHospitalStaff().put(staffMember.getUserID(), staffMember);
            System.out.println("Staff member updated: " + staffMember.getUserID());
        } else {
            System.out.println("Staff member not found: " + staffMember.getUserID());
        }
    }

    @Override
    public List<User> getAllStaff() {
        return new ArrayList<>(administrator.getHospitalStaff().values());
    }

    @Override
    public List<Appointment> viewAppointments() {
        return administrator.getAppointments();
    }

    @Override
    public void updateStockLevel(String medicineName, int quantity) {
        administrator.getInventory().updateStock(medicineName, quantity);
        System.out.println("Updated stock level for " + medicineName);
    }

    @Override
    public void updateLowStockAlert(String medicineName, int lowStockLevel) {
        administrator.getInventory().updateLowStockAlert(medicineName, lowStockLevel);
        System.out.println("Updated low stock alert level for " + medicineName);
    }

    @Override
    public void approveReplenishmentRequest(ReplenishmentRequest request) {
        if (request.getStatus() == RequestStatus.PENDING) {
            request.setStatus(RequestStatus.APPROVED);
            administrator.getInventory().updateStock(request.getMedicine(), request.getRequestedQuantity());
            System.out.println("Replenishment request approved for: " + request.getMedicine());
        } else {
            System.out.println("Request is not in pending state.");
        }
    }

    @Override
    public Inventory getInventory() {
        return administrator.getInventory();
    }
}

