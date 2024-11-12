package controllers;

import models.Appointment;
import models.Medication;
import services.AppointmentService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * The AppointmentController class provides an interface for managing appointments.
 * It acts as an intermediary between the service layer and the presentation layer.
 * This class allows scheduling, canceling, rescheduling, and viewing appointments, as well as recording outcomes
 * and managing medication status.
 */
public class AppointmentController {
    private final AppointmentService appointmentService;

    /**
     * Constructs an AppointmentController and initializes the AppointmentService.
     */
    public AppointmentController() {
        this.appointmentService = new AppointmentService();
    }

    /**
     * Schedules a new appointment.
     *
     * @param appointment the appointment to be scheduled
     * @return true if the appointment was successfully scheduled; false otherwise
     */
    public boolean scheduleAppointment(Appointment appointment) {
        return appointmentService.scheduleAppointment(appointment);
    }

    /**
     * Cancels an existing appointment based on its appointment ID.
     *
     * @param appointmentId the ID of the appointment to be canceled
     * @return true if the appointment was successfully canceled; false otherwise
     */
    public boolean cancelAppointment(String appointmentId) {
        return appointmentService.cancelAppointment(appointmentId);
    }

    /**
     * Reschedules an existing appointment.
     *
     * @param appointmentId the ID of the appointment to be rescheduled
     * @param newAppointment the new appointment details
     * @return true if the appointment was successfully rescheduled; false otherwise
     */
    public boolean rescheduleAppointment(String appointmentId, Appointment newAppointment) {
        return appointmentService.rescheduleAppointment(appointmentId, newAppointment);
    }

    /**
     * Retrieves a list of all scheduled appointments.
     *
     * @return a list of scheduled appointments
     */
    public List<Appointment> viewScheduledAppointments() {
        return appointmentService.viewScheduledAppointments();
    }

    /**
     * Retrieves an appointment by its appointment ID.
     *
     * @param appointmentId the ID of the appointment to retrieve
     * @return the appointment with the specified ID, or null if not found
     */
    public Appointment getAppointment(String appointmentId) {
        return appointmentService.getAppointment(appointmentId);
    }

    /**
     * Records the outcome of an appointment, including the service provided, prescribed medications,
     * and consultation notes.
     *
     * @param appointmentId         the ID of the appointment
     * @param serviceProvided        the service provided during the appointment
     * @param prescribedMedications  the list of medications prescribed during the appointment
     * @param consultationNotes      notes from the consultation
     */
    public void recordAppointmentOutcome(String appointmentId, String serviceProvided, List<Medication> prescribedMedications, List<Integer> prescribedQuantities, String consultationNotes) {
        appointmentService.recordAppointmentOutcome(appointmentId, serviceProvided, prescribedMedications, prescribedQuantities, consultationNotes);
    }

    /**
     * Retrieves available time slots for scheduling an appointment based on the doctor's ID and date.
     *
     * @param doctorId the ID of the doctor
     * @param date     the date for which to check available slots
     * @return a list of available LocalDateTime slots
     */
    public List<LocalDateTime> getAvailableSlots(String doctorId, LocalDate date) {
        return appointmentService.getAvailableSlots(doctorId, date);
    }

    /**
     * Updates the medication status of a specified appointment.
     *
     * @param appointmentId the ID of the appointment whose medication status should be updated
     */
    public void updateMedicationStatus(String appointmentId) {
        appointmentService.updateMedicationStatus(appointmentId);
    }
}
