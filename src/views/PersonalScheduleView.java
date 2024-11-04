package views;

import models.Doctor;
import services.ScheduleService;

/**
 * The {@code PersonalScheduleView} class provides a user interface for displaying 
 * the personal schedule of a doctor. It utilizes the {@code ScheduleService} 
 * to manage and print the doctor's schedule.
 */
public class PersonalScheduleView {

    private final ScheduleService scheduleService; // Service dependency

    /**
     * Constructs a {@code PersonalScheduleView} with the specified schedule service.
     *
     * @param scheduleService the service used to manage and retrieve doctor schedules
     */
    public PersonalScheduleView(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /**
     * Displays the personal schedule for the specified doctor.
     *
     * @param doctor the doctor whose personal schedule is to be displayed
     */
    public void displayPersonalSchedule(Doctor doctor) {
        System.out.println("Displaying Personal Schedule for Doctor: " + doctor.getName() + " (ID: " + doctor.getHospitalID() + ")");
        scheduleService.printSchedule(doctor.getHospitalID());
    }
}
