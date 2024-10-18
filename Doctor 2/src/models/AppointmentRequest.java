package models;

import java.time.LocalDate;

public class AppointmentRequest {
    private String requestId;
    private String doctorId;
    private String patientId;
    private LocalDate requestedDate;
    private String requestedTimeSlot;
    private String status; // Pending, Accepted, Declined

    public AppointmentRequest(String requestId, String doctorId, String patientId,
                             LocalDate requestedDate, String requestedTimeSlot, String status) {
        this.requestId = requestId;
        this.doctorId = doctorId;
        this.patientId = patientId;
        this.requestedDate = requestedDate;
        this.requestedTimeSlot = requestedTimeSlot;
        this.status = status;
    }

    // Getters and Setters

    public String getRequestId() {
        return requestId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public LocalDate getRequestedDate() {
        return requestedDate;
    }

    public String getRequestedTimeSlot() {
        return requestedTimeSlot;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // toString method for easy display
    @Override
    public String toString() {
        return "RequestID: " + requestId +
               " | PatientID: " + patientId +
               " | Date: " + requestedDate +
               " | Time Slot: " + requestedTimeSlot +
               " | Status: " + status;
    }
}
