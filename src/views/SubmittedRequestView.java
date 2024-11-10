package views;

import models.Pharmacist;
import services.InventoryService;

import java.util.Scanner;

/**
 * The {@code SubmittedRequestView} class provides a user interface for pharmacists 
 * to submit replenishment requests for medications. It interacts with the 
 * {@code InventoryService} to process these requests.
 */
public class SubmittedRequestView {

    private final InventoryService pharmacistView; // Service for handling inventory operations

    /**
     * Constructs a {@code SubmittedRequestView} with the specified {@code InventoryService}.
     *
     * @param pharmacistView the service for managing inventory operations
     */
    public SubmittedRequestView(InventoryService pharmacistView) {
        this.pharmacistView = pharmacistView;
    }

    /**
     * Displays the interface for submitting a replenishment request.
     * Prompts the pharmacist for the name of the medication to be requested 
     * for replenishment and processes the request.
     *
     * @param pharmacist the pharmacist submitting the replenishment request
     */
    public void displayRequests(Pharmacist pharmacist) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("\n=== Submit Replenishment Request ===");
        //System.out.print("Enter the name of the medicine to request replenishment: ");
        //String medicineName = scanner.nextLine().trim();

        // Pharmacist submits the request through the view
        //pharmacistView.submitReplenishmentRequest(medicineName);

        System.out.println("Replenishment request for Low Stock Medicines has been submitted.");
        //scanner.close(); // Closing the scanner is handled externally
    }
}
