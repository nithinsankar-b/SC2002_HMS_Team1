package views;

import services.ScheduleService;
import models.Doctor;

/**
 * The {@code UpcomingAppointmentsView} class provides a user interface for 
 * displaying upcoming appointments for a specific doctor. It interacts with 
 * the {@code ScheduleService} to retrieve and present this information.
 */
public class UpcomingAppointmentsView {
    
    private final ScheduleService scheduleService; // Service for managing schedule operations

    /**
     * Constructs an {@code UpcomingAppointmentsView} with the specified 
     * {@code ScheduleService}.
     *
     * @param scheduleService the service for managing appointment schedules
     */
    public UpcomingAppointmentsView(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /**
     * Displays the upcoming appointments for the specified doctor.
     *
     * @param doctor the doctor for whom to display upcoming appointments
     */
    public void displayUpcomingAppointments(Doctor doctor) {
        System.out.println("Displaying Upcoming Appointments for Doctor: " + doctor.getName() + " " + doctor.getHospitalID());
        scheduleService.printUpcomingAppointments(doctor.getHospitalID());
    }
}
