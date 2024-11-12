package interfaces;

import java.time.LocalDate;
import java.time.LocalTime;

public interface IScheduleService {
 
   boolean bookAppointment(String doctorID, LocalDate date, LocalTime timeSlot, String patientID);

   
   boolean cancelAppointment(String doctorID, LocalDate date, LocalTime timeSlot, String patientID);

  
   void setAvailable(String doctorID, LocalDate date, LocalTime timeSlot);

   
   void setUnavailable(String doctorID, LocalDate date, LocalTime timeSlot);

}
