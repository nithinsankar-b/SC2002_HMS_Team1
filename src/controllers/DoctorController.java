package controllers;

import services.DoctorService;
import services.AppointmentService;
import services.ScheduleService;
import services.UserService;
import views.PatientMedicalRecordView;
import views.PendingAppointmentRequestView;
import views.PersonalScheduleView;
import views.UpcomingAppointmentsView;
import services.AppointmentRequestService;
import services.MedicalRecordService;

import models.Doctor;

import java.util.Scanner;
/**
 * Controller for Doctor-related actions, responsible for handling requests
 * from the view layer and calling appropriate services to process and return
 * data related to appointments, schedules, and patient records.
 */
public class DoctorController {

    private final DoctorService doctorService;
    private final AppointmentService appointmentService;
    private final ScheduleService scheduleService;
    private final AppointmentRequestService appointmentRequestService;
    private final MedicalRecordService medicalRecordService;
    private final UserService userService;
    private final PatientMedicalRecordView medicalRecordsView;
    private final PendingAppointmentRequestView viewPendingAppointmentRequest;
    private final PersonalScheduleView personalScheduleView;
    private final UpcomingAppointmentsView viewUpcomingAppointments;

    /**
     * Constructs a DoctorController with the necessary services and initializes views.
     *
     * @param doctorService Service for doctor-related operations.
     * @param scheduleService Service for schedule-related operations.
     * @param medicalRecordService Service for medical record-related operations.
     * @param appointmentService Service for appointment-related operations.
     */
    public DoctorController(DoctorService doctorService, ScheduleService scheduleService, MedicalRecordService medicalRecordService, AppointmentService appointmentService) {
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
        this.medicalRecordService = medicalRecordService;
        this.scheduleService = scheduleService;
        this.appointmentRequestService = new AppointmentRequestService(scheduleService, appointmentService);
        this.userService = new UserService();

        // Initialize views
        this.medicalRecordsView = new PatientMedicalRecordView(medicalRecordService);
        this.viewPendingAppointmentRequest = new PendingAppointmentRequestView(scheduleService, appointmentService);
        this.personalScheduleView = new PersonalScheduleView(scheduleService);
        this.viewUpcomingAppointments = new UpcomingAppointmentsView(scheduleService);
    }

    /**
     * Displays medical records of a specific patient.
     *
     * @param patientId The ID of the patient whose records are to be viewed.
     */
    public void medicalRecordsView(String patientId) {
        medicalRecordsView.printMedicalRecords(patientId);
    }

    /**
     * Displays pending appointment requests for the given doctor.
     *
     * @param doctor The doctor whose pending appointment requests are to be viewed.
     */
    public void pendingAppointmentsView(Doctor doctor) {
        viewPendingAppointmentRequest.displayPendingRequests(doctor);
    }

    /**
     * Displays the personal schedule of the given doctor.
     *
     * @param doctor The doctor whose personal schedule is to be viewed.
     */
    public void personalScheduleView(Doctor doctor) {
        personalScheduleView.displayPersonalSchedule(doctor);
    }

    /**
     * Displays upcoming appointments for the given doctor.
     *
     * @param doctor The doctor whose upcoming appointments are to be viewed.
     */
    public void upcomingAppointmentsView(Doctor doctor) {
        viewUpcomingAppointments.displayUpcomingAppointments(doctor);
    }

    /**
     * Accepts an appointment request by request ID.
     */
    public void acceptAppointmentRequest() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter RequestID: ");
        String reqId = sc.nextLine();
        doctorService.acceptanceOfRequest(reqId);
    }

    /**
     * Declines an appointment request by request ID.
     */
    public void declineAppointmentRequest() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter RequestID: ");
        String reqId = sc.nextLine();
        doctorService.declineRequest(reqId);
    }

    /**
     * Sets a specific date and time as available for appointments for the given doctor.
     *
     * @param doctor The doctor setting the availability.
     */
    public void setToAvailable(Doctor doctor) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Date (yyyy-MM-dd): ");
        String date = sc.nextLine();
        System.out.println("Enter Start Time (HH:mm): ");
        String startTime = sc.nextLine();
        boolean success = doctorService.setAvailablity(doctor, date, startTime);
        if (success) {
            System.out.println("Requested appointment slot has been made available.");
        } else {
            System.out.println("Error in setting availability.");
        }
    }

    /**
     * Sets a specific date and time as unavailable for appointments for the given doctor.
     *
     * @param doctor The doctor setting the unavailability.
     */
    public void setToUnavailable(Doctor doctor) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter Date (yyyy-MM-dd): ");
        String date = sc.nextLine();
        System.out.println("Enter Start Time (HH:mm): ");
        String startTime = sc.nextLine();
        boolean success = doctorService.setUnavailability(doctor, date, startTime);
        if (success) {
            System.out.println("Requested appointment slot is blocked.");
        } else {
            System.out.println("Error in setting unavailability.");
        }
    }

    /**
     * Records a new diagnosis for a specified patient.
     */
    public void newPatientDiagnosis() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter PatientId: ");
        String patientId = sc.nextLine();
        System.out.println("Enter latest diagnosis: ");
        String newDiagnosis = sc.nextLine();
        doctorService.updatePatientDiagnosis(patientId, newDiagnosis);
    }

    /**
     * Records a new prescription for a specified patient.
     */
    public void newPatientPrescription() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter PatientId: ");
        String patientId = sc.nextLine();
        System.out.println("Enter latest Prescription: ");
        String newPrescription = sc.nextLine();
        doctorService.updatePatientPrescription(patientId, newPrescription);
    }

    /**
     * Records the outcome of an appointment, including services provided, medication prescribed, and consultation notes.
     */
    public void appointmentOutcomeRecord() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter AppointmentID: ");
        String appointmentId = sc.nextLine();
        System.out.println("Enter Service Provided: ");
        String serviceProvided = sc.nextLine();
        System.out.println("Enter medications prescribed (if more than 1 medicine, separate by comma): ");
        String medicineList = sc.nextLine();
        System.out.println("Enter Consultation Notes: ");
        String consultationNotes = sc.nextLine();
        doctorService.recordAppointmentOutcome(appointmentId, serviceProvided, medicineList, consultationNotes);
    }

    /**
     * Changes the password for the user account.
     *
     * @param hospitalID The hospital ID of the user.
     * @param oldPassword The current password.
     * @param newPassword The new password.
     */
    public void changePassword(String hospitalID, String oldPassword, String newPassword) {
        userService.changePassword(hospitalID, oldPassword, newPassword);
    }
}
