package controllers;

import enums.UserRole;
import java.util.Scanner;
import models.User;
import services.AppointmentRequestService;
import services.AppointmentService;
import services.BillingService;
import services.DoctorService;
import services.InventoryService;
import services.MedicalRecordService;
import services.PatientService;
import services.PharmacistService;
import services.ScheduleService;
import services.UserService;
import stores.InventoryDataStore;
import views.DoctorView;
import views.PatientView;
import views.PharmacistView;
import views.UserView;

public class UserController {
    private final UserService userService;
    private final UserView userView;

    public UserController(UserService userService) {
        this.userService = userService;
        this.userView = new UserView(userService);
    }

    public void run() {
        userView.displayLogin();
    }

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

    private void navigateToPatientPage(User user) {
        AppointmentService appointmentService = new AppointmentService();
        BillingService billingService=new BillingService();
        BillingController billingController=new BillingController(billingService);
        PatientService patientService = new PatientService(userService);
        PatientController patientController = new PatientController(patientService, appointmentService);
        PatientView patientView = new PatientView(patientController, userService, billingController);

        // Start the patient operations (menu)
        System.out.println("Navigating to Patient view...");
        patientView.start(user);
    }

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

    public boolean changePassword(String hospitalID, String oldPassword, String newPassword) {
        return userService.changePassword(hospitalID, oldPassword, newPassword);
    }
}
