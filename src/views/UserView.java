package views;

import controllers.PatientController;
import enums.UserRole;
import services.UserService;

>>>>>>> user
import java.util.Scanner;

import models.User;
import services.UserService;

public class UserView {
    private final UserService userService;
    private String loggedInHospitalID;

    public UserView(UserService userService) {
        this.userService = userService;
    }

    public void displayLogin() {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("***WELCOME TO HOSPITAL MANAGEMENT SYSTEM***");

            System.out.print("Enter Hospital ID: ");
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
                navigateToRoleSpecificPage(user, role);
            } else {
                System.out.println("Invalid Hospital ID or Password.");
            }
        }
    }

    private void navigateToRoleSpecificPage(User user, UserRole role) {
        switch (role) {
            case PATIENT:
<<<<<<< HEAD
                // Create PatientService and AppointmentService
                PatientController patientController = new PatientController(null, null);

                // Instantiate PatientController
                PatientView patientView = new PatientView(patientController);

                // Start the patient operations (menu)
=======
>>>>>>> user
                System.out.println("Navigating to Patient view...");
                System.out.println("=====================================");
                patientView.start(user);
                break;
            case DOCTOR:
                System.out.println("Navigating to Doctor view...");
                System.out.println("=====================================");
                break;
<<<<<<< HEAD
                case PHARMACIST:
                // Call to PharmacistView
                // Example: PharmacistView pharmacistView = new PharmacistView(userService);
                // pharmacistView.display();
=======
            case PHARMACIST:
>>>>>>> user
                System.out.println("Navigating to Pharmacist view...");
                System.out.println("=====================================");
                break;
<<<<<<< HEAD
=======
            case ADMINISTRATOR:
                System.out.println("Navigating to Administrator view...");
>>>>>>> user
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

<<<<<<< HEAD
            System.out.print("Enter New Password: ");
            String newPassword = scanner.nextLine();
=======
        System.out.print("Enter Old Password: ");
        String oldPassword = scanner.nextLine();
>>>>>>> user

            if (userService.changePassword(loggedInHospitalID, oldPassword, newPassword)) {
                System.out.println("Password changed successfully!");
            } else {
                System.out.println("Old password is incorrect. Password change failed.");
            }

            scanner.close();
        }
    }
}
