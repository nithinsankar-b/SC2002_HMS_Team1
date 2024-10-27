package views;

import models.Appointment;
import models.Inventory;
import models.Staff;
import interfaces.AdministratorView;
import interfaces.IAppointmentService;

import java.util.List;
import java.util.Scanner;

public class AppointmentsView implements AdministratorView {

    private final Scanner scanner = new Scanner(System.in);
    private final IAppointmentService appointmentService;  // Dependency on AppointmentService

    // Constructor for dependency injection
    public AppointmentsView(IAppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // Method to view and display all scheduled appointments
    public void viewScheduledAppointments() {
        System.out.println("==== Scheduled Appointments ====");
        List<Appointment> appointments = appointmentService.viewScheduledAppointments();  // Call service to get appointments
        displayAppointments(appointments);  // Display them
    }

    // Implementing displayAppointments from AdministratorView
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
                System.out.println("-------------------------------");
            }
        }
    }

    // Placeholder for unimplemented methods from AdministratorView
    @Override
    public void displayListOfStaff(List<Staff> staffList) {
        // Not applicable for AppointmentsView
    }

    @Override
    public void displayInventory(List<Inventory> inventory) {
        // Not applicable for AppointmentsView
    }
}



