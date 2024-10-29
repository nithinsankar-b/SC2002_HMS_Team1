package views;

import models.Doctor;
import services.ScheduleService;

public class ViewPersonalSchedule  {
	private final ScheduleService scheduleService;
	
	public ViewPersonalSchedule(ScheduleService scheduleService) {
		this.scheduleService = scheduleService;
	}

	   //Viewing of Doctor personal Schedule
    public void displayPersonalSchedule(Doctor doctor) {
    	System.out.println("Displaying Medical Records for Doctor: " + doctor.getName() + doctor.getHospitalID());
    	scheduleService.printSchedule(doctor.getHospitalID());
    }
}
