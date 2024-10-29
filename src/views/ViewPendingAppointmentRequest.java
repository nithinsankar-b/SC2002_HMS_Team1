package views;

import services.AppointmentRequestService;
import services.ScheduleService;
import services.AppointmentService;
import models.Doctor;



public class ViewPendingAppointmentRequest  {
	private final AppointmentRequestService appointmentRequestService;
	
	public ViewPendingAppointmentRequest(ScheduleService scheduleService,AppointmentService appointmentService ) {
		this.appointmentRequestService = new AppointmentRequestService(scheduleService, appointmentService);
	}
	  //View pending appointment requests
    public void displayPendingRequests(Doctor doctor) {
    	System.out.println("Displaying Pending for Doctor: " + doctor.getName() + doctor.getHospitalID());
        // Call processPendingRequests from AppointmentRequestService
        appointmentRequestService.processPendingRequests(doctor.getHospitalID());
    }
}
