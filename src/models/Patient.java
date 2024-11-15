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

// Inherits User class
public class Patient extends User {
    private String name;
    private LocalDate dateOfBirth;
    private String gender;
    private String bloodType;
    private String contactInformation; // Email address
    private Boolean isRegistered;

    public Patient(User user, String name, LocalDate dateOfBirth, String gender, String bloodType, String contactInformation) {
        super(user.getHospitalID(), user.getPassword(), UserRole.PATIENT); // Initialize with User data and set role to PATIENT
        this.name = name;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.bloodType = bloodType;
        this.contactInformation = contactInformation;
    }

    // Getters
    public String getName() {
        return name;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public String getBloodType() {
        return bloodType;
    }

    public String getContactInformation() {
        return contactInformation; // Get email address
    }

    public Boolean getRegistrationStatus() {
        return isRegistered;
    }

    // Setters
    public void setName(String name) {
        this.name = name;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public void setContactInformation(String contactInformation) {
        this.contactInformation = contactInformation; // Set email address
    }

    public void setRegistrationStatus() {
        this.isRegistered = true;
    }

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