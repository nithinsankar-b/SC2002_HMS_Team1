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


    public DoctorController(DoctorService doctorService, ScheduleService scheduleService, MedicalRecordService medicalRecordService, AppointmentService appointmentService ) {
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
        this.medicalRecordService = medicalRecordService;
        this.scheduleService = scheduleService;
        this.appointmentRequestService = new AppointmentRequestService(scheduleService, appointmentService);
        this.userService = new UserService();

        // the views
        this.medicalRecordsView = new PatientMedicalRecordView(medicalRecordService);
        this.viewPendingAppointmentRequest = new PendingAppointmentRequestView(scheduleService, appointmentService);
        this.personalScheduleView = new PersonalScheduleView(scheduleService);
        this.viewUpcomingAppointments = new UpcomingAppointmentsView(scheduleService);
        this.appointmentOutcomeRecordView = new AppointmentOutcomeRecordView();
    }
    //viewing medical records
    public void medicalRecordsView(String patientId) {

      medicalRecordsView.printMedicalRecords(patientId);
    }
    //viewing pending appt request
    public void pendingAppointmentsView(Doctor doctor) {
      viewPendingAppointmentRequest.displayPendingRequests(doctor);
    }
    //viewing personal schedule
    public void personalScheduleView(Doctor doctor) {
      personalScheduleView.displayPersonalSchedule(doctor);
    }
    //view upcoming appointments
    public void upcomingAppointmentsView(Doctor doctor) {
      viewUpcomingAppointments.displayUpcomingAppointments(doctor);
    }

    public void acceptAppointmentRequest(Doctor doctor) {
        viewPendingAppointmentRequest.displayPendingRequests(doctor);
      Scanner sc = new Scanner(System.in);
      System.out.println("Enter RequestID: ");
      String reqId = sc.nextLine();
      doctorService.acceptanceOfRequest(reqId);
    }

    public void declineAppointmentRequest(Doctor doctor) {
        viewPendingAppointmentRequest.displayPendingRequests(doctor);
      Scanner sc = new Scanner(System.in);
      System.out.println("Enter RequestID: ");
      String reqId = sc.nextLine();
      doctorService.declineRequest(reqId);
    }

    public void setToAvailable(Doctor doctor) {
      Scanner sc = new Scanner(System.in);
      System.out.println("Enter Date (yyyy-MM-dd): ");
      String date = sc.nextLine();
      System.out.println("Enter Start Time (HH:mm): ");
      String startTime = sc.nextLine();
      boolean success = doctorService.setAvailablity(doctor, date, startTime);
      if (success) {
        System.out.println("Requested appointment slot has been made available.");
      }
      else {
       System.out.println("error");
      }
    }


     public void setToUnavailable(Doctor doctor) {
       Scanner sc = new Scanner(System.in);
       System.out.println("Enter Date (yyyy-MM-dd): ");
       String date = sc.nextLine();
       System.out.println("Enter Start Time (HH:mm): ");
       String startTime = sc.nextLine();
       boolean success = doctorService.setUnavailability(doctor, date, startTime);
       if(success) {
System.out.println("Requested appointment slot is set to unavailable.");
       }else {
         System.out.println("error");
       }
     }
     
     public void blockSlots(Doctor doctor) {
    	 scheduleService.blockTimeSlots(doctor.getHospitalID());
     }
     
     public void unblockSlots(Doctor doctor) {
    	 scheduleService.unblockTimeSlots(doctor.getHospitalID());
     }
     

     public void newPatientDiagnosis() {
       Scanner sc = new Scanner(System.in);
       System.out.println("Enter PatientID: ");
       String patientId = sc.nextLine();
       System.out.println("Enter latest diagnosis: ");
       String newDiagnosis = sc.nextLine();
       doctorService.updatePatientDiagnosis(patientId, newDiagnosis);
     }

     public void newPatientPrescription() {
       Scanner sc = new Scanner(System.in);
       System.out.println("Enter PatientID: ");
       String patientId = sc.nextLine();
       System.out.println("Enter latest Prescription: ");
       String newDiagnosis = sc.nextLine();
       doctorService.updatePatientPrescription(patientId, newDiagnosis);
     }

    public void appointmentOutcomeRecord(Doctor doctor) {
        Scanner sc = new Scanner(System.in);
        String appointmentId = null;
        String serviceProvided = null;
        String medicineList = null;
        String quantitiesList = null;
        String consultationNotes = null;
        LocalDate date = null;
        LocalTime time = null;

        // Flag to check if everything is valid
        boolean isValid = true;

        // Prompt for Appointment ID
        while (appointmentId == null) {
            try {
                System.out.println("Enter AppointmentID: ");
                appointmentId = sc.nextLine();
                if (appointmentService.getAppointmentById(appointmentId) == null) {
                    throw new IllegalArgumentException("Appointment ID not found.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid Appointment ID. Please try again.");
                appointmentId = null; // Reset to re-prompt
                isValid = false;
            }
        }

        // Prompt for Service Provided
        while (serviceProvided == null || serviceProvided.trim().isEmpty()) {
            try {
                System.out.println("Enter Service Provided: ");
                serviceProvided = sc.nextLine();
                if (serviceProvided.trim().isEmpty()) {
                    throw new IllegalArgumentException("Service provided cannot be empty.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input. Please provide a valid service description.");
                serviceProvided = null; // Reset to re-prompt
                isValid = false;
            }
        }

        // Prompt for Medications Prescribed
        while (medicineList == null || medicineList.trim().isEmpty()) {
            try {
                System.out.println("Enter medications prescribed (if more than 1 medicine, separate by ;): ");
                medicineList = sc.nextLine();
                if (medicineList.trim().isEmpty()) {
                    throw new IllegalArgumentException("Medicine list cannot be empty.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input. Please provide a valid list of medications.");
                medicineList = null; // Reset to re-prompt
                isValid = false;
            }
        }

        // Prompt for Quantities Prescribed
        while (quantitiesList == null || quantitiesList.trim().isEmpty()) {
            try {
                System.out.println("Enter quantities prescribed (if more than 1 medicine, separate by ;): ");
                quantitiesList = sc.nextLine();
                if (quantitiesList.trim().isEmpty()) {
                    throw new IllegalArgumentException("Quantities list cannot be empty.");
                }

                // Check if medicineList and quantitiesList have matching counts
                String[] medicines = medicineList.split(";");
                String[] quantities = quantitiesList.split(";");
                if (medicines.length != quantities.length) {
                    throw new IllegalArgumentException("Number of medicines and quantities must match.");
                }

                // Ensure all quantities are numbers
                for (String quantity : quantities) {
                    try {
                        Integer.parseInt(quantity.trim());  // Check if the quantity is a valid integer
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Each quantity must be a valid number.");
                    }
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input. Ensure the quantities match the number of medications and are valid numbers.");
                quantitiesList = null; // Reset to re-prompt
                isValid = false;
            }
        }

        // Prompt for Consultation Notes
        while (consultationNotes == null || consultationNotes.trim().isEmpty()) {
            try {
                System.out.println("Enter Consultation Notes: ");
                consultationNotes = sc.nextLine();
                if (consultationNotes.trim().isEmpty()) {
                    throw new IllegalArgumentException("Consultation notes cannot be empty.");
                }
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid input. Please provide valid consultation notes.");
                consultationNotes = null; // Reset to re-prompt
                isValid = false;
            }
        }

        // Retrieve Appointment Date and Time
        try {
            date = appointmentService.getAppointmentById(appointmentId).getAppointmentDateTime().toLocalDate();
            time = appointmentService.getAppointmentById(appointmentId).getAppointmentDateTime().toLocalTime();
        } catch (Exception e) {
            System.out.println("Error retrieving appointment date and time. Please check the Appointment ID.");
            isValid = false;
        }

        // If all inputs are valid, proceed to record the appointment outcome
        if (isValid) {
            try {
                // Proceed to record the outcome only if the inputs are valid (including medicineList, quantitiesList)
                scheduleService.setAvailable1(doctor.getHospitalID(), date, time);
                doctorService.recordAppointmentOutcome(appointmentId, serviceProvided, medicineList, quantitiesList, consultationNotes);

            } catch (Exception e) {
                System.out.println("Error recording the appointment outcome. Please try again.");
            }
        } else {
            // If any validation fails, do not record the appointment
            System.out.println("Appointment outcome not recorded due to invalid inputs.");
        }
    }
     
     public boolean viewAppointmentOutcomeRecords(Doctor doctor) {
    	    // Initialize AppointmentService to fetch appointments
    	    AppointmentService appointmentService = new AppointmentService();
    	    List<Appointment> allAppointments = appointmentService.viewScheduledAppointments();

    	    // Filter out appointments that are CONFIRMED and belong to the specified doctor
    	    List<Appointment> confirmedAppointments = new ArrayList<>();
    	    for (Appointment appointment : allAppointments) {
    	        if (enums.AppointmentStatus.CONFIRMED.equals(appointment.getStatus()) &&
    	                appointment.getDoctorId().equals(doctor.getHospitalID())) {
    	            confirmedAppointments.add(appointment);
    	        }
    	    }

    	    // If there are confirmed appointments, display them
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

    	            // Split the appointment date and time
    	            System.out.println("Appointment Date      : " + appointment.getAppointmentDateTime().toLocalDate());
    	            System.out.println("Appointment Time      : " + appointment.getAppointmentDateTime().toLocalTime());
    	            
    	            System.out.println("Status                : " + appointment.getStatus());
    	            System.out.println("Consultation Notes    : " + appointment.getConsultationNotes());
    	            System.out.println("Service Provided      : " + appointment.getServiceProvided());

    	            // Display medications, if any
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



     public void changePassword(String hospitalID, String oldPassword, String newPassword)
     {
       userService.changePassword(hospitalID, oldPassword, newPassword);
     }
    

}
