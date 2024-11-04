package interfaces;

import models.AppointmentRequest;
import java.util.List;

/**
 * Interface for appointment request service operations.
 * This interface defines methods for managing appointment requests, including retrieving pending requests and handling acceptance or decline of requests.
 */
public interface IAppointmentRequestService {

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
     * @return True if the request was successfully accepted, false otherwise.
     */
    boolean acceptAppointmentRequest(String requestId);

    /**
     * Declines an appointment request based on its ID.
     *
     * @param requestId The ID of the appointment request to decline.
     * @return True if the request was successfully declined, false otherwise.
     */
    boolean declineAppointmentRequest(String requestId);
}
