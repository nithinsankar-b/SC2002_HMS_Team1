package models;

import java.time.LocalDateTime;
import enums.AppointmentStatus;
import enums.MedicationStatus;
import java.util.ArrayList;
import java.util.List;
import models.Medication;

/**
 * Represents an appointment in the hospital management system.
 */
public class Appointment {
    private final String appointmentId;
    private final String patientId;
    private final String doctorId;
    private final LocalDateTime appointmentDateTime;
    private AppointmentStatus status;
    private String consultationNotes;
    private String serviceProvided;
    private final List<Medication> medications;
    private int quantity; // Single quantity for the medication
    private MedicationStatus medicationStatus;
    private String medication;

    /**
     * Constructor to initialize an Appointment object.
     *
     * @param appointmentId        Unique identifier for the appointment
     * @param patientId            Identifier for the patient
     * @param doctorId             Identifier for the doctor
     * @param appointmentDateTime   Date and time of the appointment
     */
    public Appointment(String appointmentId, String patientId, String doctorId, LocalDateTime appointmentDateTime) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDateTime = appointmentDateTime;
        this.status = AppointmentStatus.PENDING;
        this.consultationNotes = "";
        this.serviceProvided = "";
        this.medications = new ArrayList<>();
        this.quantity = 0; // Default quantity
        this.medicationStatus = MedicationStatus.PENDING;
        this.medication = "";
    }

    // Getters and Setters

    /**
     * Gets the appointment ID.
     *
     * @return the appointment ID
     */
    public String getAppointmentId() { return appointmentId; }

    /**
     * Gets the patient ID.
     *
     * @return the patient ID
     */
    public String getPatientId() { return patientId; }

    /**
     * Gets the doctor ID.
     *
     * @return the doctor ID
     */
    public String getDoctorId() { return doctorId; }

    /**
     * Gets the appointment date and time.
     *
     * @return the appointment date and time
     */
    public LocalDateTime getAppointmentDateTime() { return appointmentDateTime; }

    /**
     * Gets the appointment status.
     *
     * @return the appointment status
     */
    public AppointmentStatus getStatus() { return status; }

    /**
     * Sets the appointment status.
     *
     * @param status the new status to set
     */
    public void setStatus(AppointmentStatus status) { this.status = status; }

    /**
     * Gets the list of medications associated with this appointment.
     *
     * @return the list of medications
     */
    public List<Medication> getMedications() { return medications; }

    /**
     * Adds a medication to the appointment.
     *
     * @param medication the medication to add
     */
    public void addMedication(Medication medication) { medications.add(medication); }

    /**
     * Gets the consultation notes.
     *
     * @return the consultation notes
     */
    public String getConsultationNotes() { return consultationNotes; }

    /**
     * Sets the consultation notes.
     *
     * @param consultationNotes the new consultation notes to set
     */
    public void setConsultationNotes(String consultationNotes) { this.consultationNotes = consultationNotes; }

    /**
     * Gets the service provided during the appointment.
     *
     * @return the service provided
     */
    public String getServiceProvided() { return serviceProvided; }

    /**
     * Sets the service provided during the appointment.
     *
     * @param serviceProvided the new service to set
     */
    public void setServiceProvided(String serviceProvided) { this.serviceProvided = serviceProvided; }

    /**
     * Gets the medication status.
     *
     * @return the medication status
     */
    public MedicationStatus getMedicationStatus() { return medicationStatus; }

    /**
     * Sets the medication status.
     *
     * @param status the new medication status to set
     */
    public void setMedicationStatus(MedicationStatus status) { this.medicationStatus = status; }

    /**
     * Gets the medication name.
     *
     * @return the medication name
     */
    public String getMedication() { return medication; }

    /**
     * Sets the medication name.
     *
     * @param medication the new medication name to set
     */
    public void setMedication(String medication) { this.medication = medication; }

    /**
     * Gets the quantity of medication.
     *
     * @return the quantity of medication
     */
    public int getQuantity() { return quantity; }

    /**
     * Sets the quantity of medication.
     *
     * @param quantity the new quantity to set
     */
    public void setQuantity(int quantity) { this.quantity = quantity; }

    /**
     * Creates an Appointment instance from a CSV string line.
     *
     * @param line the CSV string line
     * @return the created Appointment object, or null if invalid
     */
    public static Appointment fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length < 10) {
            return null;
        }

        String appointmentId = parts[0].replace("\"", "").trim();
        String patientId = parts[1].trim();
        String doctorId = parts[2].trim();
        LocalDateTime appointmentDateTime = LocalDateTime.parse(parts[3].trim());
        AppointmentStatus status = AppointmentStatus.valueOf(parts[4].trim());
        String consultationNotes = parts[5].trim();
        String serviceProvided = parts[6].trim();

        String medication = parts[7].trim();
        int quantity = Integer.parseInt(parts[8].trim());
        MedicationStatus medicationStatus = MedicationStatus.valueOf(parts[9].trim());

        Appointment appointment = new Appointment(appointmentId, patientId, doctorId, appointmentDateTime);
        appointment.setStatus(status);
        appointment.setConsultationNotes(consultationNotes);
        appointment.setServiceProvided(serviceProvided);
        appointment.setQuantity(quantity);
        appointment.setMedication(medication);
        appointment.setMedicationStatus(medicationStatus);

        return appointment;
    }

    /**
     * Converts the Appointment instance to a CSV string representation.
     *
     * @return the CSV string representation of the appointment
     */
    @Override
    public String toString() {
        return appointmentId + "," + patientId + "," + doctorId + "," +
                appointmentDateTime + "," + status + "," +
                consultationNotes + "," + serviceProvided + "," +
                medication + "," + quantity + "," + medicationStatus;
    }
}
