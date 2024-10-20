package views;

import models.Patient;

public class PatientView {

    // Method to display patient details
    public void showPatientDetails(Patient patient) {
        System.out.println("Patient ID: " + patient.getHospitalID());
        System.out.println("Name: " + patient.getName());
        System.out.println("Date of Birth: " + patient.getDateOfBirth());
        System.out.println("Gender: " + patient.getGender());
        System.out.println("Blood Type: " + patient.getBloodType());
        System.out.println("Contact Information: " + patient.getContactInformation());
    }

    // Method to display a success message
    public void showSuccessMessage(String message) {
        System.out.println("SUCCESS: " + message);
    }

    // Method to display an error message
    public void showErrorMessage(String message) {
        System.out.println("ERROR: " + message);
    }
}
