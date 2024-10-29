
package views;
import services.ScheduleService;
import models.Doctor;

public class ViewUpcomingAppointments {
	private final ScheduleService scheduleService;
	
	public ViewUpcomingAppointments(ScheduleService scheduleService) {
		this.scheduleService = scheduleService;
	}
    //View upcoming appointments
    public void displayUpcomingAppointments(Doctor doctor){
    	System.out.println("Displaying Upcoming Appointments for Doctor: " + doctor.getName() + " "+doctor.getHospitalID());
    	scheduleService.printUpcomingAppointments(doctor.getHospitalID());
    }
}


