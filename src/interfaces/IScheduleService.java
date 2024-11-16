package interfaces;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Interface for managing doctor schedules, including booking, canceling,
 * and setting availability or unavailability for appointments.
 */
public interface IScheduleService {

    /**
     * Books an appointment for a specific doctor at a specified date and time slot.
     *
     * @param doctorID  The unique identifier of the doctor.
     * @param date      The date of the appointment.
     * @param timeSlot  The time slot for the appointment.
     * @param patientID The unique identifier of the patient booking the appointment.
     * @return {@code true} if the appointment was successfully booked; {@code false} otherwise.
     */
    boolean bookAppointment(String doctorID, LocalDate date, LocalTime timeSlot, String patientID);

    /**
     * Cancels an appointment for a specific doctor at a specified date and time slot.
     *
     * @param doctorID  The unique identifier of the doctor.
     * @param date      The date of the appointment to cancel.
     * @param timeSlot  The time slot of the appointment to cancel.
     * @param patientID The unique identifier of the patient whose appointment is being canceled.
     * @return {@code true} if the appointment was successfully canceled; {@code false} otherwise.
     */
    boolean cancelAppointment(String doctorID, LocalDate date, LocalTime timeSlot, String patientID);

    /**
     * Sets a specific date and time slot as available for a doctor.
     *
     * @param doctorID  The unique identifier of the doctor.
     * @param date      The date to set as available.
     * @param timeSlot  The time slot to set as available.
     */
    void setAvailable(String doctorID, LocalDate date, LocalTime timeSlot);

    /**
     * Sets a specific date and time slot as unavailable for a doctor.
     *
     * @param doctorID  The unique identifier of the doctor.
     * @param date      The date to set as unavailable.
     * @param timeSlot  The time slot to set as unavailable.
     */
    void setUnavailable(String doctorID, LocalDate date, LocalTime timeSlot);
}

