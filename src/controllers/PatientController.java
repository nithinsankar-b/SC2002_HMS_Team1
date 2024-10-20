package controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import models.Patient;
import services.AppointmentService;
import services.PatientService;
import views.AllocatedAppointmentView;
import views.AppointmentHistoryView;
import views.MedicalRecordView;

public class PatientController {
    private final PatientService patientService; 
    private final AppointmentService appointmentService;
    private final AllocatedAppointmentView allocatedAppointmentView;
    private final AppointmentHistoryView appointmentHistoryView;
    private final MedicalRecordView medicalRecordView;

    // Constructor
    public PatientController(PatientService patientService, AppointmentService appointmentService) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;

        // Pass the AppointmentService instance to each view
        this.allocatedAppointmentView = new AllocatedAppointmentView(appointmentService);
        this.appointmentHistoryView = new AppointmentHistoryView(appointmentService);
        this.medicalRecordView = new MedicalRecordView(appointmentService);
    }

    // Method to view patient details
    public void viewPatientDetails(Scanner scanner) {
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();
        Patient patient = patientService.getPatientById(patientId);

        if (patient != null) {
            allocatedAppointmentView.showPatientDetails(patient);
        } else {
            allocatedAppointmentView.showErrorMessage("Patient not found.");
        }
    }

    // Method to view allocated appointments
    public void viewAllocatedAppointments(Scanner scanner) {
        System.out.print("Enter Patient ID: ");
        String patientID = scanner.nextLine();
        allocatedAppointmentView.display(patientID);
    }

    // Method to view appointment history
    public void viewAppointmentHistory(Scanner scanner) {
        System.out.print("Enter Patient ID: ");
        String patientID = scanner.nextLine();
        appointmentHistoryView.display(patientID);
    }

    // Method to view medical records
    public void viewMedicalRecords(Scanner scanner) {
        System.out.print("Enter Patient ID: ");
        String patientID = scanner.nextLine();
        medicalRecordView.display(patientID);
    }

    // Method to update contact information
    public void updateContactInformation(Scanner scanner) {
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();
        System.out.print("Enter new contact information: ");
        String newContactInfo = scanner.nextLine();

        boolean success = patientService.updatePatientContact(patientId, newContactInfo);
        if (success) {
            System.out.println("Contact information updated successfully.");
        } else {
            System.out.println("Failed to update contact information. Patient not found.");
        }
    }

    // Method to create an appointment for a patient
    public void createAppointment(Scanner scanner) {
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();
        System.out.print("Enter Doctor ID: ");
        String doctorId = scanner.nextLine();
        System.out.print("Enter appointment date and time (yyyy-MM-ddTHH:mm): ");
        String appointmentDateTime = scanner.nextLine();

        LocalDateTime dateTime = LocalDateTime.parse(appointmentDateTime);

        boolean success = patientService.createAppointment(patientId, doctorId, dateTime);
        if (success) {
            System.out.println("Appointment created successfully.");
        } else {
            System.out.println("Failed to create appointment.");
        }
    }

    // Method to cancel an appointment for a patient
    public void cancelAppointment(Scanner scanner) {
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();
        System.out.print("Enter Appointment ID: ");
        String appointmentId = scanner.nextLine();

        boolean success = patientService.cancelAppointment(patientId, appointmentId);
        if (success) {
            System.out.println("Appointment cancelled successfully.");
        } else {
            System.out.println("Failed to cancel appointment. Please check the details and try again.");
        }
    }

    // Method to reschedule an existing appointment
    public void rescheduleAppointment(Scanner scanner) {
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();
        System.out.print("Enter Appointment ID: ");
        String appointmentId = scanner.nextLine();
        System.out.print("Enter new appointment date and time (yyyy-MM-ddTHH:mm): ");
        String newDateTimeStr = scanner.nextLine();

        LocalDateTime newDateTime = LocalDateTime.parse(newDateTimeStr);

        boolean success = patientService.rescheduleAppointment(patientId, appointmentId, newDateTime);
        if (success) {
            System.out.println("Appointment rescheduled successfully.");
        } else {
            System.out.println("Failed to reschedule appointment. Please check the details and try again.");
        }
    }

    // Method to view available appointment slots
    public void viewAvailableAppointmentSlots(Scanner scanner) {
        System.out.print("Enter Doctor ID: ");
        String doctorId = scanner.nextLine();
        System.out.print("Enter date for available slots (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();

        LocalDate date = LocalDate.parse(dateStr);

        // Check back in AppointmentService again
        List<LocalDateTime> availableSlots = appointmentService.getAvailableSlots(doctorId, date);
        if (availableSlots.isEmpty()) {
            System.out.println("No available slots for the given date.");
        } else {
            System.out.println("Available slots:");
            availableSlots.forEach(slot -> System.out.println(slot));
        }
    }
}