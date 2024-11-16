package views;

import interfaces.iPatientView;

import java.time.format.DateTimeFormatter;
import java.util.List;
import models.Appointment;
import models.Patient;
import services.AppointmentService;

/**
 * View class to display the appointment history of a patient.
 * Implements the iPatientView interface to handle patient-specific operations.
 */
public class AppointmentHistoryView implements iPatientView {
    private final AppointmentService appointmentService;

    /**
     * Constructor for AppointmentHistoryView.
     *
     * @param appointmentService the AppointmentService instance used to fetch appointment data.
     */
    public AppointmentHistoryView(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * Displays the details of the patient.
     *
     * @param patient the Patient object whose details are to be displayed.
     */
    @Override
    public void showPatientDetails(Patient patient) {
        System.out.println("Patient ID: " + patient.getHospitalID());
        System.out.println("Name: " + patient.getName());
        System.out.println("Date of Birth: " + patient.getDateOfBirth());
        System.out.println("Gender: " + patient.getGender());
        System.out.println("Blood Type: " + patient.getBloodType());
        System.out.println("Contact Information: " + patient.getContactInformation());
    }

    /**
     * Displays a success message.
     *
     * @param message the success message to display.
     */
    @Override
    public void showSuccessMessage(String message) {
        System.out.println("SUCCESS: " + message);
    }

    /**
     * Displays an error message.
     *
     * @param message the error message to display.
     */
    @Override
    public void showErrorMessage(String message) {
        System.out.println("ERROR: " + message);
    }

    /**
     * Displays the appointment history of the patient.
     *
     * @param patient the Patient object whose appointment history is to be displayed.
     */
    @Override
    public void display(Patient patient) {
        // Get the list of all scheduled appointments from the AppointmentService
        List<Appointment> appointments = appointmentService.viewScheduledAppointments();

        // Define a DateTimeFormatter for a 24-hour format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

        // Filter appointments that belong to the patient
        boolean found = false;
        for (Appointment appointment : appointments) {
            if (appointment.getPatientId().equals(patient.getHospitalID())) {
                found = true;
                System.out.println("Appointment ID: " + appointment.getAppointmentId());
                System.out.println("Doctor ID: " + appointment.getDoctorId());

                // Format the appointment date and time
                String formattedDateTime = appointment.getAppointmentDateTime().format(formatter) + " HRS";
                System.out.println("Date & Time: " + formattedDateTime);

                System.out.println("Status: " + appointment.getStatus());

                // Display additional details for completed appointments
                if (appointment.getStatus() == enums.AppointmentStatus.COMPLETED) {
                    System.out.println("Service Provided: " + appointment.getServiceProvided());
                    System.out.println("Consultation Notes: " + appointment.getConsultationNotes());
                }

                System.out.println("------------------------");
            }
        }

        if (!found) {
            System.out.println("No appointment history found for Patient ID: " + patient.getHospitalID());
        }
    }
}
