package views;

import interfaces.iPatientView;

import java.time.format.DateTimeFormatter;
import java.util.List;
import models.Appointment;
import models.Patient;
import services.AppointmentService;

public class AppointmentHistoryView implements iPatientView {
    private final AppointmentService appointmentService;

    // Constructor
    public AppointmentHistoryView(AppointmentService appointmentService) {
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

    // Display method with formatted date and time, including "HRS"
    @Override
    public void display(Patient patient) {
        //System.out.println("Displaying appointment history for Patient ID: " + patient.getHospitalID());

        // Get the list of all scheduled appointments from AppointmentService
        List<Appointment> appointments = appointmentService.viewScheduledAppointments();

        // Define a DateTimeFormatter for a 24-hour format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm"); // Example: 24 Oct 2024, 14:30

        // Filter appointments that belong to the patient and are in any status (completed, cancelled, rescheduled)
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