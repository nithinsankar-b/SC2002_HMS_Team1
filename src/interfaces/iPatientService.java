package interfaces;

import java.time.LocalDateTime;
import models.Appointment;
import models.Patient;

/**
 * Interface for managing patient-related operations in the system.
 * This interface defines methods for retrieving patient information,
 * managing appointments, and updating contact information.
 */
public interface iPatientService {

    /**
     * Retrieves the details of a patient using their hospital ID.
     *
     * @param hospitalID The unique identifier of the patient.
     * @return The patient object corresponding to the provided ID, or {@code null} if no patient is found.
     */
    Patient getPatientById(String hospitalID);

    /**
     * Updates the contact information of a specified patient.
     *
     * @param hospitalID           The unique identifier of the patient.
     * @param newContactInformation The new contact information to be updated (e.g., phone number or email).
     * @return {@code true} if the contact information was successfully updated, {@code false} otherwise.
     */
    boolean updatePatientContact(String hospitalID, String newContactInformation);

    /**
     * Lists all patients in the system.
     * This method retrieves and displays the details of all registered patients.
     */
    void listAllPatients();

    /**
     * Retrieves all appointments for a specified patient.
     *
     * @param patientID The unique identifier of the patient.
     * @return An array of {@link Appointment} objects for the specified patient, or an empty array if no appointments are found.
     */
    Appointment[] getPatientAppointments(String patientID);

    /**
     * Creates a new appointment for a patient with a specified doctor.
     *
     * @param patientID       The unique identifier of the patient.
     * @param doctorID        The unique identifier of the doctor.
     * @param appointmentDate The date and time of the appointment.
     * @return {@code true} if the appointment was successfully created, {@code false} otherwise.
     */
    boolean createAppointment(String patientID, String doctorID, LocalDateTime appointmentDate);

    /**
     * Cancels an existing appointment for a patient.
     *
     * @param patientID    The unique identifier of the patient.
     * @param appointmentID The unique identifier of the appointment to be canceled.
     * @return {@code true} if the appointment was successfully canceled, {@code false} otherwise.
     */
    boolean cancelAppointment(String patientID, String appointmentID);

    /**
     * Reschedules an existing appointment for a patient.
     *
     * @param patientID    The unique identifier of the patient.
     * @param appointmentID The unique identifier of the appointment to be rescheduled.
     * @param newDate       The new date and time for the appointment.
     * @return {@code true} if the appointment was successfully rescheduled, {@code false} otherwise.
     */
    boolean rescheduleAppointment(String patientID, String appointmentID, LocalDateTime newDate);
}

