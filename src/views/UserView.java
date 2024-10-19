package views;

import services.UserService;
import enums.UserRole; // Import the UserRole enum

import java.util.Scanner;

public class UserView {
    private UserService userService;
    private String loggedInHospitalID;

    public UserView(UserService userService) {
        this.userService = userService;
    }

    public void displayLogin() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("***WELCOME TO HOSPITAL MANAGEMENT SYSTEM***");

        System.out.print("Enter Hospital ID: ");
        String hospitalID = scanner.nextLine();

        System.out.print("Enter Password: ");
        String password = scanner.nextLine();

        if (userService.login(hospitalID, password)) {
            loggedInHospitalID = hospitalID;
            System.out.println("Login successful!");
            UserRole role = userService.getUserRole(hospitalID);
            System.out.println("Role: " + role);
            navigateToRoleSpecificPage(role);
        } else {
            System.out.println("Invalid Hospital ID or Password.");
        }
    }

    private void navigateToRoleSpecificPage(UserRole role) {
        switch (role) {
            case PATIENT:
                // Call to PatientView
                // Example: PatientView patientView = new PatientView(userService);
                // patientView.display();
                System.out.println("Navigating to Patient view...");
                break;
            case DOCTOR:
                // Call to DoctorView
                // Example: DoctorView doctorView = new DoctorView(userService);
                // doctorView.display();
                System.out.println("Navigating to Doctor view...");
                break;
            case PHARMACIST:
                // Call to PharmacistView
                // Example: PharmacistView pharmacistView = new PharmacistView(userService);
                // pharmacistView.display();
                System.out.println("Navigating to Pharmacist view...");
                break;
            case SUPERVISOR:
                // Call to SupervisorView
                // Example: SupervisorView supervisorView = new SupervisorView(userService);
                // supervisorView.display();
                System.out.println("Navigating to Supervisor view...");
                break;
            default:
                System.out.println("Role not recognized.");
        }
    }

    public void displayChangePassword() {
        Scanner scanner = new Scanner(System.in);

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
