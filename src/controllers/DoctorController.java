package controllers;

import services.DoctorService;
import services.AppointmentService;
import services.ScheduleService;
import services.AppointmentRequestService;
import services.MedicalRecordService;

import models.Doctor;

import java.util.Scanner;
public class DoctorController {
	private final DoctorService doctorService;
    private final AppointmentService appointmentService;
	private final ScheduleService scheduleService;
	private final AppointmentRequestService appointmentRequestService;
	private final MedicalRecordService medicalRecordService;
    private final views.MedicalRecordsView medicalRecordsView;
    private final views.PendingAppointmentRequestView viewPendingAppointmentRequest;
    private final views.PersonalScheduleView personalScheduleView;
    private final views.UpcomingAppointmentsView viewUpcomingAppointments;
    

    public DoctorController(DoctorService doctorService, ScheduleService scheduleService, MedicalRecordService medicalRecordService, AppointmentService appointmentService ) {
        this.doctorService = doctorService;
        this.appointmentService = appointmentService;
        this.medicalRecordService = medicalRecordService;
        this.scheduleService = scheduleService;
        this.appointmentRequestService = new AppointmentRequestService(scheduleService, appointmentService);
		
        // the views
        this.medicalRecordsView = new views.MedicalRecordsView(medicalRecordService);
        this.viewPendingAppointmentRequest = new views.PendingAppointmentRequestView(scheduleService, appointmentService);
        this.personalScheduleView = new views.PersonalScheduleView(scheduleService);
        this.viewUpcomingAppointments = new views.UpcomingAppointmentsView(scheduleService);
        
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
    
    public void acceptAppointmentRequest() {
    	Scanner sc = new Scanner(System.in);
    	System.out.println("Enter RequestID: ");
    	String reqId = sc.nextLine();
    	doctorService.acceptanceOfRequest(reqId);
    }
    
    public void declineAppointmentRequest() {
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
    		 System.out.println("Requested appointment slot is Blocked.");
    	 }else {
    		 System.out.println("error");
    	 }
     }
     
     public void newPatientDiagnosis() {
    	 Scanner sc = new Scanner(System.in);
    	 System.out.println("Enter PatientId): ");
    	 String patientId = sc.nextLine();
    	 System.out.println("Enter latest diagnosis: ");
    	 String newDiagnosis = sc.nextLine();
    	 doctorService.updatePatientDiagnosis(patientId, newDiagnosis);
     }
     
     public void newPatientPrescription() {
    	 Scanner sc = new Scanner(System.in);
    	 System.out.println("Enter PatientId: ");
    	 String patientId = sc.nextLine();
    	 System.out.println("Enter latest Prescription: ");
    	 String newDiagnosis = sc.nextLine();
    	 doctorService.updatePatientPrescription(patientId, newDiagnosis);
     }
     
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
     
    
}
