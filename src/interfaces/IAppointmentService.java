package interfaces;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import models.Appointment;
import models.Medication;

/**
 * Interface for appointment-related service operations.
 * Provides methods to manage appointments such as scheduling, cancellation, rescheduling,
 * viewing scheduled appointments, fetching appointment details, and recording outcomes.
 */
public interface IAppointmentService {

    /**
     * Schedules a new appointment.
     *
     * @param appointment The appointment to be scheduled.
     * @return true if the appointment was successfully scheduled, false otherwise.
     */
    boolean scheduleAppointment(Appointment appointment);

    /**
     * Cancels an existing appointment.
     *
     * @param appointmentId The ID of the appointment to be canceled.
     * @return true if the appointment was successfully canceled, false otherwise.
     */
    boolean cancelAppointment(String appointmentId);

    /**
     * Reschedules an existing appointment to a new date and time.
     *
     * @param appointmentId The ID of the appointment to be rescheduled.
     * @param newAppointment The updated appointment details.
     * @return true if the appointment was successfully rescheduled, false otherwise.
     */
    boolean rescheduleAppointment(String appointmentId, Appointment newAppointment);

    /**
     * Retrieves a list of all scheduled appointments.
     *
     * @return A list of all scheduled appointments.
     */
    List<Appointment> viewScheduledAppointments();

    /**
     * Fetches the details of a specific appointment by its ID.
     *
     * @param appointmentId The ID of the appointment to retrieve.
     * @return The Appointment object if found, or null if no such appointment exists.
     */
    Appointment getAppointment(String appointmentId);

    /**
     * Retrieves the available time slots for a specific doctor on a specific date.
     *
     * @param doctorId The ID of the doctor whose availability is to be checked.
     * @param date The date for which to retrieve available slots.
     * @return A list of available LocalDateTime slots for the doctor on the given date.
     */
    List<LocalDateTime> getAvailableSlots(String doctorId, LocalDate date);

    /**
     * Fetches the details of a specific appointment by its ID.
     *
     * @param appointmentId The ID of the appointment to retrieve.
     * @return The Appointment object if found, or null if no such appointment exists.
     */
    Appointment getAppointmentById(String appointmentId);

    /**
     * Records the outcome of a completed appointment, including services provided, prescribed medications,
     * quantities, and consultation notes.
     *
     * @param appointmentId The ID of the appointment whose outcome is to be recorded.
     * @param serviceProvided The service provided during the appointment.
     * @param prescribedMedications A list of medications prescribed during the appointment.
     * @param prescribedQuantities A list of quantities corresponding to the prescribed medications.
     * @param consultationNotes Notes recorded during the consultation.
     */
    void recordAppointmentOutcome(String appointmentId, String serviceProvided, 
                                  List<Medication> prescribedMedications, 
                                  List<Integer> prescribedQuantities, 
                                  String consultationNotes);
}
