package models;

import java.time.LocalDate;
import java.time.LocalTime;

public class Schedule {
    private String doctorID;
    private LocalDate date;
    private LocalTime timeSlot;
    private String status; // Status can be "Available", "Blocked", or a Patient ID

    public Schedule(String doctorID, LocalDate date, LocalTime timeSlot, String status) {
        this.doctorID = doctorID;
        this.date = date;
        this.timeSlot = timeSlot;
        this.status = status; // Store status directly
    }

    // Getters and Setters
    public String getDoctorID() {
        return doctorID;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTimeSlot() {
        return timeSlot;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Doctor ID: " + doctorID +
               ", Date: " + date +
               ", Time Slot: " + timeSlot +
               ", Status: " + status;
    }
}
