package views;

import controllers.PatientController;
import enums.UserRole;
import services.UserService;
import services.AppointmentService;
import services.PatientService;
import models.User;

import java.util.Scanner;

public class UserView {
    private final UserService userService;
    private String loggedInHospitalID;

    public UserView(UserService userService) {
        this.userService = userService;
    }

    public void displayLogin() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("***WELCOME TO HOSPITAL MANAGEMENT SYSTEM***");

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
                    System.out.println("=====================================");

                    // Passing in user object
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

            if (!isAuthenticated) {
                System.out.println("Maximum attempts reached.");
            }
        }

        System.out.println("Thank You. Exiting HMS.");
        System.exit(0);
    }

    private void navigateToRoleSpecificPage(User user, UserRole role) {
        switch (role) {
            case PATIENT:
                // Create the AppointmentService and PatientService instances
                AppointmentService appointmentService = new AppointmentService();
                PatientService patientService = new PatientService(userService);

                // Instantiate PatientController
                PatientController patientController = new PatientController(patientService, appointmentService);

                // Instantiate PatientView
                PatientView patientView = new PatientView(patientController, userService);

                // Start the patient operations (menu)
                System.out.println("Navigating to Patient view...");
                System.out.println("=====================================");
                patientView.start(user);
                break;

            case DOCTOR:
                System.out.println("Navigating to Doctor view...");
                System.out.println("=====================================");
                break;

            case PHARMACIST:
                // Example: PharmacistView pharmacistView = new PharmacistView(userService);
                System.out.println("Navigating to Pharmacist view...");
                System.out.println("=====================================");
                break;

            case ADMINISTRATOR:
                System.out.println("Navigating to Administrator view...");
                break;

            default:
                System.out.println("Role not recognized.");
        }
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