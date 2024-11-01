package models;

import java.time.LocalDateTime;
import enums.AppointmentStatus;
import enums.MedicationStatus;
import java.util.ArrayList;
import java.util.List;
import models.Medication;

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

    // Constructor with quantity handling
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
    public String getAppointmentId() { return appointmentId; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public LocalDateTime getAppointmentDateTime() { return appointmentDateTime; }
    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }

    public List<Medication> getMedications() { return medications; }
    public void addMedication(Medication medication) { medications.add(medication); }

    public String getConsultationNotes() { return consultationNotes; }
    public void setConsultationNotes(String consultationNotes) { this.consultationNotes = consultationNotes; }
    
    public String getServiceProvided() { return serviceProvided; }
    public void setServiceProvided(String serviceProvided) { this.serviceProvided = serviceProvided; }
    
    public MedicationStatus getMedicationStatus() { return medicationStatus; }
    public void setMedicationStatus(MedicationStatus status) { this.medicationStatus = status; }
    
    public String getMedication() { return medication; }
    public void setMedication(String medication) { this.medication = medication; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    // Static method to create an Appointment from a CSV string line
    public static Appointment fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length < 9) {
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

    @Override
    public String toString() {
        return appointmentId + "," + patientId + "," + doctorId + "," +
               appointmentDateTime + "," + status + "," +
               consultationNotes + "," + serviceProvided + "," +
                medication + "," + quantity + "," + medicationStatus;
    }
}
