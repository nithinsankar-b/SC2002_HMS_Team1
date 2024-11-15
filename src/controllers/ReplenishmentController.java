package controllers;

import java.util.List;

import enums.StatusEnum;
import models.ReplenishmentRequest;
import services.ReplenishmentService;

public class ReplenishmentController {

    private final ReplenishmentService service = new ReplenishmentService();

    // Pharmacist actions
    public List<ReplenishmentRequest> sendReplenishmentRequest(List<String> medicines) {
        return service.createReplenishmentRequest(medicines);
    }

    public List<ReplenishmentRequest> viewPendingRequests() {
        return service.getRequestsByStatus(StatusEnum.PENDING);
    }

    public List<ReplenishmentRequest> viewPastRequests() {
        List<ReplenishmentRequest> approved = service.getRequestsByStatus(StatusEnum.APPROVED);
        List<ReplenishmentRequest> rejected = service.getRequestsByStatus(StatusEnum.REJECTED);
        approved.addAll(rejected);
        return approved;
    }

    // Administrator actions
    public List<ReplenishmentRequest> viewPendingRequestsAdmin() {
        return service.getRequestsByStatus(StatusEnum.PENDING);
    }

    public void approveRequest(String requestId) {
        service.updateRequestStatus(requestId, StatusEnum.APPROVED);
    }

    public void rejectRequest(String requestId) {
        service.updateRequestStatus(requestId, StatusEnum.REJECTED);
    }
}
