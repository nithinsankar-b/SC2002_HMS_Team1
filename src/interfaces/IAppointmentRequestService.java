package interfaces;

import models.AppointmentRequest;
import java.util.List;

/**
 * Interface for appointment request service operations.
 * This interface defines methods for managing appointment requests, 
 * including retrieving, accepting, declining, and processing requests.
 */
public interface IAppointmentRequestService {

    /**
     * Accepts an appointment request.
     *
     * @param request The appointment request to be accepted.
     */
    void acceptRequest(AppointmentRequest request);

    /**
     * Retrieves all pending appointment requests.
     *
     * @return A list of pending appointment requests.
     */
    List<AppointmentRequest> getPendingRequests();

    /**
     * Processes all pending requests for a specific doctor.
     *
     * @param doctorID The ID of the doctor for whom the pending requests are being processed.
     */
    void processPendingRequests(String doctorID);

    /**
     * Declines an appointment request.
     *
     * @param request The appointment request to be declined.
     */
    void declineRequest(AppointmentRequest request);

    /**
     * Saves an appointment request to the system.
     *
     * @param request The appointment request to be saved.
     */
    void saveAppointmentRequest(AppointmentRequest request);

    /**
     * Retrieves all appointment requests in the system.
     *
     * @return A list of all appointment requests.
     */
    List<AppointmentRequest> getAllRequests();
}

