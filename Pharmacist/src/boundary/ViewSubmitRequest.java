package boundary;
import models.Pharmacist;

import interfaces.IPharmacistView;
import java.util.*;

public class ViewSubmitRequest {

	private final IPharmacistView pharmacistView;




	    public ViewSubmitRequest(IPharmacistView pharmacistView) {
	        this.pharmacistView = pharmacistView;
	    }

	    /**
	     * Displays the interface to submit a replenishment request.
	     */
	    public void displaySubmitRequest(Pharmacist pharmacist) {
	        Scanner scanner = new Scanner(System.in);

	        System.out.println("\n=== Submit Replenishment Request ===");
	        System.out.print("Enter the name of the medicine to request replenishment: ");
	        String medicineName = scanner.nextLine().trim();

	        System.out.print("Enter the quantity to request: ");
	        int quantity = scanner.nextInt();
	        scanner.nextLine();  // Consume the newline character

	        // Pharmacist submits the request through the view
	        pharmacistView.submitReplenishmentRequest(pharmacist, medicineName, quantity);

	        System.out.println("Replenishment request for " + medicineName + " has been submitted.");
	        scanner.close();
	    }
	}



