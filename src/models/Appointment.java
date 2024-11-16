package models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import enums.AppointmentStatus;
import enums.MedicationStatus;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Medication;

/**
 * Represents an appointment in the healthcare system.
 * Stores details such as patient ID, doctor ID, appointment date and time, status,
 * consultation notes, service provided, and prescribed medications.
 */
public class Appointment {

    /** The file path for the CSV file containing appointment data. */
    private static final String APPOINTMENT_FILE = "data/Appointment.csv";

    /** Cache for storing appointments to reduce redundant file access. */
    private static Map<String, Appointment> appointmentCache = new HashMap<>();

    /** Unique identifier for the appointment. */
    private final String appointmentId;

    /** ID of the patient associated with the appointment. */
    private final String patientId;

    /** ID of the doctor associated with the appointment. */
    private final String doctorId;

    /** Date and time of the appointment. */
    private final LocalDateTime appointmentDateTime;

    /** Current status of the appointment (e.g., PENDING, COMPLETED). */
    private AppointmentStatus status;

    /** Notes recorded during the consultation. */
    private String consultationNotes;

    /** Description of the service provided during the appointment. */
    private String serviceProvided;

    /** List of medications prescribed during the appointment. */
    private List<Medication> medications;

    /** List of quantities corresponding to the prescribed medications. */
    private List<Integer> quantities;

    /** Status of the prescribed medications (e.g., PENDING, DISPENSED). */
    private MedicationStatus medicationStatus;

    /**
     * Constructs an {@code Appointment} with the specified details.
     *
     * @param appointmentId        The unique identifier for the appointment.
     * @param patientId            The ID of the patient associated with the appointment.
     * @param doctorId             The ID of the doctor associated with the appointment.
     * @param appointmentDateTime  The date and time of the appointment.
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
        this.quantities = new ArrayList<>();
        this.medicationStatus = MedicationStatus.PENDING;
    }

    /**
     * Retrieves the unique identifier for the appointment.
     *
     * @return The appointment ID.
     */
    public String getAppointmentId() {
        return appointmentId;
    }

    /**
     * Retrieves the ID of the patient associated with the appointment.
     *
     * @return The patient ID.
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Retrieves the ID of the doctor associated with the appointment.
     *
     * @return The doctor ID.
     */
    public String getDoctorId() {
        return doctorId;
    }

    /**
     * Retrieves the date and time of the appointment.
     *
     * @return The appointment date and time.
     */
    public LocalDateTime getAppointmentDateTime() {
        return appointmentDateTime;
    }

    /**
     * Retrieves the current status of the appointment.
     *
     * @return The status of the appointment.
     */
    public AppointmentStatus getStatus() {
        return status;
    }

    /**
     * Updates the status of the appointment.
     *
     * @param status The new status of the appointment.
     */
    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    /**
     * Retrieves the list of medications prescribed during the appointment.
     *
     * @return A list of prescribed medications.
     */
    public List<Medication> getMedications() {
        return medications;
    }

    /**
     * Retrieves the list of quantities corresponding to each medication.
     *
     * @return A list of medication quantities.
     */
    public List<Integer> getQuantities() {
        return quantities;
    }

    /**
     * Adds a prescribed medication and its corresponding quantity to the appointment.
     *
     * @param medication The medication to add.
     * @param quantity   The quantity of the medication prescribed.
     */
    public void addMedication(Medication medication, int quantity) {
        medications.add(medication);
        quantities.add(quantity);
    }

    /**
     * Retrieves the consultation notes recorded during the appointment.
     *
     * @return The consultation notes.
     */
    public String getConsultationNotes() {
        return consultationNotes;
    }

    /**
     * Updates the consultation notes for the appointment.
     *
     * @param consultationNotes The new consultation notes.
     */
    public void setConsultationNotes(String consultationNotes) {
        this.consultationNotes = consultationNotes;
    }

    /**
     * Retrieves the description of the service provided during the appointment.
     *
     * @return The service description.
     */
    public String getServiceProvided() {
        return serviceProvided;
    }

    /**
     * Updates the description of the service provided during the appointment.
     *
     * @param serviceProvided The new service description.
     */
    public void setServiceProvided(String serviceProvided) {
        this.serviceProvided = serviceProvided;
    }

    /**
     * Retrieves the status of the prescribed medications.
     *
     * @return The medication status.
     */
    public MedicationStatus getMedicationStatus() {
        return medicationStatus;
    }

    /**
     * Updates the status of the prescribed medications.
     *
     * @param medicationStatus The new medication status.
     */
    public void setMedicationStatus(MedicationStatus medicationStatus) {
        this.medicationStatus = medicationStatus;
    }



    /**
 * Creates an {@code Appointment} instance from a CSV string line.
 * Parses the given string to extract appointment details such as appointment ID,
 * patient ID, doctor ID, appointment date and time, status, consultation notes,
 * service provided, prescribed medications, and their quantities.
 *
 * @param line The CSV string containing appointment details, where fields are
 *             separated by commas. Expected fields include:
 *             <ol>
 *                 <li>Appointment ID</li>
 *                 <li>Patient ID</li>
 *                 <li>Doctor ID</li>
 *                 <li>Appointment Date and Time</li>
 *                 <li>Appointment Status</li>
 *                 <li>Consultation Notes</li>
 *                 <li>Service Provided</li>
 *                 <li>Medications (semicolon-separated)</li>
 *                 <li>Quantities (semicolon-separated)</li>
 *                 <li>Medication Status</li>
 *             </ol>
 * 
 * @return An {@code Appointment} object created from the parsed details.
 *         Returns {@code null} if the input string does not contain the required fields.
 * 
 * @throws DateTimeParseException If the appointment date and time format is invalid.
 * @throws IllegalArgumentException If the appointment status or medication status is invalid.
 */
public static Appointment fromString(String line) {
    // Split the CSV string into fields
    String[] parts = line.split(",");
    if (parts.length < 10) {
        return null; // Ensure that there are enough fields
    }

    // Parse appointment details
    String appointmentId = parts[0].replace("\"", "").trim();
    String patientId = parts[1].trim();
    String doctorId = parts[2].trim();
    LocalDateTime appointmentDateTime = LocalDateTime.parse(parts[3].trim());
    AppointmentStatus status = AppointmentStatus.valueOf(parts[4].trim());
    String consultationNotes = parts[5].trim();
    String serviceProvided = parts[6].trim();

    // Parse medications and quantities, assuming they are separated by semicolons
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
            appointment.addMedication(med, quantity); // Add medication and quantity
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
        for (int i = 0; i < medications.size() && i < quantities.size(); i++) {
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
    return "\"" + appointmentId + "\"" + "," + patientId + "," + doctorId + "," +
            appointmentDateTime + "," + status + "," +
            consultationNotes + "," + serviceProvided + "," +
            medicationsStr.toString() + "," + quantitiesStr.toString() + "," + medicationStatus;
}

/**
 * Sets the list of prescribed medications for the appointment.
 *
 * @param prescribedMedications A list of {@code Medication} objects representing
 *                              the prescribed medications for this appointment.
 */
public void setMedications(List<models.Medication> prescribedMedications) {
    medications = prescribedMedications;
}

/**
 * Sets the list of prescribed quantities corresponding to each medication.
 *
 * @param prescribedQuantities A list of integers representing the quantities
 *                             prescribed for each medication in the same order.
 */
public void setQuantities(List<Integer> prescribedQuantities) {
    quantities = prescribedQuantities;
}

/**
 * Retrieves an appointment by its ID. Uses a caching mechanism to avoid repeated
 * file reads for the same appointment.
 *
 * @param appointmentId The unique ID of the appointment to retrieve.
 * @return The {@code Appointment} object if found, or {@code null} if the appointment
 *         with the given ID does not exist.
 */
public static Appointment getAppointmentById(String appointmentId) {
    // Check cache first for the appointment
    if (appointmentCache.containsKey(appointmentId)) {
        return appointmentCache.get(appointmentId);
    }

    // Load the appointment from file if not in cache
    try (BufferedReader reader = new BufferedReader(new FileReader(APPOINTMENT_FILE))) {
        String line;
        reader.readLine(); // Skip header line
        while ((line = reader.readLine()) != null) {
            Appointment appointment = Appointment.fromString(line);

            // Check if appointment is successfully created and matches the ID
            if (appointment != null && appointment.getAppointmentId().equals(appointmentId)) {
                appointmentCache.put(appointmentId, appointment); // Cache it for future calls
                return appointment;
            }
        }
    } catch (IOException e) {
        System.err.println("Error loading appointment record: " + e.getMessage());
    }

    System.err.println("Appointment ID " + appointmentId + " not found.");
    return null; // Return null if appointment not found
}
}
