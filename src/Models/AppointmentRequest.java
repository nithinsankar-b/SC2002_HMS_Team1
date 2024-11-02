
package models;

import java.time.LocalDate;
import java.time.LocalTime;

public class AppointmentRequest {
    private String requestId;
    private String patientId;
    private String doctorId;
    private LocalDate requestedDate;
    private LocalTime requestedTimeSlot;
    private String status; // Can be "Pending", "Accepted", "Declined"

    // Constructor
    public AppointmentRequest(String requestId, String patientId, String doctorId, LocalDate requestedDate, LocalTime requestedTimeSlot, String status) {
        this.requestId = requestId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.requestedDate = requestedDate;
        this.requestedTimeSlot = requestedTimeSlot;
        this.status = status;
    }

    // Getters and setters
    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public LocalDate getRequestedDate() {
        return requestedDate;
    }

    public void setRequestedDate(LocalDate requestedDate) {
        this.requestedDate = requestedDate;
    }

    public LocalTime getRequestedTimeSlot() {
        return requestedTimeSlot;
    }

    public void setRequestedTimeSlot(LocalTime requestedTimeSlot) {
        this.requestedTimeSlot = requestedTimeSlot;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Convert AppointmentRequest to CSV string
    public String toString() {
        return String.join(",", requestId, doctorId, patientId, requestedDate.toString(), requestedTimeSlot.toString(), status);
    }

    // Static method to create an AppointmentRequest from a CSV string
    public static AppointmentRequest fromString(String csv) {
        String[] fields = csv.split(",");
        
        if (fields.length == 6) { // Ensure we have the correct number of fields
            String requestId = fields[0];
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

