package interfaces;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import models.Appointment;
import models.Medication;

public interface IAppointmentService {
    boolean scheduleAppointment(Appointment appointment);
    boolean cancelAppointment(String appointmentId);
    boolean rescheduleAppointment(String appointmentId, Appointment newAppointment);
    List<Appointment> viewScheduledAppointments();
    Appointment getAppointment(String appointmentId);
    void recordAppointmentOutcome(String appointmentId, String serviceProvided, List<Medication> prescribedMedications, String consultationNotes);
    List<LocalDateTime> getAvailableSlots(String doctorId, LocalDate date);
}
