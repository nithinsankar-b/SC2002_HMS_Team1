package controllers;

import models.InventoryDisplay;
import models.Patient;
import models.User;
import services.PharmacistService;
import services.UserService;
import services.InventoryService;
import services.PatientService;
import services.AppointmentService;
import src.views.AppointmentOutcomeRecordView;
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
	        //System.out.print("Enter the medicine name for replenishment: ");
	        //String medicineName = scanner.nextLine().trim();

	        inventoryService.submitReplenishmentRequest(); // Pass the medicine name to the service
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



