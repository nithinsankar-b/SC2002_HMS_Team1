package interfaces;

import models.Appointment;
import models.Medication;

import java.util.List;

public interface IAppointmentService {
    boolean scheduleAppointment(Appointment appointment);
    boolean cancelAppointment(String appointmentId);
    boolean rescheduleAppointment(String appointmentId, Appointment newAppointment);
    List<Appointment> viewScheduledAppointments();
    Appointment getAppointment(String appointmentId);
    void recordAppointmentOutcome(String appointmentId, String serviceProvided, List<Medication> prescribedMedications, String consultationNotes);
}
