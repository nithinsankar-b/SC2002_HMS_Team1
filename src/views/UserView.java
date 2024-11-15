package views;

import controllers.AdministratorController;
import controllers.PatientController;
import controllers.PharmacistController;
import controllers.DoctorController;
import controllers.UserController;
import enums.UserRole;
import services.UserService;
import services.AppointmentService;
import services.InventoryService;
import services.PatientService;
import services.ProjectAdminService;
import services.ReplenishmentService;
import models.Administrator;
import services.PharmacistService;
import services.DoctorService;
import services.ScheduleService;
import services.AppointmentRequestService;
import services.MedicalRecordService;
import controllers.BillingController;
import services.BillingService;
import views.DoctorView;
import controllers.DoctorController;
import views.PharmacistView;
import stores.InventoryDataStore;
import models.User;
import views.PatientView;

import java.io.Console;
import java.util.Scanner;

/**
 * The UserView class is responsible for handling the user interface
 * for the Hospital Management System (HMS). It manages user
 * authentication and navigates users to their role-specific views
 * based on their roles (Patient, Doctor, Pharmacist, Administrator).
 */
public class UserView {
    private final UserService userService;
    private String loggedInHospitalID;

    // Centralized messages for consistency
    private static final String WELCOME_MESSAGE = "***WELCOME TO HOSPITAL MANAGEMENT SYSTEM***";
    private static final String SEPARATOR = "===========================================";

    /**
     * Constructs a UserView instance with the specified UserService.
     *
     * @param userService the UserService used for user-related operations
     */
    public UserView(UserService userService) {
        this.userService = userService;
    }

    /**
     * Displays the login interface for the user.
     * This method runs in a loop until the user successfully logs in
     * or chooses to exit the application.
     */
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

    /**
     * Attempts to log in the user by prompting for their Hospital ID
     * and password, allowing up to three attempts.
     *
     * @param scanner the Scanner object for user input
     * @return true if authentication is successful, false otherwise
     */
    private boolean attemptLogin(Scanner scanner) {
        int attempts = 3; // Maximum number of attempts allowed
        boolean isAuthenticated = false;

        while (attempts > 0 && !isAuthenticated) {
            System.out.print("Enter Hospital ID (Case Sensitive): ");
            String hospitalID = scanner.nextLine();

            //System.out.print("Enter Password: ");
            Console console = System.console();
            char[] passwordArray = console.readPassword("Enter Password: ");
            String password = new String(passwordArray);
            //String password = scanner.nextLine();

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

    /**
     * Prompts the user to decide whether to exit the application
     * or attempt to log in again.
     *
     * @param scanner the Scanner object for user input
     * @return true to retry login, false to exit the application
     */
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

    /**
     * Navigates to the role-specific page based on the user's role.
     *
     * @param user the User object representing the logged-in user
     * @param role the UserRole of the logged-in user
     */
    private void navigateToRoleSpecificPage(User user, UserRole role) {
        switch (role) {
            case PATIENT:
                // Create necessary services for Patient role
                AppointmentService appointmentService = new AppointmentService();
                PatientService patientService = new PatientService(userService);
                BillingService billingService=new BillingService();
                BillingController billingController=new BillingController();
                PatientController patientController = new PatientController(patientService, appointmentService);
                PatientView patientView = new PatientView(patientController, userService,billingController);

                System.out.println("Navigating to Patient view...");
                System.out.println(SEPARATOR);
                patientView.start(user);
                break;

            case DOCTOR:
                // Create necessary services for Doctor role
                ScheduleService scheduleService = new ScheduleService();
                MedicalRecordService medicalRecordService = new MedicalRecordService();
                appointmentService = new AppointmentService();
                AppointmentRequestService appointmentRequestService = new AppointmentRequestService(scheduleService, appointmentService);
                DoctorService doctorService = new DoctorService(userService, scheduleService, medicalRecordService, appointmentService);
                DoctorController doctorController = new DoctorController(doctorService, scheduleService, medicalRecordService, appointmentService);
                DoctorView doctorView = new DoctorView(doctorController, doctorService, userService, scheduleService, medicalRecordService, appointmentService);

                System.out.println("Navigating to Doctor view...");
                System.out.println(SEPARATOR);
                doctorView.start(user);
                break;

            case PHARMACIST:
                // Create necessary services for Pharmacist role
                InventoryDataStore inventoryDataStore = new InventoryDataStore();
                InventoryService inventoryService = new InventoryService(inventoryDataStore);
                ReplenishmentService replenishmentService = new ReplenishmentService();
                appointmentService = new AppointmentService();
                PharmacistService pharmacistService = new PharmacistService(userService, appointmentService, inventoryService);
                patientService = new PatientService(userService);
                PharmacistController pharmacistController = new PharmacistController(pharmacistService, inventoryService, appointmentService, replenishmentService);
                PharmacistView pharmacistView = new PharmacistView(pharmacistController, pharmacistService, userService, patientService);

                System.out.println("Navigating to Pharmacist view...");
                System.out.println(SEPARATOR);
                pharmacistView.start(user);
                break;

            case ADMINISTRATOR:
                navigateToAdministratorPage(user);
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
        BillingService billingService=new BillingService();
        BillingController billingController=new BillingController();

        // Instantiate PatientView
        PatientView patientView = new PatientView(patientController, userService, billingController);

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
        ReplenishmentService replenishmentService = new ReplenishmentService();
        // Instantiate PatientController
        PharmacistController pharmacistController = new PharmacistController(pharmacistService, inventoryService,appointmentService, replenishmentService);

        // Instantiate PatientView
        PharmacistView pharmacistView = new PharmacistView(pharmacistController, pharmacistService, userService, patientService);

        // Start the patient operations (menu)
        System.out.println("Navigating to Pharmacist view...");
        System.out.println(SEPARATOR);
        pharmacistView.start(user);
    }

    private void navigateToDoctorPage(User user) {
        // Create the necessary services
        ScheduleService scheduleService = new ScheduleService();

        MedicalRecordService medicalRecordService = new MedicalRecordService();

        AppointmentService appointmentService = new AppointmentService();

        AppointmentRequestService appointmentRequestService = new AppointmentRequestService(scheduleService, appointmentService);

        DoctorService doctorService = new DoctorService( userService,scheduleService,medicalRecordService, appointmentService);

        // Instantiate DoctorController
        DoctorController doctorController = new DoctorController(doctorService,  scheduleService,  medicalRecordService, appointmentService);

        // Instantiate DoctorView
        DoctorView doctorView = new DoctorView( doctorController, doctorService, userService, scheduleService, medicalRecordService, appointmentService);

        // Start the doctor operations (menu)
        System.out.println("Navigating to Doctor view...");
        System.out.println(SEPARATOR);
        doctorView.start(user);
    }


    private void navigateToAdministratorPage(User user) {
        // Create necessary services for Administrator
        InventoryDataStore inventoryDataStore = new InventoryDataStore();
        InventoryService inventoryService = new InventoryService(inventoryDataStore);
        ProjectAdminService adminService = new ProjectAdminService(new Administrator(user.getHospitalID(), user.getPassword(), null), inventoryService,userService);
        AppointmentService appointmentService = new AppointmentService();
    
        // Instantiate AdministratorController
        AdministratorController adminController = new AdministratorController(appointmentService, adminService, userService);
    
        // Start the administrator operations (menu), passing in the logged-in hospital ID
        System.out.println("Navigating to Administrator view...");
        System.out.println(SEPARATOR);
        adminController.start(user.getHospitalID());
    }


    /**
     * Displays the change password interface for the logged-in user.
     * It prompts for the old password and the new password, and attempts
     * to change the password using the UserService.
     */
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
