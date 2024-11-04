package models;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents a schedule for a doctor in the healthcare system.
 * This class contains details about the doctor's availability for appointments, including
 * the date, time slot, and status of the appointment.
 */
public class Schedule {
    private String doctorID; // The ID of the doctor associated with this schedule
    private LocalDate date; // The date of the scheduled appointment
    private LocalTime timeSlot; // The time slot for the appointment
    private String status; // The status of the time slot ("Available", "Blocked", or Patient ID)

    /**
     * Constructs a Schedule object with the specified details.
     *
     * @param doctorID The ID of the doctor.
     * @param date The date of the appointment.
     * @param timeSlot The time slot of the appointment.
     * @param status The status of the time slot.
     */
    public Schedule(String doctorID, LocalDate date, LocalTime timeSlot, String status) {
        this.doctorID = doctorID;
        this.date = date;
        this.timeSlot = timeSlot;
        this.status = status; // Store status directly
    }

    // Getters

    /**
     * Returns the doctor ID associated with this schedule.
     *
     * @return The doctor ID.
     */
    public String getDoctorID() {
        return doctorID;
    }

    /**
     * Returns the date of the scheduled appointment.
     *
     * @return The date of the appointment.
     */
    public LocalDate getDate() {
        return date;
    }

    /**
     * Returns the time slot of the scheduled appointment.
     *
     * @return The time slot of the appointment.
     */
    public LocalTime getTimeSlot() {
        return timeSlot;
    }

    /**
     * Returns the status of the time slot.
     *
     * @return The status of the time slot.
     */
    public String getStatus() {
        return status;
    }

    // Setter

    /**
     * Sets the status of the time slot.
     *
     * @param status The status to set (e.g., "Available", "Blocked", or a Patient ID).
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Returns a string representation of the Schedule object.
     *
     * @return A string containing the doctor's ID, date, time slot, and status.
     */
    @Override
    public String toString() {
        return "Doctor ID: " + doctorID +
               ", Date: " + date +
               ", Time Slot: " + timeSlot +
               ", Status: " + status;
    }
}
