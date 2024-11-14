package views;

import services.ScheduleService;
import models.Doctor;
import models.Appointment;
import services.AppointmentService;
import java.util.List;
import java.util.ArrayList;
import enums.AppointmentStatus;

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

        // Fetch all appointments
        AppointmentService appointmentService = new AppointmentService();
        List<Appointment> allAppointments = appointmentService.viewScheduledAppointments();

        // Filter the appointments for the given doctor and confirmed status
        List<Appointment> doctorAppointments = new ArrayList<>();
        for (Appointment appointment : allAppointments) {
            // Check if the appointment matches the doctor's ID and is confirmed
            if (appointment.getDoctorId().equals(doctor.getHospitalID()) && AppointmentStatus.CONFIRMED.equals(appointment.getStatus())) {
                doctorAppointments.add(appointment);
            }
        }

        // If there are any confirmed appointments, display them
        if (doctorAppointments.isEmpty()) {
            System.out.println("No upcoming confirmed appointments found for Doctor: " + doctor.getName());
        } else {
            for (Appointment appointment : doctorAppointments) {
            	System.out.println("----------------------------");
                System.out.println("Appointment ID : " + appointment.getAppointmentId());
                System.out.println("Patient ID     : " + appointment.getPatientId());
                System.out.println("Date           : " + appointment.getAppointmentDateTime().toLocalDate()); // Show the date
                System.out.println("Time           : " + appointment.getAppointmentDateTime().toLocalTime()); // Show the time
                System.out.println("----------------------------"); // Separator for readability
            }
        }
    }

}
