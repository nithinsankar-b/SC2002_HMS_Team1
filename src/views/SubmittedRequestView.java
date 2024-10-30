package views;
import models.Pharmacist;

import java.util.*;
import services.InventoryService;

public class SubmittedRequestView {

	private final InventoryService pharmacistView;




	    public SubmittedRequestView(InventoryService pharmacistView) {
	        this.pharmacistView = pharmacistView;
	    }

	    /**
	     * Displays the interface to submit a replenishment request.
	     */
	    public void displayRequests(Pharmacist pharmacist) {
	        Scanner scanner = new Scanner(System.in);

	        System.out.println("\n=== Submit Replenishment Request ===");
	        System.out.print("Enter the name of the medicine to request replenishment: ");
	        String medicineName = scanner.nextLine().trim();


	        // Pharmacist submits the request through the view
	        pharmacistView.submitReplenishmentRequest(medicineName);

	        System.out.println("Replenishment request for " + medicineName + " has been submitted.");
	        //scanner.close();
	    }
	}



