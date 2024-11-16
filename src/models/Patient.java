package models;

import enums.UserRole;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import models.User;
import models.Appointment;
import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;

/**
 * The `Patient` class represents a patient in the hospital management system.
 * It extends the `User` class, inheriting user-related attributes and behaviors,
 * and adds patient-specific properties such as name, date of birth, gender, blood type,
 * contact information, and registration status.
 */
public class Patient extends User {
    private String name;
    private LocalDate dateOfBirth;
    private String gender;
    private String bloodType;
    private String contactInformation; // Email address
    private Boolean isRegistered;

    /**
     * Constructor to initialize a `Patient` object with user details and patient-specific attributes.
     *
     * @param user              The user information of the patient.
     * @param name              The name of the patient.
     * @param dateOfBirth       The date of birth of the patient.
     * @param gender            The gender of the patient.
     * @param bloodType         The blood type of the patient.
     * @param contactInformation The contact information (email address) of the patient.
     */
    public Patient(User user, String name, LocalDate dateOfBirth, String gender, String bloodType, String contactInformation) {
        super(user.getHospitalID(), user.getPassword(), UserRole.PATIENT); // Initialize with User data and set role to PATIENT
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.bloodType = bloodType;
        this.contactInformation = contactInformation;
    }

    // Getters

    /**
     * Gets the name of the patient.
     *
     * @return The patient's name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the date of birth of the patient.
     *
     * @return The patient's date of birth.
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Gets the gender of the patient.
     *
     * @return The patient's gender.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Gets the blood type of the patient.
     *
     * @return The patient's blood type.
     */
    public String getBloodType() {
        return bloodType;
    }

    /**
     * Gets the contact information (email address) of the patient.
     *
     * @return The patient's contact information.
     */
    public String getContactInformation() {
        return contactInformation;
    }

    /**
     * Gets the registration status of the patient.
     *
     * @return True if the patient is registered, otherwise false.
     */
    public Boolean getRegistrationStatus() {
        return isRegistered;
    }

    // Setters

    /**
     * Sets the name of the patient.
     *
     * @param name The patient's name.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the date of birth of the patient.
     *
     * @param dateOfBirth The patient's date of birth.
     */
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Sets the gender of the patient.
     *
     * @param gender The patient's gender.
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * Sets the blood type of the patient.
     *
     * @param bloodType The patient's blood type.
     */
    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    /**
     * Sets the contact information (email address) of the patient.
     *
     * @param contactInformation The patient's contact information.
     */
    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation;
    }

    /**
     * Sets the registration status of the patient to true.
     */
    public void setRegistrationStatus() {
        this.isRegistered = true;
    }

    /**
     * Retrieves the list of appointments associated with the patient from the appointment file.
     *
     * @return A list of `Appointment` objects for the patient.
     */
    public List<Appointment> getAppointments() {
        List<Appointment> patientAppointments = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("data/Appointment.csv"))) {
            String line;
            reader.readLine();  // Skip header line
            while ((line = reader.readLine()) != null) {
                Appointment appointment = Appointment.fromString(line);

                // Check if the appointment belongs to this patient
                if (appointment != null && appointment.getPatientId().equals(getHospitalID())) {
                    patientAppointments.add(appointment);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading appointments: " + e.getMessage());
        }

        return patientAppointments;  // Return the list of appointments for the patient
    }
}
