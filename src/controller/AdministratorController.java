package controller;

import interfaces.IProjectAdmService;
import boundary.ManageHospStaff;
import boundary.AppointmentsView;
import boundary.ViewAndManageInventory;
import Models.User;
import Models.Inventory;
import Models.Appointment;

import java.util.List;

public class AdministratorController {

    private IProjectAdmService projectAdmService;
    private ManageHospStaff staffView;               // Specific for managing staff
    private AppointmentsView appointmentView;        // Specific for handling appointments
    private ViewAndManageInventory inventoryView;    // Specific for managing inventory

    // Constructor
    public AdministratorController(IProjectAdmService projectAdmService,
                                   ManageHospStaff staffView,
                                   AppointmentsView appointmentView,
                                   ViewAndManageInventory inventoryView) {
        this.projectAdmService = projectAdmService;
        this.staffView = staffView;
        this.appointmentView = appointmentView;
        this.inventoryView = inventoryView;
    }

    // Manage Staff
    public void addStaff() {
        User staff = staffView.getStaffDetails();  // Collect input from specific view
        projectAdmService.addStaff(staff);         // Call service layer to add staff
        List<User> staffList = projectAdmService.getAllStaff();  // Get updated list
        staffView.displayListOfStaff(staffList);   // Pass the result back to boundary for display
    }

    public void removeStaff() {
        String staffID = staffView.getStaffIDForRemoval();  // Collect input for staff ID
        projectAdmService.removeStaff(new User(staffID, "", "", "", 0));  // Call service layer to remove staff
        List<User> staffList = projectAdmService.getAllStaff();  // Get updated staff list
        staffView.displayListOfStaff(staffList);  // Pass updated staff list back for display
    }

    // View Appointments
    public void displayAppointments() {
        List<Appointment> appointments = projectAdmService.viewAppointments();
        appointmentView.displayAppointments(appointments);  // Show appointments using the view
    }

    // Manage Inventory
    public void updateInventory() {
        String medicineName = inventoryView.getMedicineName();  // Collect input from user
        int quantity = inventoryView.getMedicineQuantity();     // Collect input from user
        projectAdmService.updateStockLevel(medicineName, quantity);  // Update inventory via service layer
        Inventory updatedInventory = projectAdmService.getInventory();  // Get updated inventory
        inventoryView.displayInventory(updatedInventory);  // Pass updated inventory for display
    }
}




   



