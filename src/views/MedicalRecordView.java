// File: views/MedicalRecordView.java
package views;

import interfaces.iPatientView;
import java.util.List;
import models.Appointment;
import models.Patient;
import services.AppointmentService;

public class MedicalRecordView implements iPatientView {
    private final AppointmentService appointmentService;

    // Constructor
    public MedicalRecordView(AppointmentService appointmentService) {
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

    @Override
    public void display(String patientID) {
        System.out.println("Displaying medical records for Patient ID: " + patientID);

        // Get the list of all scheduled appointments from AppointmentService
        List<Appointment> appointments = appointmentService.viewScheduledAppointments();

        // Filter appointments that belong to the patient and have been completed
        boolean found = false;
        for (Appointment appointment : appointments) {
            if (appointment.getPatientId().equals(patientID) && appointment.getStatus() == enums.AppointmentStatus.COMPLETED) {
                found = true;
                System.out.println("Appointment ID: " + appointment.getAppointmentId());
                System.out.println("Doctor ID: " + appointment.getDoctorId());
                System.out.println("Date & Time: " + appointment.getAppointmentDateTime());
                System.out.println("Service Provided: " + appointment.getServiceProvided());
                System.out.println("Consultation Notes: " + appointment.getConsultationNotes());
                System.out.println("Medications Prescribed:");
                appointment.getMedications().forEach(medication -> {
                    System.out.println(" - Medication: " + medication.getName() + ", Quantity: " + medication.getQuantity() + ", Status: " + medication.getStatus());
                });
                System.out.println("------------------------");
            }
        }

        if (!found) {
            System.out.println("No medical records found for Patient ID: " + patientID);
        }
    }
}