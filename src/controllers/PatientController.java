package controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

import models.Patient;
import models.Appointment;
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
        if (patient != null) {
            allocatedAppointmentView.display(patient);
        } else {
            System.out.println("Patient not found.");
        }
    }

    // Method to view appointment history
    public void viewAppointmentHistory(Patient patient) {
        if (patient != null) {
            appointmentHistoryView.display(patient);
        } else {
            System.out.println("Patient not found.");
        }
    }

    // Method to view medical records
    public void viewMedicalRecords(Patient patient) {
        if (patient != null) {
            medicalRecordView.display(patient);
        } else {
            System.out.println("Patient not found.");
        }
    }

    // Method to update contact information
    public void updateContactInformation(Patient patient) {
        if (patient != null) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter new contact information: ");
            String newContactInfo = scanner.nextLine();

            boolean success = patientService.updatePatientContact(patient.getHospitalID(), newContactInfo);
            if (success) {
                System.out.println("Contact information updated successfully.\nUpdated information will be reflected upon next login.");
            } else {
                System.out.println("Failed to update contact information. Patient not found.");
            }
        } else {
            System.out.println("Patient not found.");
        }
    }

    // Method to create an appointment for a patient
    public void createAppointment(Patient patient) {
        if (patient != null) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter Doctor ID: ");
            String doctorId = scanner.nextLine();

            System.out.print("Enter appointment date and time (yyyy-MM-ddTHH:mm): ");
            String appointmentDateTime = scanner.nextLine();

            try {
                LocalDateTime dateTime = LocalDateTime.parse(appointmentDateTime);
                Appointment appointment = new Appointment("Appointment: " + System.currentTimeMillis(), patient.getHospitalID(), doctorId, dateTime);
                boolean success = appointmentService.scheduleAppointment(appointment);
                if (success) {
                    System.out.println("Appointment created successfully.");
                } else {
                    System.out.println("Failed to create appointment.");
                }
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use yyyy-MM-ddTHH:mm.");
            }
        } else {
            System.out.println("Patient not found.");
        }
    }

    // Method to cancel an appointment for a patient
    public void cancelAppointment(Patient patient) {
        if (patient != null) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter Appointment ID: ");
            String appointmentId = scanner.nextLine();

            boolean success = appointmentService.cancelAppointment(appointmentId);
            if (success) {
                System.out.println("Appointment cancelled successfully.");
            } else {
                System.out.println("Failed to cancel appointment. Please check the details and try again.");
            }
        } else {
            System.out.println("Patient not found.");
        }
    }

    // Method to reschedule an existing appointment
    public void rescheduleAppointment(Patient patient) {
        if (patient != null) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter Appointment ID: ");
            String appointmentId = scanner.nextLine();
            System.out.print("Enter new appointment date and time (yyyy-MM-ddTHH:mm): ");
            String newDateTimeStr = scanner.nextLine();

            try {
                LocalDateTime newDateTime = LocalDateTime.parse(newDateTimeStr);
                Appointment appointment = appointmentService.getAppointment(appointmentId);
                if (appointment != null && appointment.getPatientId().equals(patient.getHospitalID())) {
                    Appointment newAppointment = new Appointment(appointment.getAppointmentId(), appointment.getPatientId(), appointment.getDoctorId(), newDateTime);
                    newAppointment.setServiceProvided("Rescheduled");
                    newAppointment.setStatus(appointment.getStatus());
                    newAppointment.setConsultationNotes(appointment.getConsultationNotes());
                    newAppointment.getMedications().addAll(appointment.getMedications());

                    boolean success = appointmentService.rescheduleAppointment(appointmentId, newAppointment);
                    if (success) {
                        System.out.println("Appointment rescheduled successfully.");
                    } else {
                        System.out.println("Failed to reschedule appointment. Please check the details and try again.");
                    }
                } else {
                    System.out.println("Appointment not found or does not belong to the patient.");
                }
            } catch (Exception e) {
                System.out.println("Invalid date format. Please use yyyy-MM-ddTHH:mm.");
            }
        } else {
            System.out.println("Patient not found.");
        }
    }

    // Method to view available appointment slots
    public void viewAvailableAppointmentSlots() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Doctor ID: ");
        String doctorId = scanner.nextLine();
        System.out.print("Enter date for available slots (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();

        LocalDate date = LocalDate.parse(dateStr);

        List<LocalDateTime> availableSlots = appointmentService.getAvailableSlots(doctorId, date);
        if (availableSlots.isEmpty()) {
            System.out.println("No available slots for the given date.");
        } else {
            System.out.println("Available slots:");
            availableSlots.forEach(System.out::println);
        }
    }
}