package interfaces;

import models.Doctor;

/**
 * Interface for doctor-related service operations.
 * Provides methods to manage doctor details, handle appointment requests, manage availability,
 * and update patient records such as diagnoses and prescriptions.
 */
public interface IDoctorService {

    /**
     * Retrieves the details of a doctor by their hospital ID.
     *
     * @param hospitalID The unique ID of the doctor.
     * @return The Doctor object if found, or null if no doctor exists with the given ID.
     */
    Doctor getDoctorById(String hospitalID);

    /**
     * Updates the contact information of a doctor.
     *
     * @param hospitalID The unique ID of the doctor.
     * @param newContactInformation The new contact information to be updated.
     * @return true if the contact information was successfully updated, false otherwise.
     */
    boolean updateDoctorContact(String hospitalID, String newContactInformation);

    /**
     * Lists all the doctors in the system.
     * This typically displays the details of all doctors.
     */
    void listAllDoctors();

    /**
     * Accepts a pending appointment request.
     *
     * @param requestId The ID of the appointment request to be accepted.
     */
    void acceptanceOfRequest(String requestId);

    /**
     * Declines a pending appointment request.
     *
     * @param requestId The ID of the appointment request to be declined.
     */
    void declineRequest(String requestId);

    /**
     * Sets the availability of a doctor for a specific date and time slot.
     *
     * @param doctor The Doctor object whose availability is being updated.
     * @param dateStr The date for which availability is being set, in the format "yyyy-MM-dd".
     * @param timeSlotStr The time slot for which availability is being set, in the format "HH:mm".
     * @return true if availability was successfully updated, false otherwise.
     */
    boolean setAvailablity(Doctor doctor, String dateStr, String timeSlotStr);

    /**
     * Sets the unavailability of a doctor for a specific date and time slot.
     *
     * @param doctor The Doctor object whose availability is being updated.
     * @param dateStr The date for which unavailability is being set, in the format "yyyy-MM-dd".
     * @param timeSlotStr The time slot for which unavailability is being set, in the format "HH:mm".
     * @return true if unavailability was successfully updated, false otherwise.
     */
    boolean setUnavailability(Doctor doctor, String dateStr, String timeSlotStr);

    /**
     * Updates a patient's diagnosis in the medical records.
     *
     * @param patientID The unique ID of the patient.
     * @param newDiagnosis The new diagnosis to be added to the patient's record.
     */
    void updatePatientDiagnosis(String patientID, String newDiagnosis);

    /**
     * Updates a patient's prescription in the medical records.
     *
     * @param patientID The unique ID of the patient.
     * @param newPrescription The new prescription to be added to the patient's record.
     */
    void updatePatientPrescription(String patientID, String newPrescription);

    /**
     * Records the outcome of a completed appointment, including services provided,
     * prescribed medications, and consultation notes.
     *
     * @param appointmentId The ID of the appointment whose outcome is being recorded.
     * @param serviceProvided A description of the service provided during the appointment.
     * @param medicationsInput A list of medications prescribed during the appointment, as a string.
     * @param quantitiesInput A list of quantities corresponding to the prescribed medications, as a string.
     * @param consultationNotes Notes recorded during the consultation.
     */
    void recordAppointmentOutcome(String appointmentId, String serviceProvided, String medicationsInput, String quantitiesInput, String consultationNotes);
}

