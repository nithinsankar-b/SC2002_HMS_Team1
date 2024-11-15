package interfaces;

import enums.StatusEnum;
import models.ReplenishmentRequest;

import java.util.List;

public interface IReplenishmentService {

    /**
     * Ensures the CSV file exists with the required headers.
     */
    void generateCsvIfNotExists();

    /**
     * Creates replenishment requests for the given list of medicines.
     *
     * @param medicines List of medicine names to request.
     * @return List of newly created replenishment requests.
     */
    List<ReplenishmentRequest> createReplenishmentRequest(List<String> medicines);

    /**
     * Writes a replenishment request to the CSV file.
     *
     * @param request The replenishment request to write.
     */
    void writeToCsv(ReplenishmentRequest request);

    /**
     * Retrieves all replenishment requests from the CSV file.
     *
     * @return List of all replenishment requests.
     */
    List<ReplenishmentRequest> getAllRequests();

    /**
     * Retrieves replenishment requests by their status.
     *
     * @param status The status to filter by.
     * @return List of replenishment requests matching the given status.
     */
    List<ReplenishmentRequest> getRequestsByStatus(StatusEnum status);

    /**
     * Parses a CSV row into a ReplenishmentRequest object.
     *
     * @param csvRow A row from the CSV file.
     * @return Parsed ReplenishmentRequest object or null if invalid.
     */
    ReplenishmentRequest parseCsvRow(String csvRow);

    /**
     * Checks if a medicine already has a pending replenishment request.
     *
     * @param medicineName Name of the medicine.
     * @return True if a pending request exists, false otherwise.
     */
    boolean isMedicineAlreadyRequested(String medicineName);

    /**
     * Approves a replenishment request by updating its status to APPROVED.
     *
     * @param requestId ID of the request to approve.
     * @return True if the request was successfully updated, false otherwise.
     */
    boolean approveRequest(String requestId);

    /**
     * Rejects a replenishment request by updating its status to REJECTED.
     *
     * @param requestId ID of the request to reject.
     * @return True if the request was successfully updated, false otherwise.
     */
    boolean rejectRequest(String requestId);

    /**
     * Updates the status of a replenishment request by its ID.
     *
     * @param requestId ID of the request to update.
     * @param newStatus The new status to set.
     * @return True if the request was found and updated, false otherwise.
     */
    boolean updateRequestStatus(String requestId, StatusEnum newStatus);
}
