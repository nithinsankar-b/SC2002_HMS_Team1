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
    private List<Medication> medications;
    private List<Integer> quantities; // List of quantities for each medication
    private MedicationStatus medicationStatus;

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
        this.quantities = new ArrayList<>(); // Initialize the quantities list
        this.medicationStatus = MedicationStatus.PENDING;
    }

    // Getters and Setters
    public String getAppointmentId() { return appointmentId; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public LocalDateTime getAppointmentDateTime() { return appointmentDateTime; }
    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }

    public List<Medication> getMedications() { return medications; }
    public List<Integer> getQuantities() { return quantities; }

    public void addMedication(Medication medication, int quantity) {
        medications.add(medication);
        quantities.add(quantity); // Add quantity for the corresponding medication
    }

    public String getConsultationNotes() { return consultationNotes; }
    public void setConsultationNotes(String consultationNotes) { this.consultationNotes = consultationNotes; }

    public String getServiceProvided() { return serviceProvided; }
    public void setServiceProvided(String serviceProvided) { this.serviceProvided = serviceProvided; }

    public MedicationStatus getMedicationStatus() { return medicationStatus; }
    public void setMedicationStatus(MedicationStatus medicationStatus) { this.medicationStatus = medicationStatus; }


    // Static method to create an Appointment from a CSV string line
    public static Appointment fromString(String line) {
        String[] parts = line.split(",");
        if (parts.length < 10) {
            return null; // Ensure that there are enough fields
        }

        String appointmentId = parts[0].replace("\"", "").trim();
        String patientId = parts[1].trim();
        String doctorId = parts[2].trim();
        LocalDateTime appointmentDateTime = LocalDateTime.parse(parts[3].trim());
        AppointmentStatus status = AppointmentStatus.valueOf(parts[4].trim());
        String consultationNotes = parts[5].trim();
        String serviceProvided = parts[6].trim();

        // Parsing medications and quantities, assuming they are separated by semicolons
        String[] medicationParts = parts[7].split(";");
        String[] quantityParts = parts[8].split(";");

        // Initialize the Appointment
        Appointment appointment = new Appointment(appointmentId, patientId, doctorId, appointmentDateTime);
        appointment.setStatus(status);
        appointment.setConsultationNotes(consultationNotes);
        appointment.setServiceProvided(serviceProvided);

        // Add medications and their respective quantities
        for (int i = 0; i < medicationParts.length; i++) {
            String medication = medicationParts[i].trim();
            int quantity = -1; // Use -1 or another sentinel value to indicate an invalid or missing quantity

            try {
                if (i < quantityParts.length && !quantityParts[i].trim().isEmpty()) {
                    quantity = Integer.parseInt(quantityParts[i].trim()); // Parse quantity
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity format for medication: " + medication + ". Defaulting to -1.");
            }

            // Skip medications with invalid quantity or handle them differently
            if (quantity >= 0) {
                // Create a Medication object and add it to the appointment
                Medication med = new Medication(medication, quantity, MedicationStatus.PENDING);
                appointment.addMedication(med, quantity); // Assuming addMedication handles the addition correctly
            } else {
                System.out.println("Skipping medication with invalid quantity: " + medication);
            }
        }

        // Set the medication status
        appointment.setMedicationStatus(MedicationStatus.valueOf(parts[9].trim()));

        return appointment;
    }


    @Override
    public String toString() {
        // Initialize StringBuilder for medications and quantities
        StringBuilder medicationsStr = new StringBuilder();
        StringBuilder quantitiesStr = new StringBuilder();

        // Check if medications and quantities lists are not empty
        if (medications != null && !medications.isEmpty() && quantities != null && !quantities.isEmpty()) {
            // Iterate over the medications and quantities lists
            for (int i = 0; i < medications.size() && i< quantities.size(); i++) {
                // Append medication names and quantities to respective StringBuilder objects
                medicationsStr.append(medications.get(i).getName());
                quantitiesStr.append(quantities.get(i));

                // Add a semicolon if it's not the last element in the list
                if (i < medications.size() - 1) {
                    medicationsStr.append(";");
                    quantitiesStr.append(";");
                }
            }
        } else {
            // If lists are empty, append default values or empty strings
            medicationsStr.append("No medications");
            quantitiesStr.append("0");
        }

        // Return the formatted string with all values
        return "\""+ appointmentId + "\"" + "," + patientId + "," + doctorId + "," +
                appointmentDateTime + "," + status + "," +
                consultationNotes + "," + serviceProvided + "," +
                medicationsStr.toString() + "," + quantitiesStr.toString() + "," + medicationStatus;
    }


    public void setMedications(List<models.Medication> prescribedMedications) {
        medications=prescribedMedications;
    }

    public void setQuantities(List<Integer> prescribedQuantities) {
        quantities=prescribedQuantities;
    }
}
