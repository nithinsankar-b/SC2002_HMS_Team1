package interfaces;

import models.Patient;

/**
 * Interface for defining the patient view operations.
 * This interface provides methods for displaying patient details,
 * messages, and the main content of the view, such as appointments,
 * appointment history, or medical records.
 */
public interface iPatientView {

    /**
     * Displays the details of a specific patient.
     *
     * @param patient The {@link Patient} object containing the patient's details.
     */
    void showPatientDetails(Patient patient);

    /**
     * Displays a success message to the user.
     *
     * @param message The message to be displayed.
     */
    void showSuccessMessage(String message);

    /**
     * Displays an error message to the user.
     *
     * @param message The message to be displayed.
     */
    void showErrorMessage(String message);

    /**
     * Displays the main content of the view based on the patient.
     * The content could include appointments, appointment history,
     * or medical records associated with the patient.
     *
     * @param patient The {@link Patient} object representing the patient for whom the view is displayed.
     */
    void display(Patient patient);
}

