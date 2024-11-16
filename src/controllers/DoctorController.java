package controllers;

import services.DoctorService;
import services.AppointmentService;
import services.ScheduleService;
import services.UserService;
import views.PatientMedicalRecordView;
import views.PendingAppointmentRequestView;
import views.PersonalScheduleView;
import views.UpcomingAppointmentsView;
import views.AppointmentOutcomeRecordView;
import services.AppointmentRequestService;
import services.MedicalRecordService;
import models.Appointment;
import models.Doctor;
import models.Medication;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
/**
 * Controller class for handling operations related to doctors.
 * This class manages doctor functionalities such as viewing schedules,
 * managing appointments, updating medical records, and recording appointment outcomes.
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
  private final AppointmentOutcomeRecordView appointmentOutcomeRecordView;

  /**
   * Constructor for DoctorController.
   *
   * @param doctorService       The service for handling doctor-related operations.
   * @param scheduleService     The service for managing schedules.
   * @param medicalRecordService The service for handling medical records.
   * @param appointmentService  The service for managing appointments.
   */
  public DoctorController(DoctorService doctorService, ScheduleService scheduleService, 
                          MedicalRecordService medicalRecordService, AppointmentService appointmentService) {
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
      this.appointmentOutcomeRecordView = new AppointmentOutcomeRecordView();
  }

  /**
   * Displays the medical records of a patient.
   *
   * @param patientId The ID of the patient.
   */
  public void medicalRecordsView(String patientId) {
      medicalRecordsView.printMedicalRecords(patientId);
  }

  /**
   * Displays pending appointment requests for a doctor.
   *
   * @param doctor The doctor object.
   */
  public void pendingAppointmentsView(Doctor doctor) {
      viewPendingAppointmentRequest.displayPendingRequests(doctor);
  }

  /**
   * Displays the personal schedule of a doctor.
   *
   * @param doctor The doctor object.
   */
  public void personalScheduleView(Doctor doctor) {
      personalScheduleView.displayPersonalSchedule(doctor);
  }

  /**
   * Displays upcoming appointments for a doctor.
   *
   * @param doctor The doctor object.
   */
  public void upcomingAppointmentsView(Doctor doctor) {
      viewUpcomingAppointments.displayUpcomingAppointments(doctor);
  }

  /**
   * Accepts an appointment request for a doctor.
   *
   * @param doctor The doctor object.
   */
  public void acceptAppointmentRequest(Doctor doctor) {
      viewPendingAppointmentRequest.displayPendingRequests(doctor);
      Scanner sc = new Scanner(System.in);
      System.out.println("Enter RequestID: ");
      String reqId = sc.nextLine();
      doctorService.acceptanceOfRequest(reqId);
  }

  /**
   * Declines an appointment request for a doctor.
   *
   * @param doctor The doctor object.
   */
  public void declineAppointmentRequest(Doctor doctor) {
      viewPendingAppointmentRequest.displayPendingRequests(doctor);
      Scanner sc = new Scanner(System.in);
      System.out.println("Enter RequestID: ");
      String reqId = sc.nextLine();
      doctorService.declineRequest(reqId);
  }

  /**
   * Sets a specific time slot as available for appointments.
   *
   * @param doctor The doctor object.
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
          System.out.println("Error.");
      }
  }

  /**
   * Sets a specific time slot as unavailable for appointments.
   *
   * @param doctor The doctor object.
   */
  public void setToUnavailable(Doctor doctor) {
      Scanner sc = new Scanner(System.in);
      System.out.println("Enter Date (yyyy-MM-dd): ");
      String date = sc.nextLine();
      System.out.println("Enter Start Time (HH:mm): ");
      String startTime = sc.nextLine();
      boolean success = doctorService.setUnavailability(doctor, date, startTime);
      if (success) {
          System.out.println("Requested appointment slot is set to unavailable.");
      } else {
          System.out.println("Error.");
      }
  }

  /**
   * Blocks time slots for a doctor.
   *
   * @param doctor The doctor object.
   */
  public void blockSlots(Doctor doctor) {
      scheduleService.blockTimeSlots(doctor.getHospitalID());
  }

  /**
   * Unblocks time slots for a doctor.
   *
   * @param doctor The doctor object.
   */
  public void unblockSlots(Doctor doctor) {
      scheduleService.unblockTimeSlots(doctor.getHospitalID());
  }

  /**
   * Updates the diagnosis for a patient.
   */
  public void newPatientDiagnosis() {
      Scanner sc = new Scanner(System.in);
      System.out.println("Enter PatientID: ");
      String patientId = sc.nextLine();
      System.out.println("Enter latest diagnosis: ");
      String newDiagnosis = sc.nextLine();
      doctorService.updatePatientDiagnosis(patientId, newDiagnosis);
  }

  /**
   * Updates the prescription for a patient.
   */
  public void newPatientPrescription() {
      Scanner sc = new Scanner(System.in);
      System.out.println("Enter PatientID: ");
      String patientId = sc.nextLine();
      System.out.println("Enter latest Prescription: ");
      String newDiagnosis = sc.nextLine();
      doctorService.updatePatientPrescription(patientId, newDiagnosis);
  }

  /**
   * Records the outcome of an appointment, including service provided, medications prescribed, and notes.
   *
   * @param doctor The doctor object.
   */
  public void appointmentOutcomeRecord(Doctor doctor) {
      Scanner sc = new Scanner(System.in);

      System.out.println("Enter AppointmentID: ");
      String appointmentId = sc.nextLine();
      System.out.println("Enter Service Provided: ");
      String serviceProvided = sc.nextLine();
      System.out.println("Enter medications prescribed  (if more than 1 medicine, separate by ;): ");
      String medicineList = sc.nextLine();
      System.out.println("Enter quantities prescribed (if more than 1 medicine, separate by ;): ");
      String quantitiesList = sc.nextLine();
      System.out.println("Enter Consultation Notes: ");
      String consultationNotes = sc.nextLine();

      LocalDate date = appointmentService.getAppointmentById(appointmentId).getAppointmentDateTime().toLocalDate();
      LocalTime time = appointmentService.getAppointmentById(appointmentId).getAppointmentDateTime().toLocalTime();
      scheduleService.setAvailable1(doctor.getHospitalID(), date, time);
      doctorService.recordAppointmentOutcome(appointmentId, serviceProvided, medicineList, quantitiesList, consultationNotes);
  }

  /**
   * Displays appointment outcome records for the doctor.
   *
   * @param doctor The doctor object.
   * @return True if there are confirmed appointments, false otherwise.
   */
  public boolean viewAppointmentOutcomeRecords(Doctor doctor) {
      AppointmentService appointmentService = new AppointmentService();
      List<Appointment> allAppointments = appointmentService.viewScheduledAppointments();

      List<Appointment> confirmedAppointments = new ArrayList<>();
      for (Appointment appointment : allAppointments) {
          if (enums.AppointmentStatus.CONFIRMED.equals(appointment.getStatus()) &&
              appointment.getDoctorId().equals(doctor.getHospitalID())) {
              confirmedAppointments.add(appointment);
          }
      }

      if (confirmedAppointments.isEmpty()) {
          System.out.println("No confirmed appointments found for Doctor " + doctor.getName());
          return false;
      } else {
          System.out.println("Displaying Confirmed Appointments for Doctor: " + doctor.getName());
          for (Appointment appointment : confirmedAppointments) {
              List<Medication> medications = appointment.getMedications();
              System.out.println("--------------------------------------");
              System.out.println("Appointment ID        : " + appointment.getAppointmentId());
              System.out.println("Patient ID            : " + appointment.getPatientId());
              System.out.println("Doctor ID             : " + appointment.getDoctorId());
              System.out.println("Appointment Date      : " + appointment.getAppointmentDateTime().toLocalDate());
              System.out.println("Appointment Time      : " + appointment.getAppointmentDateTime().toLocalTime());
              System.out.println("Status                : " + appointment.getStatus());
              System.out.println("Consultation Notes    : " + appointment.getConsultationNotes());
              System.out.println("Service Provided      : " + appointment.getServiceProvided());

              if (medications != null && !medications.isEmpty()) {
                  System.out.println("Medications:");
                  for (Medication medication : medications) {
                      System.out.println("    Name              : " + medication.getName());
                      System.out.println("    Quantity          : " + medication.getQuantity());
                  }
              } else {
                  System.out.println("Medications: None");
              }
              System.out.println("Medication Status     : " + appointment.getMedicationStatus());
              System.out.println("--------------------------------------");
          }
      }
      return true;
  }

  /**
   * Changes the password for a doctor.
   *
   * @param hospitalID  The doctor's hospital ID.
   * @param oldPassword The old password.
   * @param newPassword The new password.
   */
  public void changePassword(String hospitalID, String oldPassword, String newPassword) {
      userService.changePassword(hospitalID, oldPassword, newPassword);
  }
}
