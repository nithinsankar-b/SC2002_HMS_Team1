package controllers;

import models.Patient;
import services.PatientService;
import views.IPatientView;
import java.util.Map;

public class PatientController {
    private final PatientService patientService;
    private final IPatientView patientView;

    public PatientController(PatientService patientService, IPatientView patientView) {
        this.patientService = patientService;
        this.patientView = patientView;
    }

    // Entry method to start patient-related options
    public void start() {
        // Implementation to display options and call appropriate methods
    }

    // Method to view allocated appointments
    public void viewAllocatedAppointment(IPatientView patientView) {
        // Implementation to interact with appointment data and display via patientView
    }

    // Method to view available appointments for booking
    public void viewAvailableAppointment(IPatientView patientView) {
        // Implementation to display available appointments
    }

    // Method to register a new patient
    public void registerPatient(Map<String, String> patientData) {
        // Extract data from the map and create a new Patient object
        String patientID = patientData.get("patientID");
        String password = patientData.get("password");
        String name = patientData.get("name");
        LocalDate dob = LocalDate.parse(patientData.get("dob")); // Make sure date format matches
        String gender = patientData.get("gender");
        String bloodType = patientData.get("bloodType");
        String contactInformation = patientData.get("contactInformation");

        // Create and register the patient
        Patient newPatient = new Patient(patientID, password, name, dob, gender, bloodType, contactInformation);
        patientService.addPatient(newPatient); // Assuming addPatient method exists in PatientService
        patientView.showSuccessMessage("Patient registered successfully.");
    }

    // Method to view patient details
    public void viewPatientDetails(String patientID) {
        Patient patient = patientService.getPatientById(patientID);
        if (patient != null) {
            patientView.displayPatientDetails(patient);
        } else {
            patientView.showErrorMessage("Patient not found.");
        }
    }

    // Method to update patient information
    public void updatePatientInfo(String patientID, Map<String, String> updatedData) {
        Patient patient = patientService.getPatientById(patientID);
        if (patient != null) {
            if (updatedData.containsKey("contactInformation")) {
                patient.setContactInformation(updatedData.get("contactInformation"));
            }
            // Add more fields as needed based on the map contents
            patientView.showSuccessMessage("Patient information updated successfully.");
        } else {
            patientView.showErrorMessage("Patient not found.");
        }
    }

    // Method to delete a patient
    public void deletePatient(String patientID) {
        boolean success = patientService.deletePatient(patientID); // Assuming deletePatient method exists
        if (success) {
            patientView.showSuccessMessage("Patient deleted successfully.");
        } else {
            patientView.showErrorMessage("Patient not found.");
        }
    }

    // Method to view all patients
    public void viewAllPatients() {
        patientService.listAllPatients(); // Assuming this method prints all patient info
    }
}