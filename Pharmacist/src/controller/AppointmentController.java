package controller;

import java.time.LocalDateTime;
import models.Appointment;
import models.Medication;
import services.AppointmentService;

import java.util.ArrayList;
import java.util.List;

public class AppointmentController {
    private final List<Appointment> appointments;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointments = new ArrayList<>();}

    public void scheduleAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    public boolean cancelAppointment(String appointmentId) {
        return appointments.removeIf(appointment -> appointment.getAppointmentId().equals(appointmentId));
    }

    public boolean rescheduleAppointment(String appointmentId, Appointment newAppointment) {
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getAppointmentId().equals(appointmentId)) {
                appointments.set(i, newAppointment);
                return true;
            }
        }
        return false;
    }

    public List<Appointment> viewScheduledAppointments() {
        return new ArrayList<>(appointments);
    }

    public Appointment getAppointment(String appointmentId) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equals(appointmentId)) {
                return appointment;
            }
        }
        return null;
    }

    public void recordAppointmentOutcome(String appointmentId, String serviceProvided, String consultationNotes, List<Medication> medications) {
        Appointment appointment = getAppointment(appointmentId);
        if (appointment == null) {
            // Log the error or throw an exception
            System.out.println("Appointment not found for ID: " + appointmentId);
            return;
        }
        appointment.setServiceProvided(serviceProvided);
        appointment.setConsultationNotes(consultationNotes);
        for (Medication medication : medications) {
            appointment.addMedication(medication);
        }
    }
}