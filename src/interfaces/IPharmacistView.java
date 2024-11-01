package interfaces;

import models.Appointment;

import models.Pharmacist;
import enums.MedicationStatus;
import models.Patient;
import java.util.List;



public interface IPharmacistView{
    
    // Method to display patient details
    public void showPatientDetails(Patient patient);

    // Method to display a success message
    public void showSuccessMessage(String message);

    // Method to display an error message
    public void showErrorMessage(String message);

    // Method to display the main content of the view (Appointment/AppointmentHistory/MedicalRecord)
    public void display(String PatientID);
}