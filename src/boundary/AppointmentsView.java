package boundary;

import models.Appointment;
import models.User;
import models.Inventory;
import java.util.List;
import interfaces.AdministratorView;

public class AppointmentsView implements AdministratorView {

    @Override
    public void displayAppointments(List<Appointment> appointments) {
        System.out.println("\n-- Appointments List --");
        for (Appointment appointment : appointments) {
            System.out.println("Appointment ID: " + appointment.getAppointmentId() +
                               ", Status: " + appointment.getStatus() +
                               ", Patient: " + appointment.getPatient().getUserID() +
                               ", Doctor: " + appointment.getDoctor().getUserID());
        }
    }

    @Override
    public void displayListOfStaff(List<User> staffList) {
    }

    @Override
    public void displayInventory(Inventory inventory) {
    }
}









