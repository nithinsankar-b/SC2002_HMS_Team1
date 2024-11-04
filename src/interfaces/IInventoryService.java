package interfaces;

/**
 * Interface for inventory service operations.
 * This interface defines methods related to inventory management, 
 * specifically for submitting replenishment requests.
 */
public interface IInventoryService {

    /**
     * Submits a replenishment request for a specified medicine.
     *
     * @param medicineName the name of the medicine to be replenished
     */
    void submitReplenishmentRequest(String medicineName);
}
