// src/controllers/AppointmentController.java
package controllers;

import models.Appointment;
import models.Medication;
import services.AppointmentService;

import java.time.LocalDateTime;
import java.util.List;

public class AppointmentController {
    private AppointmentService appointmentService;

    public AppointmentController() {
        this.appointmentService = new AppointmentService();
    }

    public void scheduleAppointment(String appointmentId, String patientId, String doctorId, LocalDateTime appointmentDateTime) {
        Appointment appointment = new Appointment(appointmentId, patientId, doctorId, appointmentDateTime);
        if (appointmentService.scheduleAppointment(appointment)) {
            System.out.println("Appointment scheduled successfully.");
        } else {
            System.out.println("Failed to schedule appointment.");
        }
    }

    public void cancelAppointment(String appointmentId) {
        if (appointmentService.cancelAppointment(appointmentId)) {
            System.out.println("Appointment canceled successfully.");
        } else {
            System.out.println("Failed to cancel appointment.");
        }
    }

    public void rescheduleAppointment(String appointmentId, Appointment newAppointment) {
        if (appointmentService.rescheduleAppointment(appointmentId, newAppointment)) {
            System.out.println("Appointment rescheduled successfully.");
        } else {
            System.out.println("Failed to reschedule appointment.");
        }
    }

    public void recordOutcome(String appointmentId, String serviceProvided, List<Medication> prescribedMedications, String consultationNotes) {
        appointmentService.recordAppointmentOutcome(appointmentId, serviceProvided, prescribedMedications, consultationNotes);
        System.out.println("Appointment outcome recorded successfully.");
    }

    public void viewScheduledAppointments() {
        appointmentService.viewScheduledAppointments().forEach(appointment -> {
            System.out.println("Appointment ID: " + appointment.getAppointmentId());
            System.out.println("Patient ID: " + appointment.getPatientId());
            System.out.println("Doctor ID: " + appointment.getDoctorId());
            System.out.println("Date & Time: " + appointment.getAppointmentDateTime());
            System.out.println("Status: " + appointment.getStatus());
            System.out.println("Service Provided: " + appointment.getServiceProvided());
            System.out.println("Consultation Notes: " + appointment.getConsultationNotes());
            appointment.getMedications().forEach(medication -> {
                System.out.println("Medication: " + medication.getName() + ", Quantity: " + medication.getQuantity() + ", Status: " + medication.getStatus());
            });
            System.out.println("------------------------");
        });
    }
}
