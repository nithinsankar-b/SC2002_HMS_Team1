package interfaces;

import models.AppointmentRequest;
import java.util.List;

public interface IAppointmentService {
    /**
     * Retrieves pending appointment requests for a specific doctor within the current week.
     *
     * @param doctorId The ID of the doctor.
     * @return A list of pending appointment requests.
     */
    List<AppointmentRequest> getPendingRequestsForDoctor(String doctorId);

    /**
     * Accepts an appointment request based on its ID.
     *
     * @param requestId The ID of the appointment request to accept.
     */
    void acceptAppointmentRequest(String requestId);

    /**
     * Declines an appointment request based on its ID.
     *
     * @param requestId The ID of the appointment request to decline.
     */
    void declineAppointmentRequest(String requestId);
    
    List<AppointmentRequest> viewUpcomingAppointments(String doctorId);
}
