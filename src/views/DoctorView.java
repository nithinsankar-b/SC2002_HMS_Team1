package views;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import models.Doctor;
import controllers.DoctorController;
import interfaces.IDoctorView;
import models.User;
import services.AppointmentRequestService;
import services.AppointmentService;
import services.DoctorService;
import services.MedicalRecordService;
import services.ScheduleService;
import services.UserService;

/**
 * The {@code DoctorView} class provides a user interface for doctors to manage their operations.
 * It allows doctors to view medical records, manage schedules, handle appointments, and more.
 */
public class DoctorView implements IDoctorView {
    private final Scanner scanner; // Scanner for user input
    private final DoctorController doctorController; // Controller for doctor operations
    private final UserService userService; // Service to manage user details
    private final AppointmentRequestService appointmentRequestService; // Service for appointment requests
    private final ScheduleService scheduleService; // Service for managing schedules
    private final MedicalRecordService medicalRecordService; // Service for managing medical records
    private final AppointmentService appointmentService; // Service for managing appointments
    private final DoctorService doctorService; // Service for managing doctor details

    /**
     * Constructs a {@code DoctorView} with the specified services and controller.
     *
     * @param doctorController the controller for managing doctor operations
     * @param doctorService the service for managing doctor details
     * @param userService the service for managing user details
     * @param scheduleService the service for managing schedules
     * @param medicalRecordService the service for managing medical records
     * @param appointmentService the service for managing appointments
     */
    public DoctorView(DoctorController doctorController, DoctorService doctorService,
                      UserService userService, ScheduleService scheduleService,
                      MedicalRecordService medicalRecordService, AppointmentService appointmentService) {
        this.scanner = new Scanner(System.in); // Initialize scanner for user input
        this.doctorController = doctorController;
        this.userService = userService;
        this.appointmentService = appointmentService;
        this.medicalRecordService = medicalRecordService;
        this.doctorService = doctorService;
        this.scheduleService = scheduleService;
        this.appointmentRequestService = new AppointmentRequestService(scheduleService, appointmentService);
    }

    /**
     * Starts the doctor operations, allowing the doctor to interact with the system.
     *
     * @param user the user object containing doctor's details
     */
    public void start(User user) {
        Doctor doctor = doctorService.getDoctorById(user.getHospitalID());
        // Load doctor details from the User object
        if (doctor == null) {
            System.out.println("Doctor record not found for user: " + user.getHospitalID());
            return; // Exit if no doctor record is found
        }

        if (doctor.getContactInformation().equals("default@example.com") && user.getPassword().equals("password")) {
            System.out.println("Your email and password are both set to default and must be updated.");

            System.out.println("\nChange Password");
            System.out.println("===========================================");
            changePassword(); // Change password

            System.out.println("\nChange Email");
            System.out.println("===========================================");
            // Update contact information
            promptForEmailChange(doctor);

        } else if (doctor.getContactInformation().equals("default@example.com")) {
            System.out.println("Your email is set to the default and must be updated.\n");
            promptForEmailChange(doctor);

        } else if (user.getPassword().equals("password")) {
            System.out.println("Your password is set to the default and must be updated.\n");
            changePassword();
        }

        boolean isRunning = true;
   
        while (isRunning) {
            displayMenu();
            int choice = getUserInput();
            boolean inMainMenu = true;

            switch (choice) {
            case 1 ->{ Scanner sc = new Scanner(System.in);
            System.out.println("Enter PatientID");
            String p=sc.nextLine();
            doctorController.medicalRecordsView(p);}
            case 2 -> doctorController.personalScheduleView(doctor);
            case 3 -> doctorController.upcomingAppointmentsView(doctor);
            case 4 -> doctorController.pendingAppointmentsView(doctor);
            case 5 -> {manageAvailabilityMenu(doctor);
            inMainMenu = false;}
            case 6 -> {manageAppointmentRequestsMenu(doctor);
            inMainMenu = false;}
            case 7 -> {managePatientRecordsMenu(); 
            inMainMenu = false;}
            case 8 -> {if(doctorController.viewAppointmentOutcomeRecords(doctor)) {
            	doctorController.appointmentOutcomeRecord(doctor);}}
            case 9 -> changePassword();
            case 10 -> {
                System.out.println("Logging out...");
                isRunning = false;
            }
            default -> System.out.println("ERROR: Invalid choice, please try again.");
        }

            // Only prompt to continue if the user has not chosen to log out
            if (isRunning && inMainMenu) {
            	Scanner sc=new Scanner(System.in);
            
            	
                System.out.println("\nDo you want to continue (Y/N): ");
                
                String continueInput = sc.nextLine().trim().toUpperCase();

                if (continueInput.equals("N") || continueInput.equals("NO")) {
                    isRunning = false;
                } else if (continueInput.equals("Y") || continueInput.equals("YES")) {
                    System.out.println("==============================\n");
                } else {
                    System.out.println("Invalid input. Please enter Y or N.");
                }
            }
        }
        // Do not close the scanner here as it's used in the main UserView
    }

    private void promptForEmailChange(Doctor doctor) {
        System.out.print("Enter new email: ");
        String newEmail = scanner.nextLine();
        if (doctorService.updateDoctorContact(doctor.getHospitalID(), newEmail)) {
            System.out.println("Email updated successfully.");
        } else {
            System.out.println("Failed to update email.");
        }
    }

    /**
     * Displays the menu options for doctor operations.
     */
    public void displayMenu() {
        System.out.println("Please choose an option:");
        System.out.println("1. View Medical Records");
        System.out.println("2. View Personal Schedule");
        System.out.println("3. View Upcoming Appointments");
        System.out.println("4. View Pending Appointment Requests");
        System.out.println("5. Set Availability.");
        System.out.println("6. Accept or Decline Appointment Request.");
        System.out.println("7. Update Patient Medical Records.");
        System.out.println("8. Record Appointment Outcome");
        System.out.println("9. Change Password");
        System.out.println("10. Log Out");
    }
    
    public void manageAvailabilityMenu(User user) {
    	Doctor doctor = doctorService.getDoctorById(user.getHospitalID());
        boolean managingAvailability = true;
        while (managingAvailability) {
            System.out.println("Manage Availability:");
            System.out.println("1. Set Date to Available");
            System.out.println("2. Set Date to Unavailable");
            System.out.println("3. Return to Main Menu");

            int choice = getUserInput();

            switch (choice) {
                case 1 -> doctorController.unblockSlots(doctor);
                case 2 -> doctorController.blockSlots(doctor);
                case 3 -> {managingAvailability = false;
                return;}
                default -> System.out.println("ERROR: Invalid choice, please try again.");
            }
        }
    }
    
    private void manageAppointmentRequestsMenu(Doctor doctor) {
        boolean managingRequests = true;
        while (managingRequests) {
            System.out.println("Accept or Decline Appointment Request:");
            System.out.println("1. Accept Appointment Request");
            System.out.println("2. Decline Appointment Request");
            System.out.println("3. Return to Main Menu");

            int choice = getUserInput();

            switch (choice) {
                case 1 -> doctorController.acceptAppointmentRequest(doctor);
                case 2 -> doctorController.declineAppointmentRequest(doctor);
                case 3 -> {managingRequests = false;
                return;}
                default -> System.out.println("ERROR: Invalid choice, please try again.");
            }
           }
        }
    
    private void managePatientRecordsMenu() {
        boolean managingRecords = true;
        while (managingRecords) {
            System.out.println("Update Patient Medical Records:");
            System.out.println("1. Add New Diagnosis for Patient");
            System.out.println("2. Add New Prescription for Patient");
            System.out.println("3. Return to Main Menu");

            int choice = getUserInput();

            switch (choice) {
                case 1 -> doctorController.newPatientDiagnosis();
                case 2 -> doctorController.newPatientPrescription();
                case 3 -> {managingRecords = false;
                return;}
                default -> System.out.println("ERROR: Invalid choice, please try again.");
            }
        }
    }

    /**
     * Gets user input with error checking.
     *
     * @return the choice selected by the user
     */
    private int getUserInput() {
        int choice = -1;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            showErrorMessage("Invalid input. Please enter a number between 1 and 13.");
        }
        return choice;
    }

    /**
     * Displays a success message to the user.
     *
     * @param message the message to be displayed
     */
    public void showSuccessMessage(String message) {
        System.out.println("SUCCESS: " + message);
    }

    /**
     * Displays an error message to the user.
     *
     * @param message the message to be displayed
     */
    public void showErrorMessage(String message) {
        System.out.println("ERROR: " + message);
    }
    private void changePassword()
    {
      Scanner sc=new Scanner(System.in);
      System.out.print("Enter Hospital ID: ");
      String id = sc.nextLine();
      
      System.out.print("Enter old password: ");
      String oldPassword = sc.nextLine();
      
      System.out.print("Enter new password: ");
      String newPassword = sc.nextLine();
      
      doctorController.changePassword(id, oldPassword, newPassword);
    }
}


