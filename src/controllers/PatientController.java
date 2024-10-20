// File: controllers/PatientController.java
package controllers;

import java.time.LocalDateTime;
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

    // Main method to handle different patient operations
    public void handlePatientOperations() {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("Please choose an option:");
            System.out.println("1. View Patient Details");
            System.out.println("2. View Allocated Appointments");
            System.out.println("3. View Appointment History");
            System.out.println("4. View Medical Records");
            System.out.println("5. Update Contact Information");
            System.out.println("6. Create Appointment");
            System.out.println("7. Cancel Appointment");
            System.out.println("8. Reschedule Appointment");
            System.out.println("9. View All Patients");
            System.out.println("10. Delete Patient");
            System.out.println("11. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1 -> viewPatientDetails(scanner);
                case 2 -> viewAllocatedAppointments(scanner);
                case 3 -> viewAppointmentHistory(scanner);
                case 4 -> viewMedicalRecords(scanner);
                case 5 -> updateContactInformation(scanner);
                case 6 -> createAppointment(scanner);
                case 7 -> cancelAppointment(scanner);
                case 8 -> rescheduleAppointment(scanner);
                case 9 -> viewAllPatients();
                case 10 -> deletePatient(scanner);
                case 11 -> isRunning = false;
                default -> System.out.println("Invalid choice, please try again.");
            }
        }
    }

    // Method to view patient details
    private void viewPatientDetails(Scanner scanner) {
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
    private void viewAllocatedAppointments(Scanner scanner) {
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();
        allocatedAppointmentView.display();
    }

    // Method to view appointment history
    private void viewAppointmentHistory(Scanner scanner) {
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();
        appointmentHistoryView.display();
    }

    // Method to view medical records
    private void viewMedicalRecords(Scanner scanner) {
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();
        medicalRecordView.display();
    }

    // Method to update contact information
    private void updateContactInformation(Scanner scanner) {
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
    private void createAppointment(Scanner scanner) {
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
    private void cancelAppointment(Scanner scanner) {
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
    private void rescheduleAppointment(Scanner scanner) {
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

    // Method to view all patients
    private void viewAllPatients() {
        patientService.listAllPatients();
    }

    // Method to delete a patient
    private void deletePatient(Scanner scanner) {
        System.out.print("Enter Patient ID: ");
        String patientId = scanner.nextLine();

        boolean success = patientService.deletePatient(patientId); // Use the deletePatient method in PatientService

        if (success) {
            System.out.println("Patient " + patientId + " deleted successfully.");
        } else {
            System.out.println("Failed to delete patient. Patient not found.");
        }
    }
} 