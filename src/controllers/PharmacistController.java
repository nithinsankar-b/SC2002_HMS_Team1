package controllers;

import models.InventoryDisplay;
<<<<<<< HEAD
=======

>>>>>>> parent of a4ef980 (Merge pull request #26 from nithinsankar-b/pharma)
import models.Patient;
import models.User;
import services.PharmacistService;
import services.UserService;
import services.InventoryService;
import services.PatientService;
import services.AppointmentService;
import views.AppointmentOutcomeRecordView;
import views.MedicalInventoryView;

import java.util.List;
import java.util.Scanner;

import enums.MedicationStatus;

/**
 * Controller class for handling pharmacist-related operations.
 * This class coordinates between services and views for the pharmacist to manage 
 * medication inventory, view appointment outcomes, update prescription statuses, 
 * and handle user account management functions such as password changes.
 */
public class PharmacistController {
<<<<<<< HEAD

    private final PharmacistService pharmacistService;
    private final InventoryService inventoryService;
    private final AppointmentService appointmentService;
    private final MedicalInventoryView medicalInventoryView;
    private final UserService userService;
    private final AppointmentOutcomeRecordView appointmentOutcomeRecordView;

    /**
     * Constructs a PharmacistController with specified services.
     *
     * @param pharmacistService    the service for pharmacist-related operations
     * @param inventoryService     the service for managing inventory
     * @param appointmentService   the service for managing appointments
     */
    public PharmacistController(PharmacistService pharmacistService, 
                                InventoryService inventoryService, 
                                AppointmentService appointmentService) {
        this.pharmacistService = pharmacistService;
        this.inventoryService = inventoryService;
        this.appointmentService = appointmentService;
        this.medicalInventoryView = new MedicalInventoryView(inventoryService);
        this.appointmentOutcomeRecordView = new AppointmentOutcomeRecordView();
        this.userService = new UserService();
    }

    /**
     * Displays the medication inventory.
     * Retrieves the list of inventory items and displays them using the MedicalInventoryView.
     */
    public void viewMedicationInventory() {
        List<InventoryDisplay> inventory = inventoryService.getInventoryDisplay();
        medicalInventoryView.display(inventory);
    }

    /**
     * Submits a replenishment request for a specified medication.
     * Prompts the user to enter the medicine name and submits the request using InventoryService.
     */
    public void submitReplenishmentRequest() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the medicine name for replenishment: ");
        String medicineName = scanner.nextLine().trim();

        inventoryService.submitReplenishmentRequest(medicineName);
    }

    /**
     * Updates the prescription status for a specified appointment.
     *
     * @param appointmentId the ID of the appointment whose prescription status is to be updated
     */
    public void updatePrescription(String appointmentId) {
        boolean isUpdated = pharmacistService.updatePrescriptionStatus(appointmentId);
        if (isUpdated) {
            System.out.println("Prescription status updated successfully.");
        } else {
            System.err.println("Failed to update prescription status. Please check the appointment ID.");
        }
    }

    /**
     * Views the records of appointment outcomes.
     * Uses AppointmentOutcomeRecordView to load and display the outcomes.
     */
    public void viewAppointmentOutcomeRecords() {
        appointmentOutcomeRecordView.loadAndPrintAppointments();
    }

    /**
     * Changes the password for the user account.
     *
     * @param hospitalID   the ID of the hospital associated with the account
     * @param oldPassword  the current password of the user
     * @param newPassword  the new password to set for the user
     */
    public void changePassword(String hospitalID, String oldPassword, String newPassword) {
        userService.changePassword(hospitalID, oldPassword, newPassword);
    }
}
=======
	

	    private final PharmacistService pharmacistService;
	    private final InventoryService inventoryService;
	    private final AppointmentService appointmentService;
	    private final MedicalInventoryView medicalInventoryView;
	    
	    private final AppointmentOutcomeRecordView appointmentOutcomeRecordView;

	    // Constructor
	    public PharmacistController(PharmacistService pharmacistService, 
	                                InventoryService inventoryService, 
	                                AppointmentService appointmentService) {
	        this.pharmacistService = pharmacistService;
	        this.inventoryService = inventoryService;
	        this.appointmentService = appointmentService;
	        

	        // Initialize only necessary views
	        this.medicalInventoryView = new MedicalInventoryView(inventoryService);
	        
	        this.appointmentOutcomeRecordView = new AppointmentOutcomeRecordView();
	    }

	    // Method to view medication inventory
	    public void viewMedicationInventory() {
	        List<InventoryDisplay> inventory = inventoryService.getInventoryDisplay();
	        medicalInventoryView.display(inventory);
	    }

	   
	    
	    // Method to submit replenishment request
	    public void submitReplenishmentRequest() {
	        Scanner scanner = new Scanner(System.in);
	        System.out.print("Enter the medicine name for replenishment: ");
	        String medicineName = scanner.nextLine().trim();

	        inventoryService.submitReplenishmentRequest(medicineName); // Pass the medicine name to the service
	    }

	    // Method to handle updating prescription status
	    public void updatePrescription(String appointmentId) {
	        boolean isUpdated = pharmacistService.updatePrescriptionStatus(appointmentId);
	        if (isUpdated) {
	            System.out.println("Prescription status updated successfully.");
	        } else {
	            System.err.println("Failed to update prescription status. Please check the appointment ID.");
	        }
	    }

	    // New method to view appointment outcome records
	    public void viewAppointmentOutcomeRecords() {
	    	appointmentOutcomeRecordView.loadAndPrintAppointments();
	    }
	}



>>>>>>> parent of a4ef980 (Merge pull request #26 from nithinsankar-b/pharma)
