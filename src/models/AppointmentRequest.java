package models;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Represents an appointment request in the healthcare system.
 * This class contains information about a patient's request for an appointment with a doctor,
 * including the request ID, patient ID, doctor ID, requested date, time slot, and the status of the request.
 */
public class AppointmentRequest {
    private String requestId;
    private String patientId;
    private String doctorId;
    private LocalDate requestedDate;
    private LocalTime requestedTimeSlot;
    private String status; // Can be "Pending", "Accepted", "Declined"

    /**
     * Constructs a new AppointmentRequest with the specified details.
     *
     * @param requestId         The unique identifier for the appointment request.
     * @param patientId        The ID of the patient making the request.
     * @param doctorId         The ID of the doctor for whom the appointment is requested.
     * @param requestedDate     The date for which the appointment is requested.
     * @param requestedTimeSlot The time slot for the requested appointment.
     * @param status           The current status of the appointment request (Pending, Accepted, Declined).
     */
    public AppointmentRequest(String requestId, String patientId, String doctorId, LocalDate requestedDate, LocalTime requestedTimeSlot, String status) {
        this.requestId = requestId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.requestedDate = requestedDate;
        this.requestedTimeSlot = requestedTimeSlot;
        this.status = status;
    }

    // Getters and setters

    /**
     * Returns the request ID of this appointment request.
     *
     * @return The request ID.
     */
    public String getRequestId() {
        return requestId;
    }

    /**
     * Sets the request ID for this appointment request.
     *
     * @param requestId The request ID to set.
     */
    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    /**
     * Returns the patient ID associated with this appointment request.
     *
     * @return The patient ID.
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Sets the patient ID for this appointment request.
     *
     * @param patientId The patient ID to set.
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * Returns the doctor ID associated with this appointment request.
     *
     * @return The doctor ID.
     */
    public String getDoctorId() {
        return doctorId;
    }

    /**
     * Sets the doctor ID for this appointment request.
     *
     * @param doctorId The doctor ID to set.
     */
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    /**
     * Returns the requested date for this appointment.
     *
     * @return The requested date.
     */
    public LocalDate getRequestedDate() {
        return requestedDate;
    }

    /**
     * Sets the requested date for this appointment.
     *
     * @param requestedDate The requested date to set.
     */
    public void setRequestedDate(LocalDate requestedDate) {
        this.requestedDate = requestedDate;
    }

    /**
     * Returns the requested time slot for this appointment.
     *
     * @return The requested time slot.
     */
    public LocalTime getRequestedTimeSlot() {
        return requestedTimeSlot;
    }

    /**
     * Sets the requested time slot for this appointment.
     *
     * @param requestedTimeSlot The requested time slot to set.
     */
    public void setRequestedTimeSlot(LocalTime requestedTimeSlot) {
        this.requestedTimeSlot = requestedTimeSlot;
    }

    /**
     * Returns the current status of this appointment request.
     *
     * @return The status of the appointment request.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the status for this appointment request.
     *
     * @param status The status to set (e.g., Pending, Accepted, Declined).
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Converts this AppointmentRequest to a CSV string representation.
     *
     * @return A CSV string containing the details of this appointment request.
     */
    public String toString() {
        return String.join(",", "\"" + requestId + "\"", doctorId, patientId, requestedDate.toString(), requestedTimeSlot.toString(), status);
    }

    /**
     * Creates an AppointmentRequest from a CSV string.
     *
     * @param csv The CSV string representing an appointment request.
     * @return An AppointmentRequest object created from the CSV string, or null if the input is invalid.
     */
    public static AppointmentRequest fromString(String csv) {
        String[] fields = csv.split(",");
        
        if (fields.length == 6) { // Ensure we have the correct number of fields
            String requestId = fields[0].replace("\"", "").trim();
            String doctorId = fields[1];
            String patientId = fields[2];
            LocalDate requestedDate = LocalDate.parse(fields[3]);
            LocalTime requestedTimeSlot = LocalTime.parse(fields[4]);
            String status = fields[5];

            return new AppointmentRequest(requestId, patientId, doctorId, requestedDate, requestedTimeSlot, status);
        }
        return null;
    }
}
