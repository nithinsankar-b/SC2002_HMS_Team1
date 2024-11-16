package controllers;

import java.util.List;

import enums.StatusEnum;
import models.ReplenishmentRequest;
import services.ReplenishmentService;

/**
 * Controller class for managing replenishment requests.
 * Handles actions for both pharmacists and administrators.
 */
public class ReplenishmentController {

    private final ReplenishmentService service = new ReplenishmentService();

    /**
     * Sends a replenishment request for the specified medicines.
     * This action is performed by the pharmacist.
     *
     * @param medicines List of medicine names to request replenishment for.
     * @return A list of created replenishment requests.
     */
    public List<ReplenishmentRequest> sendReplenishmentRequest(List<String> medicines) {
        return service.createReplenishmentRequest(medicines);
    }

    /**
     * Retrieves a list of pending replenishment requests.
     * This action is performed by the pharmacist.
     *
     * @return A list of pending replenishment requests.
     */
    public List<ReplenishmentRequest> viewPendingRequests() {
        return service.getRequestsByStatus(StatusEnum.PENDING);
    }

    /**
     * Retrieves a list of past replenishment requests, including approved and rejected requests.
     * This action is performed by the pharmacist.
     *
     * @return A list of past replenishment requests.
     */
    public List<ReplenishmentRequest> viewPastRequests() {
        List<ReplenishmentRequest> approved = service.getRequestsByStatus(StatusEnum.APPROVED);
        List<ReplenishmentRequest> rejected = service.getRequestsByStatus(StatusEnum.REJECTED);
        approved.addAll(rejected);
        return approved;
    }

    /**
     * Retrieves a list of pending replenishment requests for the administrator's review.
     *
     * @return A list of pending replenishment requests.
     */
    public List<ReplenishmentRequest> viewPendingRequestsAdmin() {
        return service.getRequestsByStatus(StatusEnum.PENDING);
    }

    /**
     * Approves a replenishment request with the specified request ID.
     * This action is performed by the administrator.
     *
     * @param requestId The ID of the replenishment request to approve.
     */
    public void approveRequest(String requestId) {
        service.updateRequestStatus(requestId, StatusEnum.APPROVED);
    }

    /**
     * Rejects a replenishment request with the specified request ID.
     * This action is performed by the administrator.
     *
     * @param requestId The ID of the replenishment request to reject.
     */
    public void rejectRequest(String requestId) {
        service.updateRequestStatus(requestId, StatusEnum.REJECTED);
    }
}

