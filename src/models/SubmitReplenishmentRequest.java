package models;

/**
 * The SubmitReplenishmentRequest class represents a request to replenish the stock of a specific medication.
 * It includes details about the medication, current stock levels, and the requested replenishment amount.
 */
public class SubmitReplenishmentRequest {

    private String medicineName;
    private int currentStock;
    private int requestedAmount;

    /**
     * Constructs a SubmitReplenishmentRequest with the specified details.
     *
     * @param medicineName   The name of the medication.
     * @param currentStock   The current stock level of the medication.
     * @param requestedAmount The amount of medication requested for replenishment.
     */
    public SubmitReplenishmentRequest(String medicineName, int currentStock, int requestedAmount) {
        this.medicineName = medicineName;
        this.currentStock = currentStock;
        this.requestedAmount = requestedAmount;
    }

    /**
     * Retrieves the name of the medication.
     *
     * @return The name of the medication.
     */
    public String getMedicineName() {
        return medicineName;
    }

    /**
     * Sets the name of the medication.
     *
     * @param medicineName The new name of the medication.
     */
    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    /**
     * Retrieves the current stock level of the medication.
     *
     * @return The current stock level.
     */
    public int getCurrentStock() {
        return currentStock;
    }

    /**
     * Sets the current stock level of the medication.
     *
     * @param currentStock The new stock level.
     */
    public void setCurrentStock(int currentStock) {
        this.currentStock = currentStock;
    }

    /**
     * Retrieves the requested replenishment amount for the medication.
     *
     * @return The requested replenishment amount.
     */
    public int getRequestedAmount() {
        return requestedAmount;
    }

    /**
     * Sets the requested replenishment amount for the medication.
     *
     * @param requestedAmount The new requested replenishment amount.
     */
    public void setRequestedAmount(int requestedAmount) {
        this.requestedAmount = requestedAmount;
    }

    /**
     * Approves the replenishment request.
     * This method is currently unimplemented.
     */
    public void approve() {
        throw new UnsupportedOperationException("Unimplemented method 'approve'");
    }
}

