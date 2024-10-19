// src/services/AppointmentService.java
package services;

import models.Appointment;
import enums.AppointmentStatus;
import java.time.LocalDateTime;
import models.Medication;

import java.util.ArrayList;
import java.util.List;

public class AppointmentService {
    private List<Appointment> appointments = new ArrayList<>();

    public boolean scheduleAppointment(Appointment appointment) {
        return appointments.add(appointment);
    }

    public boolean cancelAppointment(String appointmentId) {
        return appointments.removeIf(appointment -> appointment.getAppointmentId().equals(appointmentId));
    }

    public boolean rescheduleAppointment(String appointmentId, Appointment newAppointment) {
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getAppointmentId().equals(appointmentId)) {
                appointments.set(i, newAppointment);
                return true; // Appointment rescheduled
            }
        }
        return false; // Appointment not found
    }

    public List<Appointment> viewScheduledAppointments() {
        return appointments;
    }

    public Appointment getAppointment(String appointmentId) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equals(appointmentId)) {
                return appointment;
            }
        }
        return null; // Appointment not found
    }

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
