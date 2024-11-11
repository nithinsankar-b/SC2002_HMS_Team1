package interfaces;

import models.AppointmentRequest;
import java.util.List;

/**
 * Interface for appointment request service operations.
 * This interface defines methods for managing appointment requests, including retrieving pending requests and handling acceptance or decline of requests.
 */
public interface IAppointmentRequestService {

	void acceptRequest(AppointmentRequest request);

    List<AppointmentRequest> getPendingRequests();

    void processPendingRequests(String doctorID);

    void declineRequest(AppointmentRequest request);

    void saveAppointmentRequest(AppointmentRequest request);

    void save(AppointmentRequest request);

    List<AppointmentRequest> getAllRequests();
}
