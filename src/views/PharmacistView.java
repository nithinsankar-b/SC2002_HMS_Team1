package views;

import controllers.PharmacistController;
import interfaces.IPharmacistView;
import models.Pharmacist;
import models.User;
import services.PharmacistService;
import services.UserService;
import services.PatientService;
import views.AppointmentOutcomeRecordView;

import java.util.Scanner;

/**
 * The {@code PharmacistView} class provides a user interface for pharmacists to interact 
 * with the system. It allows pharmacists to manage medications, update prescription statuses, 
 * view appointment outcomes, and change their passwords.
 */
public class PharmacistView implements IPharmacistView {
    private final Scanner scanner; // Scanner for user input
    private final PharmacistController pharmacistController; // Controller for pharmacist operations
    private final PharmacistService pharmacistService; // Service for managing pharmacists
    private final UserService userService; // Service for managing users
    private final PatientService patientService; // Service for managing patients

    /**
     * Constructs a {@code PharmacistView} with the specified services and controller.
     *
     * @param pharmacistController the controller for pharmacist operations
     * @param pharmacistService    the service for managing pharmacists
     * @param userService          the service for managing users
     * @param patientService       the service for managing patients
     */
    public PharmacistView(PharmacistController pharmacistController, PharmacistService pharmacistService,
                          UserService userService, PatientService patientService) {
        this.scanner = new Scanner(System.in); // Managed centrally, do not close here
        this.pharmacistController = pharmacistController;
        this.pharmacistService = pharmacistService;
        this.userService = userService;
        this.patientService = patientService;
    }

    /**
     * Starts the main interaction loop for the pharmacist, allowing them to perform various operations.
     *
     * @param user the user logged in as a pharmacist
     */
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
                case 3 -> pharmacistController.viewReplenishmentRequests();  // New case for viewing requests
                case 4 -> updatePrescriptionStatus();
                case 5 -> viewAppointmentOutcomeRecords();
                case 6 -> changePassword();
                case 7 -> {
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

    /**
     * Registers a new pharmacist if not found.
     *
     * @param user the user who is trying to register as a pharmacist
     * @return the newly registered pharmacist
     */
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

    /**
     * Updates the prescription status based on the appointment ID provided by the user.
     */
    private void updatePrescriptionStatus() {
System.out.println("Appointments with Pending Medication Status:");
    	

        AppointmentOutcomeRecordView appointmentOutcomeRecordView = new AppointmentOutcomeRecordView();
		// Print pending appointments using AppointmentOutcomeRecordView
        boolean f=appointmentOutcomeRecordView.loadAndPrintPendingAppointments();
        if(f) {
        System.out.print("Enter Appointment ID: ");
        String appointmentId = scanner.nextLine().trim();
     // Call the controller method to update the prescription
        pharmacistController.updatePrescription(appointmentId);
        }else {
        	System.out.println("No Appointments with Pending Medication Status");
        }
        
    }

    // Method to view appointment outcome records
    public void viewAppointmentOutcomeRecords() {
        System.out.println("Viewing Appointment Outcome Records...");
        pharmacistController.viewAppointmentOutcomeRecords();
    }

    /**
     * Displays the menu options available to the pharmacist.
     */
    public void displayMenu() {
        System.out.println("Please choose an option:");
        System.out.println("1. View Medication Inventory");
        System.out.println("2. Submit Replenishment Request");
        System.out.println("3. View Replenishment Requests");  // New option to view requests
        System.out.println("4. Update Prescription Status");
        System.out.println("5. View Appointment Outcome Record");
        System.out.println("6. Change Password");
        System.out.println("7. Log Out");
    }

    /**
     * Gets user input for menu selection with error checking.
     *
     * @return the user's choice as an integer
     */
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
