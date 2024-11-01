package controllers;

import services.UserService;
import views.UserView;
import enums.UserRole;
import models.User;
import services.AppointmentService;
import stores.InventoryDataStore;
import services.InventoryService;
import services.PatientService;
import services.PharmacistService;
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
 * The UserController class is responsible for managing user interactions
 * within the hospital management system. It handles user authentication,
 * role-specific navigation, and password management.
 */
public class UserController {
    private final UserService userService;
    private final UserView userView;

    /**
     * Constructs a UserController with the specified UserService.
     *
     * @param userService the UserService to handle user-related operations
     */
    public UserController(UserService userService) {
        this.userService = userService;
        this.userView = new UserView(userService);
    }

    /**
     * Runs the user login process by displaying the login view.
     */
    public void run() {
        userView.displayLogin();
    }

    /**
     * Attempts to log in a user with the given hospital ID and password.
     * Allows a maximum of three attempts.
     *
     * @param scanner the Scanner to read user input
     * @return true if the login is successful, false otherwise
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
     * Navigates to the role-specific page for the authenticated user.
     *
     * @param user the authenticated User
     * @param role the UserRole of the authenticated User
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
     * Navigates to the Patient view and initializes the required services and controllers.
     *
     * @param user the authenticated User
     */
    private void navigateToPatientPage(User user) {
        AppointmentService appointmentService = new AppointmentService();
        PatientService patientService = new PatientService(userService);
        PatientController patientController = new PatientController(patientService, appointmentService);
        PatientView patientView = new PatientView(patientController, userService);

        // Start the patient operations (menu)
        System.out.println("Navigating to Patient view...");
        patientView.start(user);
    }

    /**
     * Navigates to the Pharmacist view and initializes the required services and controllers.
     *
     * @param user the authenticated User
     */
    private void navigateToPharmacistPage(User user) {
        AppointmentService appointmentService = new AppointmentService();
        InventoryDataStore inventoryDataStore = new InventoryDataStore();
        InventoryService inventoryService = new InventoryService(inventoryDataStore);
        PharmacistService pharmacistService = new PharmacistService(userService, appointmentService, inventoryService);
        PatientService patientService = new PatientService(userService);
        PharmacistController pharmacistController = new PharmacistController(pharmacistService, inventoryService, appointmentService);
        PharmacistView pharmacistView = new PharmacistView(pharmacistController, pharmacistService, userService, patientService);

        // Start the pharmacist operations (menu)
        System.out.println("Navigating to Pharmacist view...");
        pharmacistView.start(user);
    }

    /**
     * Navigates to the Doctor view and initializes the required services and controllers.
     *
     * @param user the authenticated User
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
     * Changes the password for the user with the specified hospital ID.
     *
     * @param hospitalID the unique identifier of the user
     * @param oldPassword the current password of the user
     * @param newPassword the new password to be set
     * @return true if the password change is successful, false otherwise
     */
    public boolean changePassword(String hospitalID, String oldPassword, String newPassword) {
        return userService.changePassword(hospitalID, oldPassword, newPassword);
    }
}
