// src/models/Appointment.java
package models;

import java.time.LocalDateTime;
import enums.AppointmentStatus;
import java.util.ArrayList;
import java.util.List;

import enums.MedicationStatus;

public class Appointment {
    private final String appointmentId;
    private final String patientId;
    private final String doctorId;
    private final LocalDateTime appointmentDateTime;
    private AppointmentStatus status;
    private String consultationNotes;
    private String serviceProvided;
    private final List<Medication> medications;

    public Appointment(String appointmentId, String patientId, String doctorId, LocalDateTime appointmentDateTime) {
        this.appointmentId = appointmentId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentDateTime = appointmentDateTime;
        this.status = AppointmentStatus.PENDING;
        this.consultationNotes = "";
        this.serviceProvided = "";
        this.medications = new ArrayList<>();
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public String getPatientId() {
        return patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
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

    public static Appointment fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length < 7) {
            return null;
        }

        String appointmentId = parts[0];
        String patientId = parts[1];
        String doctorId = parts[2];
        LocalDateTime appointmentDateTime = LocalDateTime.parse(parts[3]);
        AppointmentStatus status = AppointmentStatus.valueOf(parts[4]);
        String consultationNotes = parts[5];
        String serviceProvided = parts[6];

        Appointment appointment = new Appointment(appointmentId, patientId, doctorId, appointmentDateTime);
        appointment.setStatus(status);
        appointment.setConsultationNotes(consultationNotes);
        appointment.setServiceProvided(serviceProvided);

        if (parts.length > 7) {
            String[] meds = parts[7].split(";");
            for (String med : meds) {
                String[] medParts = med.split(":");
                if (medParts.length == 3) {
                    String medicationName = medParts[0];
                    int quantity = Integer.parseInt(medParts[1]);
                    MedicationStatus medicationStatus = MedicationStatus.valueOf(medParts[2]);
                    Medication medication = new Medication(medicationName, quantity, medicationStatus);
                    appointment.addMedication(medication);
                }
            }
        }

        return appointment;
    }

    @Override
    public String toString() {
        StringBuilder medicationInfo = new StringBuilder();
        for (Medication medication : medications) {
            medicationInfo.append(medication.toString()).append(";");
        }
        return appointmentId + "," + patientId + "," + doctorId + "," +
                appointmentDateTime + "," + status + "," +
                consultationNotes + "," + serviceProvided + "," + medicationInfo;
    }
}
