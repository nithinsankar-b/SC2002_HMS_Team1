package views;

import controllers.PharmacistController;
import models.Pharmacist;
import models.User;
import services.PharmacistService;
import services.UserService;
import services.PatientService;

import java.util.Scanner;

public class PharmacistView {
    private final Scanner scanner;
    private final PharmacistController pharmacistController;
    private final PharmacistService pharmacistService;
    private final UserService userService;
    private final PatientService patientService;

    public PharmacistView(PharmacistController pharmacistController, PharmacistService pharmacistService, 
                          UserService userService, PatientService patientService) {
        this.scanner = new Scanner(System.in); // Managed centrally, do not close here
        this.pharmacistController = pharmacistController;
        this.pharmacistService = pharmacistService;
        this.userService = userService;
        this.patientService = patientService;
    }

    // Main method to handle different pharmacist operations
    public void start(User user) {
        Pharmacist pharmacist = pharmacistService.getPharmacistById(user.getHospitalID());

        if (pharmacist == null) {
            System.out.println("Pharmacist record not found for user: " + user.getHospitalID());
            System.out.println("Do you want to register a new pharmacist? (Y/N): ");
            String userInput = scanner.nextLine().trim().toUpperCase();

            if (userInput.equals("Y")) {
                pharmacist = registerPharmacist(user);
            } else {
                System.out.println("Unable to proceed without pharmacist information.");
                return;
            }
        }

        boolean isRunning = true;
        while (isRunning) {
            displayMenu();
            int choice = getUserInput();

            switch (choice) {
                case 1 -> pharmacistController.viewMedicationInventory();
                
                case 2 -> pharmacistController.submitReplenishmentRequest();
                case 3 -> updatePrescriptionStatus(); 
                case 4 -> viewAppointmentOutcomeRecords(); 
                case 5 -> changePassword();
                case 6 -> {
                    System.out.println("Logging out...");
                    isRunning = false;
                }
                default -> System.out.println("ERROR: Invalid choice, please try again.");
            }

            if (isRunning) {
                System.out.println("\nDo you want to continue (Y/N): ");
                String continueInput = scanner.nextLine().trim().toUpperCase();

                if (continueInput.equals("N")) {
                    isRunning = false;
                } else if (!continueInput.equals("Y")) {
                    System.out.println("Invalid input. Please enter Y or N.");
                } else {
                    System.out.println("==============================\n");
                }
            }
        }
    }

    // Method to register a new pharmacist if not found
    private Pharmacist registerPharmacist(User user) {
        System.out.print("Enter Pharmacist Name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter Contact Information: ");
        String contactInformation = scanner.nextLine().trim();

        Pharmacist pharmacist = new Pharmacist(user, name, contactInformation);
        pharmacistService.addPharmacist(pharmacist);
        System.out.println("New pharmacist registered successfully.");

        return pharmacist;
    }

    // Method to update prescription status
    private void updatePrescriptionStatus() {
        System.out.print("Enter Appointment ID: ");
        String appointmentId = scanner.nextLine().trim();

        // Call the controller method to update the prescription
        pharmacistController.updatePrescription(appointmentId);
    }
// Method to view appointment outcome records
    private void viewAppointmentOutcomeRecords() {
        System.out.println("Viewing Appointment Outcome Records...");
        pharmacistController.viewAppointmentOutcomeRecords();
    }

    // Display menu options
    private void displayMenu() {
        System.out.println("Please choose an option:");
        System.out.println("1. View Medication Inventory");
        System.out.println("2. Submit Replenishment Request");
        System.out.println("3. Update Prescription Status");
        System.out.println("4. View Appointment Outcome Record"); // New menu option
        System.out.println("5. Change Password");
        System.out.println("6. Log Out");
    }

    // Get user input with error checking
    private int getUserInput() {
        int choice = -1;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("ERROR: Invalid input. Please enter a number between 1 and 6.");
        }
        return choice;
    }
    
    private void changePassword()
    {
      Scanner sc=new Scanner(System.in);
      System.out.println("Enter Hospital ID");
      String id = sc.nextLine();
      
      System.out.println("Enter old password");
      String oldPassword = sc.nextLine();
      
      System.out.println("Enter new password");
      String newPassword = sc.nextLine();
      
      pharmacistController.changePassword(id, oldPassword, newPassword);
    }
    }