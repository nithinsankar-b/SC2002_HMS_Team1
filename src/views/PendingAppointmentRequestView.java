package views;

import services.AppointmentRequestService;
import services.ScheduleService;
import services.AppointmentService;
import models.Doctor;

/**
 * The {@code PendingAppointmentRequestView} class provides a user interface for displaying 
 * pending appointment requests for a specific doctor.
 * It utilizes the {@code AppointmentRequestService} to manage and display these requests.
 */
public class PendingAppointmentRequestView {

    private final AppointmentRequestService appointmentRequestService; // Service dependency

    /**
     * Constructs a {@code PendingAppointmentRequestView} with the specified services.
     *
     * @param scheduleService the service used to manage doctor schedules
     * @param appointmentService the service used to manage appointments
     */
    public PendingAppointmentRequestView(ScheduleService scheduleService, AppointmentService appointmentService) {
        this.appointmentRequestService = new AppointmentRequestService(scheduleService, appointmentService);
    }

    /**
     * Displays the pending appointment requests for the specified doctor.
     *
     * @param doctor the doctor for whom the pending requests are to be displayed
     */
    public void displayPendingRequests(Doctor doctor) {
        System.out.println("Displaying Pending Requests for Doctor: " + doctor.getName() + " (ID: " + doctor.getHospitalID() + ")");
        // Call processPendingRequests from AppointmentRequestService
        appointmentRequestService.processPendingRequests(doctor.getHospitalID());
    }
}
