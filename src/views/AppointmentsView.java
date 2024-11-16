package views;

import models.Appointment;
import models.Inventory;
import models.Staff;
import interfaces.IAdministratorView;
import interfaces.IAppointmentService;

import java.util.List;
import java.util.Scanner;

/**
 * The AppointmentsView class provides a view for displaying and managing scheduled appointments.
 * Implements the IAdministratorView interface for viewing and managing administrative data.
 */
public class AppointmentsView implements IAdministratorView {

    private final Scanner scanner = new Scanner(System.in);
    private final IAppointmentService appointmentService;  // Dependency on AppointmentService

    /**
     * Constructor for dependency injection of the appointment service.
     *
     * @param appointmentService The service for managing appointments.
     */
    public AppointmentsView(IAppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * Displays all scheduled appointments by fetching them from the appointment service.
     */
    public void viewScheduledAppointments() {
        System.out.println("==== Scheduled Appointments ====");
        List<Appointment> appointments = appointmentService.viewScheduledAppointments();  // Call service to get appointments
        displayAppointments(appointments);  // Display them
    }

    /**
     * Displays a list of appointments.
     * Implements the method from the IAdministratorView interface.
     *
     * @param appointments The list of appointments to display.
     */
    @Override
    public void displayAppointments(List<Appointment> appointments) {
        if (appointments.isEmpty()) {
            System.out.println("No appointments scheduled.");
        } else {
            System.out.println("Scheduled Appointments:");
            for (Appointment appointment : appointments) {
                System.out.println("Appointment ID: " + appointment.getAppointmentId());
                System.out.println("Patient ID: " + appointment.getPatientId());
                System.out.println("Doctor ID: " + appointment.getDoctorId());
                System.out.println("Date & Time: " + appointment.getAppointmentDateTime());
                System.out.println("Status: " + appointment.getStatus()); // Display the status of the appointment
                System.out.println("-------------------------------");
            }
        }
    }

    /**
     * Displays a list of staff members.
     * Not implemented in AppointmentsView.
     *
     * @param staffList The list of staff members to display.
     */
    @Override
    public void displayListOfStaff(List<Staff> staffList) {
        // Not applicable for AppointmentsView
    }

    /**
     * Displays a list of inventory items.
     * Not implemented in AppointmentsView.
     *
     * @param inventory The list of inventory items to display.
     */
    @Override
    public void displayInventory(List<Inventory> inventory) {
        // Not applicable for AppointmentsView
    }

    /**
     * Displays the menu for the administrator view.
     * Not implemented in AppointmentsView.
     */
    @Override
    public void displayMenu() {
        throw new UnsupportedOperationException("Unimplemented method 'displayMenu'");
    }

    /**
     * Gets the menu choice from the user.
     * Not implemented in AppointmentsView.
     *
     * @return The menu choice as an integer.
     */
    @Override
    public int getMenuChoice() {
        throw new UnsupportedOperationException("Unimplemented method 'getMenuChoice'");
    }
}



