package interfaces;

import models.Doctor;

public interface IDoctorService {
	Doctor getDoctorById(String hospitalID);

    boolean updateDoctorContact(String hospitalID, String newContactInformation);

    void listAllDoctors();

    void acceptanceOfRequest(String requestId);

    void declineRequest(String requestId);

    boolean setAvailablity(Doctor doctor, String dateStr, String timeSlotStr);

    boolean setUnavailability(Doctor doctor, String dateStr, String timeSlotStr);

    void updatePatientDiagnosis(String patientID, String newDiagnosis);

    void updatePatientPrescription(String patientID, String newPrescription);

    void recordAppointmentOutcome(String appointmentId, String serviceProvided, String medicationsInput, String quantitiesInput, String consultationNotes);
}
