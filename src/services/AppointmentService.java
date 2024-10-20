package services;

import enums.AppointmentStatus; // Import the interface
import interfaces.IAppointmentService;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import models.Appointment;
import models.Medication;

public class AppointmentService implements IAppointmentService { // Implement the interface
    private final List<Appointment> appointments = new ArrayList<>();

    @Override
    public boolean scheduleAppointment(Appointment appointment) {
        return appointments.add(appointment);
    }

    @Override
    public boolean cancelAppointment(String appointmentId) {
        return appointments.removeIf(appointment -> appointment.getAppointmentId().equals(appointmentId));
    }

    @Override
    public boolean rescheduleAppointment(String appointmentId, Appointment newAppointment) {
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getAppointmentId().equals(appointmentId)) {
                appointments.set(i, newAppointment);
                return true; // Appointment rescheduled
            }
        }
        return false; // Appointment not found
    }

    @Override
    public List<Appointment> viewScheduledAppointments() {
        return appointments;
    }

    @Override
    public Appointment getAppointment(String appointmentId) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equals(appointmentId)) {
                return appointment;
            }
        }
        return null; // Appointment not found
    }

    @Override
    public void recordAppointmentOutcome(String appointmentId, String serviceProvided, List<Medication> prescribedMedications, String consultationNotes) {
        Appointment appointment = getAppointment(appointmentId);
        if (appointment != null && appointment.getStatus() == AppointmentStatus.PENDING) {
            appointment.setStatus(AppointmentStatus.COMPLETED); // Change status to completed
            appointment.setAppointmentOutcomeDate(LocalDateTime.now()); // Record date of outcome
            appointment.setServiceProvided(serviceProvided);
            appointment.getMedications().clear(); // Clear existing medications
            prescribedMedications.forEach(appointment::addMedication); // Add new prescribed medications
            appointment.setConsultationNotes(consultationNotes); // Set consultation notes
        }
    }
}