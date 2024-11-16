package controllers;

import services.UserService;
import controllers.BillingController;
import services.BillingService;
import views.UserView;
import enums.UserRole;
import models.User;
import services.AppointmentService;
import stores.InventoryDataStore;
import services.InventoryService;
import services.PatientService;
import services.PharmacistService;
import services.ReplenishmentService;
import services.DoctorService;
import services.ScheduleService;
import services.AppointmentRequestService;
import services.MedicalRecordService;
import controllers.PatientController;
import controllers.PharmacistController;
import controllers.DoctorController;
import views.PharmacistView;
import views.PatientView;
import views.DoctorView;

import java.util.Scanner;

/**
 * Controller class for handling user-related operations.
 * This includes user login, role-based navigation, and user account management functionalities.
 */
public class UserController {
    private final UserService userService;
    private final UserView userView;

    /**
     * Constructor for the UserController.
     *
     * @param userService The UserService instance for managing user-related operations.
     */
    public UserController(UserService userService) {
        this.userService = userService;
        this.userView = new UserView(userService);
    }

    /**
     * Starts the UserController by displaying the login interface.
     */
    public void run() {
        userView.displayLogin();
    }

    /**
     * Handles user login attempts, allowing a maximum of three attempts.
     *
     * @param scanner The Scanner instance for user input.
     * @return True if login is successful, false otherwise.
     */
    public boolean attemptLogin(Scanner scanner) {
        int attempts = 3; // Maximum number of attempts allowed
        boolean isAuthenticated = false;

        while (attempts > 0 && !isAuthenticated) {
            System.out.print("Enter Hospital ID (Case Sensitive): ");
            String hospitalID = scanner.nextLine();

            System.out.print("Enter Password: ");
            String password = scanner.nextLine();

            if (userService.login(hospitalID, password)) {
                isAuthenticated = true;
                UserRole role = userService.getUserRole(hospitalID);
                User user = userService.getUserById(hospitalID);
                navigateToRoleSpecificPage(user, role);
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
     * Navigates to the role-specific page based on the user's role.
     *
     * @param user The authenticated user.
     * @param role The role of the user.
     */
    private void navigateToRoleSpecificPage(User user, UserRole role) {
        switch (role) {
            case PATIENT:
                navigateToPatientPage(user);
                break;

            case DOCTOR:
                navigateToDoctorPage(user);
                break;

            case PHARMACIST:
                navigateToPharmacistPage(user);
                break;

            case ADMINISTRATOR:
                System.out.println("Navigating to Administrator view...");
                // Implement the AdministratorView and corresponding logic here
                break;

            default:
                System.out.println("Role not recognized.");
        }
    }

    /**
     * Navigates to the patient-specific page.
     *
     * @param user The authenticated patient user.
     */
    private void navigateToPatientPage(User user) {
        AppointmentService appointmentService = new AppointmentService();
        BillingService billingService = new BillingService();
        BillingController billingController = new BillingController();
        PatientService patientService = new PatientService(userService);
        PatientController patientController = new PatientController(patientService, appointmentService);
        PatientView patientView = new PatientView(patientController, userService, billingController);

        // Start the patient operations (menu)
        System.out.println("Navigating to Patient view...");
        patientView.start(user);
    }

    /**
     * Navigates to the pharmacist-specific page.
     *
     * @param user The authenticated pharmacist user.
     */
    private void navigateToPharmacistPage(User user) {
        AppointmentService appointmentService = new AppointmentService();
        InventoryDataStore inventoryDataStore = new InventoryDataStore();
        ReplenishmentService replenishmentService = new ReplenishmentService();
        InventoryService inventoryService = new InventoryService(inventoryDataStore);
        PharmacistService pharmacistService = new PharmacistService(userService, appointmentService, inventoryService);
        PatientService patientService = new PatientService(userService);
        PharmacistController pharmacistController = new PharmacistController(pharmacistService, inventoryService, appointmentService, replenishmentService);
        PharmacistView pharmacistView = new PharmacistView(pharmacistController, pharmacistService, userService, patientService);

        // Start the pharmacist operations (menu)
        System.out.println("Navigating to Pharmacist view...");
        pharmacistView.start(user);
    }

    /**
     * Navigates to the doctor-specific page.
     *
     * @param user The authenticated doctor user.
     */
    private void navigateToDoctorPage(User user) {
        ScheduleService scheduleService = new ScheduleService();
        MedicalRecordService medicalRecordService = new MedicalRecordService();
        AppointmentService appointmentService = new AppointmentService();
        AppointmentRequestService appointmentRequestService = new AppointmentRequestService(scheduleService, appointmentService);
        DoctorService doctorService = new DoctorService(userService, scheduleService, medicalRecordService, appointmentService);
        DoctorController doctorController = new DoctorController(doctorService, scheduleService, medicalRecordService, appointmentService);
        DoctorView doctorView = new DoctorView(doctorController, doctorService, userService, scheduleService, medicalRecordService, appointmentService);

        // Start the doctor operations (menu)
        System.out.println("Navigating to Doctor view...");
        doctorView.start(user);
    }

    /**
     * Changes the password for a user.
     *
     * @param hospitalID  The hospital ID of the user.
     * @param oldPassword The old password of the user.
     * @param newPassword The new password of the user.
     * @return True if the password was successfully changed, false otherwise.
     */
    public boolean changePassword(String hospitalID, String oldPassword, String newPassword) {
        return userService.changePassword(hospitalID, oldPassword, newPassword);
    }
}

