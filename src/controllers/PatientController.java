package controllers;

import models.Patient;
import services.PatientService;
import views.PatientView;

public class PatientController {
    private final PatientService patientService;
    private final PatientView patientView;

    public PatientController(PatientService patientService, PatientView patientView) {
        this.patientService = patientService;
        this.patientView = patientView;
    }

    // Method to display patient details
    public void displayPatientDetails(String hospitalID) {
        Patient patient = patientService.getPatientById(hospitalID);
        if (patient != null) {
            patientView.showPatientDetails(patient);
        } else {
            patientView.showErrorMessage("Patient not found!");
        }
    }

    // Method to update patient contact information
    public void updatePatientContact(String hospitalID, String newContact) {
        boolean isUpdated = patientService.updatePatientContact(hospitalID, newContact);
        if (isUpdated) {
            patientView.showSuccessMessage("Contact information updated successfully!");
        } else {
            patientView.showErrorMessage("Failed to update contact information. Patient not found!");
        }
    }
}
