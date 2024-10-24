package views;

import interfaces.iPatientView;

import java.time.format.DateTimeFormatter;
import java.util.List;
import models.Appointment;
import models.Patient;
import services.AppointmentService;

public class AllocatedAppointmentView implements iPatientView {
    private final AppointmentService appointmentService;

    // Constructor
    public AllocatedAppointmentView(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Override
    public void showPatientDetails(Patient patient) {
        System.out.println("Patient ID: " + patient.getHospitalID());
        System.out.println("Name: " + patient.getName());
        System.out.println("Date of Birth: " + patient.getDateOfBirth());
        System.out.println("Gender: " + patient.getGender());
        System.out.println("Blood Type: " + patient.getBloodType());
        System.out.println("Contact Information: " + patient.getContactInformation());
    }

    @Override
    public void showSuccessMessage(String message) {
        System.out.println("SUCCESS: " + message);
    }

    @Override
    public void showErrorMessage(String message) {
        System.out.println("ERROR: " + message);
    }

    // Display method
    @Override
    public void display(Patient patient) {
        System.out.println("Displaying allocated appointments for Patient ID: " + patient.getHospitalID());

        // Get the list of all scheduled appointments from the AppointmentService
        List<Appointment> appointments = appointmentService.viewScheduledAppointments();

        // Define a DateTimeFormatter for a 24-hour format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");

        // Filter appointments allocated to this patient and display them
        boolean found = false;
        for (Appointment appointment : appointments) {
            // Check if the appointment is allocated to the patient and is pending
            if (appointment.getPatientId().equals(patient.getHospitalID()) && appointment.getStatus() == enums.AppointmentStatus.PENDING) {
                found = true;
                System.out.println("Appointment ID: " + appointment.getAppointmentId());
                System.out.println("Doctor ID: " + appointment.getDoctorId());

                // Format the appointment date and time for better readability
                String formattedDateTime = appointment.getAppointmentDateTime().format(formatter);
                System.out.print("Date & Time: " + formattedDateTime);
                System.out.println(" HRS");

                System.out.println("Status: " + appointment.getStatus());
                System.out.println("------------------------");
            }
        }

        if (!found) {
            System.out.println("No allocated appointments found for Patient ID: " + patient.getHospitalID());
        }
    }
}
