// File: views/MedicalRecordView.java
package views;

import interfaces.iPatientView;
import java.util.List;
import models.Appointment;
import models.Patient;
import services.AppointmentService;

/**
 * MedicalRecordView provides the functionality to display a patient's medical records.
 * This includes details of completed appointments, consultation notes, and prescribed medications.
 */
public class MedicalRecordView implements iPatientView {

    private final AppointmentService appointmentService;

    /**
     * Constructor for initializing the MedicalRecordView with an AppointmentService.
     *
     * @param appointmentService The service for managing and fetching appointments.
     */
    public MedicalRecordView(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * Displays the details of a patient including ID, name, date of birth, gender, blood type, and contact information.
     *
     * @param patient The patient whose details need to be displayed.
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
     * Displays a success message to the user.
     *
     * @param message The success message to display.
     */
    @Override
    public void showSuccessMessage(String message) {
        System.out.println("SUCCESS: " + message);
    }

    /**
     * Displays an error message to the user.
     *
     * @param message The error message to display.
     */
    @Override
    public void showErrorMessage(String message) {
        System.out.println("ERROR: " + message);
    }

    /**
     * Displays the medical records of a patient by listing their completed appointments.
     * Includes details of the service provided, consultation notes, and prescribed medications.
     *
     * @param patient The patient whose medical records need to be displayed.
     */
    @Override
    public void display(Patient patient) {
        System.out.println("Displaying medical records for Patient ID: " + patient.getHospitalID());

        // Get the list of all scheduled appointments from the AppointmentService
        List<Appointment> appointments = appointmentService.viewScheduledAppointments();

        // Filter and display appointments that are completed
        boolean found = false;
        for (Appointment appointment : appointments) {
            if (appointment.getPatientId().equals(patient.getHospitalID()) && appointment.getStatus() == enums.AppointmentStatus.COMPLETED) {
                found = true;
                System.out.println("Appointment ID: " + appointment.getAppointmentId());
                System.out.println("Doctor ID: " + appointment.getDoctorId());
                System.out.println("Date & Time: " + appointment.getAppointmentDateTime());
                System.out.println("Service Provided: " + appointment.getServiceProvided());
                System.out.println("Consultation Notes: " + appointment.getConsultationNotes());
                System.out.println("Medications Prescribed:");
                appointment.getMedications().forEach(medication -> {
                    System.out.println(" - Medication: " + medication.getName() +
                                       ", Quantity: " + medication.getQuantity() +
                                       ", Status: " + medication.getStatus());
                });
                System.out.println("------------------------");
            }
        }

        if (!found) {
            System.out.println("No medical records found for Patient ID: " + patient.getHospitalID());
        }
    }
}
