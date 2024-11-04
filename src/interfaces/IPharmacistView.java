package interfaces;

import models.Appointment;
import models.Pharmacist;
import enums.MedicationStatus;
import models.Patient;
import java.util.List;

/**
 * Interface for pharmacist view operations.
 * This interface defines methods for displaying various information related to patients and appointments.
 */
public interface IPharmacistView {

    /**
     * Displays the details of a specified patient.
     *
     * @param patient The patient whose details are to be displayed.
     */
    public void showPatientDetails(Patient patient);

    /**
     * Displays a success message to the user.
     *
     * @param message The success message to be displayed.
     */
    public void showSuccessMessage(String message);

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to be displayed.
     */
    public void showErrorMessage(String message);

    /**
     * Displays the main content of the view based on the provided patient ID.
     *
     * @param PatientID The ID of the patient for whom the main content is to be displayed.
     */
    public void display(String PatientID);
}
