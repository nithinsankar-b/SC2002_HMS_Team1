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
    public void viewPatientDetails(Patient patient) {

        if (patient != null) {
            allocatedAppointmentView.showPatientDetails(patient);
        } else {
            allocatedAppointmentView.showErrorMessage("Patient not found.");
        }
    }

    // Method to view allocated appointments
    public void viewAllocatedAppointments(Patient patient) {
        allocatedAppointmentView.display(patient);
    }

    // Method to view appointment history
    public void viewAppointmentHistory(Patient patient) {
        appointmentHistoryView.display(patient);
    }

    // Method to view medical records
    public void viewMedicalRecords(Patient patient) {
        medicalRecordView.display(patient);
    }

    // Method to update contact information
    public void updateContactInformation(Patient patient) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Patient ID: " + patient.getHospitalID());
        System.out.print("Enter new contact information: ");
        String newContactInfo = scanner.nextLine();

        scanner.close();

        boolean success = patientService.updatePatientContact(patient.getHospitalID(), newContactInfo);
        if (success) {
            System.out.println("Contact information updated successfully.");
        } else {
            System.out.println("Failed to update contact information. Patient not found.");
        }
    }

    // Method to create an appointment for a patient
    public void createAppointment(Patient patient) {
        System.out.print("Patient ID: " + patient.getHospitalID());

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Doctor ID: ");
        String doctorId = scanner.nextLine();

        // Additional to check if doctor doesn't exist

        System.out.print("Enter appointment date and time (yyyy-MM-ddTHH:mm): ");

        String appointmentDateTime = scanner.nextLine();

        LocalDateTime dateTime = LocalDateTime.parse(appointmentDateTime);

        boolean success = patientService.createAppointment(patient.getHospitalID(), doctorId, dateTime);
        if (success) {
            System.out.println("Appointment created successfully.");
        } else {
            System.out.println("Failed to create appointment.");
        }
    }

    // Method to cancel an appointment for a patient
    public void cancelAppointment(Patient patient) {
        System.out.print("Patient ID: " + patient.getHospitalID());
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Appointment ID: ");
        String appointmentId = scanner.nextLine();

        boolean success = patientService.cancelAppointment(patient.getHospitalID(), appointmentId);
        if (success) {
            System.out.println("Appointment cancelled successfully.");
        } else {
            System.out.println("Failed to cancel appointment. Please check the details and try again.");
        }
    }

    // Method to reschedule an existing appointment
    public void rescheduleAppointment(Patient patient) {
        System.out.print("Patient ID: " + patient.getHospitalID());
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Appointment ID: ");
        String appointmentId = scanner.nextLine();
        System.out.print("Enter new appointment date and time (yyyy-MM-ddTHH:mm): ");
        String newDateTimeStr = scanner.nextLine();

        LocalDateTime newDateTime = LocalDateTime.parse(newDateTimeStr);

        boolean success = patientService.rescheduleAppointment(patient.getHospitalID(), appointmentId, newDateTime);
        if (success) {
            System.out.println("Appointment rescheduled successfully.");
        } else {
            System.out.println("Failed to reschedule appointment. Please check the details and try again.");
        }
    }

    // Method to view available appointment slots
    public void viewAvailableAppointmentSlots() {
        System.out.print("Enter Doctor ID: ");
        Scanner scanner = new Scanner(System.in);
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
            availableSlots.forEach(System.out::println);
        }
    }
}