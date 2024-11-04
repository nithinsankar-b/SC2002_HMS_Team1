package controllers;
import models.InventoryDisplay;


import models.Patient;
import models.Pharmacist;
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

public class PharmacistController {
  

      private final PharmacistService pharmacistService;
      private final InventoryService inventoryService;
      private final AppointmentService appointmentService;
      private final MedicalInventoryView medicalInventoryView;
      private final UserService userService;
      
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
          this.userService = new UserService();
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
      
      public void changePassword(String hospitalID, String oldPassword, String newPassword)
      {
        
        userService.changePassword(hospitalID, oldPassword, newPassword);
      }
      
}