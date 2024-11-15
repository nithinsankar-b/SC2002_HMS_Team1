package interfaces;

import models.Pharmacist;
import models.Appointment;

import java.util.List;

public interface IPharmacistService {

    /**
     * Adds a new pharmacist to the system.
     *
     * @param pharmacist the pharmacist to add
     */
    void addPharmacist(Pharmacist pharmacist);

    /**
     * Retrieves a pharmacist by their hospital ID.
     *
     * @param hospitalID the hospital ID of the pharmacist
     * @return the pharmacist with the specified hospital ID, or {@code null} if not found
     */
    Pharmacist getPharmacistById(String hospitalID);

    /**
     * Updates the contact information of a pharmacist.
     *
     * @param hospitalID the hospital ID of the pharmacist
     * @param newContactInformation the new contact information to set
     * @return {@code true} if the update was successful, {@code false} otherwise
     */
    boolean updatePharmacistContact(String hospitalID, String newContactInformation);

    /**
     * Lists all pharmacists in the system.
     */
    void listAllPharmacists();

    /**
     * Loads pharmacists from a CSV file.
     */
    void loadPharmacistsFromCSV();

    /**
     * Saves the current list of pharmacists to a CSV file.
     */
    void savePharmacistsToCSV();

    /**
     * Retrieves appointments for a given patient ID.
     *
     * @param patientId the ID of the patient
     * @return a list of appointments for the specified patient
     */
    List<Appointment> getAppointmentsByPatientId(String patientId);

    /**
     * Updates the prescription status for a given appointment and updates inventory accordingly.
     *
     * @param appointmentID the ID of the appointment to update
     * @return {@code true} if the update was successful, {@code false} otherwise
     */
    boolean updatePrescriptionStatus(String appointmentID);
}
