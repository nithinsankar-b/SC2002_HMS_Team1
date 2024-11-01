
package views;
import services.ScheduleService;
import models.Doctor;

public class UpcomingAppointmentsView {
	private final ScheduleService scheduleService;
	
	public UpcomingAppointmentsView(ScheduleService scheduleService) {
		this.scheduleService = scheduleService;
	}
    //View upcoming appointments
    public void displayUpcomingAppointments(Doctor doctor){
    	System.out.println("Displaying Upcoming Appointments for Doctor: " + doctor.getName() + " "+doctor.getHospitalID());
    	scheduleService.printUpcomingAppointments(doctor.getHospitalID());
    }
}


