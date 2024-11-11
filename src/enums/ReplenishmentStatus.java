package enums;

/**
 * Enumeration representing the status of a replenishment request for inventory.
 * This enum is used to indicate whether a replenishment request is awaiting fulfillment or has been completed.
 */
public enum ReplenishmentStatus {
    
    /**
     * Indicates that the replenishment request is pending and has not yet been fulfilled.
     */
    PENDING,

    /**
     * Indicates that the replenishment request has been fulfilled and inventory has been replenished.
     */
    APPROVED
}
