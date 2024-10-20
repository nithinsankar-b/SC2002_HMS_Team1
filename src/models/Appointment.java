// src/models/Appointment.java
package models;

import enums.AppointmentStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Appointment {
    private String appointmentId;
    private String patientId;
    private String doctorId;
    private LocalDateTime appointmentDateTime;
    private AppointmentStatus status; // E.g., Pending, Completed, Canceled
    private final List<Medication> medications; // List of prescribed medications
    private String consultationNotes; // Notes recorded during consultation
    private String serviceProvided; // Type of service provided (e.g., consultation, X-ray, etc.)
    private LocalDateTime appointmentOutcomeDate; // Date of appointment outcome record

    public Appointment(String appointmentId, String patientId, String doctorId, LocalDateTime appointmentDateTime) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDateTime = appointmentDateTime;
        this.status = AppointmentStatus.PENDING; // Default status
        this.medications = new ArrayList<>();
        this.consultationNotes = "";
        this.serviceProvided = "";
        this.appointmentOutcomeDate = null; // No outcome record initially
    }

    // Getters and Setters
    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
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

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    public void setAppointmentDateTime(LocalDateTime appointmentDateTime) {
        this.appointmentDateTime = appointmentDateTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    public List<Medication> getMedications() {
        return medications;
    }

    public void addMedication(Medication medication) {
        medications.add(medication);
    }

    public String getConsultationNotes() {
        return consultationNotes;
    }

    public void setConsultationNotes(String consultationNotes) {
        this.consultationNotes = consultationNotes;
    }

    public String getServiceProvided() {
        return serviceProvided;
    }

    public void setServiceProvided(String serviceProvided) {
        this.serviceProvided = serviceProvided;
    }

    public LocalDateTime getAppointmentOutcomeDate() {
        return appointmentOutcomeDate;
    }

    public void setAppointmentOutcomeDate(LocalDateTime appointmentOutcomeDate) {
        this.appointmentOutcomeDate = appointmentOutcomeDate;
    }
}
