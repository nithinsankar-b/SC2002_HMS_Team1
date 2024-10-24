package interfaces;

import java.time.LocalDateTime;
import models.Appointment;
import models.Patient;

public interface iPatientService {
    
    // Retrieve patient information by hospital ID
    Patient getPatientById(String hospitalID);

    // Update patient contact information
    boolean updatePatientContact(String hospitalID, String newContactInformation);

    // List all patients
    void listAllPatients();

    // Retrieve all appointments for a patient
    Appointment[] getPatientAppointments(String patientID);

    // Create a new appointment for a patient
    boolean createAppointment(String patientID, String doctorID, LocalDateTime appointmentDate);

    // Cancel an existing appointment for a patient
    boolean cancelAppointment(String patientID, String appointmentID);

    // Reschedule an existing appointment for a patient
    boolean rescheduleAppointment(String patientID, String appointmentID, LocalDateTime newDate);
}
