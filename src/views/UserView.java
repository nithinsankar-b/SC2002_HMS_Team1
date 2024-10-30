package views;

import controllers.PatientController;
import controllers.PharmacistController;
import enums.UserRole;
import services.UserService;
import stores.InventoryDataStore;
import services.AppointmentService;
import services.InventoryService;
import services.PatientService;
import services.PharmacistService;
import views.PharmacistView;
import views.PatientView;
import models.User;

import java.util.Scanner;

public class UserView {
    private final UserService userService;
    private String loggedInHospitalID;

    // Centralized messages for consistency
    private static final String WELCOME_MESSAGE = "***WELCOME TO HOSPITAL MANAGEMENT SYSTEM***";
    private static final String SEPARATOR = "===========================================";

    public UserView(UserService userService) {
        this.userService = userService;
    }

    public void displayLogin() {

        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                System.out.println("\n" + SEPARATOR);
                System.out.println(WELCOME_MESSAGE);
                System.out.println(SEPARATOR);

                boolean isAuthenticated = attemptLogin(scanner);

                if (!isAuthenticated) {
                    System.out.println("Maximum attempts reached.");
                }

                // Ask if the user wants to exit or try logging in again
                running = promptToExitOrRetry(scanner);
            }
        } catch (Exception e) {
        	e.printStackTrace(); // Print stack trace for better debugging
            System.out.println("Error encountered: " + e.getMessage());
            System.out.println("Error encountered. Please reboot HMS Application.");
        } finally {
            System.out.println("Thank You. Exiting HMS.");
        }
    }

    private boolean attemptLogin(Scanner scanner) {
        int attempts = 3; // Maximum number of attempts allowed
        boolean isAuthenticated = false;

        while (attempts > 0 && !isAuthenticated) {
            System.out.print("Enter Hospital ID (Case Sensitive): ");
            String hospitalID = scanner.nextLine();

            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            if (userService.login(hospitalID, password)) {
                loggedInHospitalID = hospitalID;
                System.out.println("Login successful!");
                UserRole role = userService.getUserRole(hospitalID);

                // Creating the User object
                User user = new User(hospitalID, password, role);

                System.out.println("Role: " + role);
                System.out.println(SEPARATOR);

                // Navigate to appropriate role-specific view
                navigateToRoleSpecificPage(user, role);
                isAuthenticated = true;
            } else {
                attempts--;
                System.out.println("Invalid Hospital ID or Password.");
                if (attempts > 0) {
                    System.out.println("You have " + attempts + " attempt(s) remaining.\n");
                }
            }
        }

        return isAuthenticated;
    }

    private boolean promptToExitOrRetry(Scanner scanner) {
        while (true) {
            System.out.print("Would you like to exit the application? (yes/no): ");
            String response = scanner.nextLine().trim().toLowerCase();

            if (response.equals("yes")) {
                return false; // Exit the loop and end the program
            } else if (response.equals("no")) {
                System.out.println("Returning to main login screen...");
                return true; // Continue running for another login attempt
            } else {
                System.out.println("Invalid response. Please enter 'yes' or 'no'.");
            }
        }
    }

    private void navigateToRoleSpecificPage(User user, UserRole role) {
        switch (role) {
            case PATIENT:
                navigateToPatientPage(user);
                break;

            case DOCTOR:
                System.out.println("Navigating to Doctor view...");
                System.out.println(SEPARATOR);
                // Implement the DoctorView and corresponding logic here
                break;

            case PHARMACIST:
                navigateToPharmacistPage(user);
                break;

            case ADMINISTRATOR:
                System.out.println("Navigating to Administrator view...");
                System.out.println(SEPARATOR);
                // Implement the AdministratorView and corresponding logic here
                break;

            default:
                System.out.println("Role not recognized.");
        }
    }

    private void navigateToPatientPage(User user) {
        // Create the AppointmentService and PatientService instances
        AppointmentService appointmentService = new AppointmentService();
        PatientService patientService = new PatientService(userService);

        // Instantiate PatientController
        PatientController patientController = new PatientController(patientService, appointmentService);

        // Instantiate PatientView
        PatientView patientView = new PatientView(patientController, userService);

        // Start the patient operations (menu)
        System.out.println("Navigating to Patient view...");
        System.out.println(SEPARATOR);
        patientView.start(user);
    }
    
    private void navigateToPharmacistPage(User user) {
        // Create the AppointmentService and PatientService instances
        AppointmentService appointmentService = new AppointmentService();
        InventoryDataStore inventoryDataStore = new InventoryDataStore();
        InventoryService inventoryService = new InventoryService(inventoryDataStore);
        PharmacistService pharmacistService = new PharmacistService(userService, appointmentService, inventoryService);
        PatientService patientService = new PatientService(userService);
        // Instantiate PatientController
        PharmacistController pharmacistController = new PharmacistController(pharmacistService, inventoryService,appointmentService);

        // Instantiate PatientView
        PharmacistView pharmacistView = new PharmacistView(pharmacistController, pharmacistService, userService, patientService);

        // Start the patient operations (menu)
        System.out.println("Navigating to Pharmacist view...");
        System.out.println(SEPARATOR);
        pharmacistView.start(user);
    }

    public void displayChangePassword() {
        try (Scanner scanner = new Scanner(System.in)) {
            // No need to ask for Hospital ID again
            System.out.print("Enter Old Password: ");
            String oldPassword = scanner.nextLine();

            System.out.print("Enter New Password: ");
            String newPassword = scanner.nextLine();

            if (userService.changePassword(loggedInHospitalID, oldPassword, newPassword)) {
                System.out.println("Password changed successfully!");
            } else {
                System.out.println("Old password is incorrect. Password change failed.");
            }
        }
    }
}