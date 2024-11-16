package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import enums.StatusEnum;

/**
 * The `ReplenishmentRequest` class models a request for replenishing low-stock medicines.
 * Each request contains an ID, a list of medicines, a status, and the date of the request.
 */
public class ReplenishmentRequest {

    private String id; // Unique identifier for the request
    private List<String> medicines; // List of medicines to be replenished
    private StatusEnum status; // Current status of the request (PENDING, APPROVED, REJECTED)
    private LocalDateTime requestDate; // Timestamp of when the request was created

    /**
     * Constructor for creating a new replenishment request.
     * Automatically generates a unique ID and sets the status to PENDING.
     *
     * @param medicines The list of medicines to be replenished.
     */
    public ReplenishmentRequest(List<String> medicines) {
        this.id = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmmss"));
        this.medicines = medicines;
        this.status = StatusEnum.PENDING;
        this.requestDate = LocalDateTime.now();
    }

    /**
     * Constructor for loading a replenishment request from a CSV file.
     *
     * @param id        The unique identifier of the request.
     * @param medicines The list of medicines in the request.
     * @param status    The status of the request (PENDING, APPROVED, REJECTED).
     */
    public ReplenishmentRequest(String id, List<String> medicines, StatusEnum status) {
        this.id = id;
        this.medicines = medicines;
        this.status = status;
        this.requestDate = LocalDateTime.now(); // Adjust as necessary if loading from CSV with timestamp
    }

    /**
     * Retrieves the unique identifier of the replenishment request.
     *
     * @return The ID of the request.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the unique identifier of the replenishment request.
     *
     * @param id The new ID for the request.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retrieves the list of medicines in the replenishment request.
     *
     * @return The list of medicines.
     */
    public List<String> getMedicines() {
        return medicines;
    }

    /**
     * Retrieves the current status of the replenishment request.
     *
     * @return The status of the request (PENDING, APPROVED, REJECTED).
     */
    public StatusEnum getStatus() {
        return status;
    }

    /**
     * Updates the status of the replenishment request.
     *
     * @param status The new status of the request (PENDING, APPROVED, REJECTED).
     */
    public void setStatus(StatusEnum status) {
        this.status = status;
    }

    /**
     * Converts the replenishment request into a CSV-compatible string format.
     *
     * @return The replenishment request as a CSV-formatted string.
     */
    public String toCsvFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedDate = requestDate.format(formatter);
        String medicinesConcatenated = String.join(";", medicines);

        return id + "," + medicinesConcatenated + "," + status + "," + formattedDate;
    }
}


