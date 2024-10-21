package interfaces;

import models.Patient;

public interface iPatientView {
    
    // Method to display patient details
    public void showPatientDetails(Patient patient);

    // Method to display a success message
    public void showSuccessMessage(String message);

    // Method to display an error message
    public void showErrorMessage(String message);

    // Method to display the main content of the view (Appointment/AppointmentHistory/MedicalRecord)
    public void display(Patient patient);
}
