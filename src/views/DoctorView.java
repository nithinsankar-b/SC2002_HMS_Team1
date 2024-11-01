package views;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import models.Doctor;
import controllers.DoctorController;
import models.User;
import services.AppointmentRequestService;
import services.AppointmentService;
import services.DoctorService;
import services.MedicalRecordService;
import services.ScheduleService;
import services.UserService;

public class DoctorView {
    private final Scanner scanner;
    private final DoctorController doctorController;
    private final UserService userService;
    private final AppointmentRequestService appointmentRequestService;
    private final ScheduleService scheduleService;
    private final MedicalRecordService medicalRecordService;
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    

    public DoctorView(DoctorController doctorController, DoctorService doctorService, UserService userService,ScheduleService scheduleService,MedicalRecordService medicalRecordService, AppointmentService appointmentService) {
        this.scanner = new Scanner(System.in); // Do not close scanner here, managed centrally by UserView
        this.doctorController = doctorController;
        this.userService = userService;
        this.appointmentService = appointmentService;
        this.medicalRecordService = medicalRecordService ;
        this.doctorService = doctorService;
        this.scheduleService = scheduleService;
        this.appointmentRequestService = new AppointmentRequestService(scheduleService, appointmentService);
        
    }

    // Main method to handle different doctor operations
    public void start(User user) {
        Doctor doctor = doctorService.getDoctorById(user.getHospitalID());
       // Load doctor details from the User object
        if (doctor == null) {
            System.out.println("Doctor record not found for user: " + user.getHospitalID());
            return; // Exit if no doctor record is found
        }
        
        boolean isRunning = true;

        while (isRunning) {
            displayMenu();
            int choice = getUserInput();

            switch (choice) {
            case 1 ->{ Scanner sc = new Scanner(System.in);
            System.out.println("Enter PatientID");
            String p=sc.nextLine();
            doctorController.medicalRecordsView(p);}
            case 2 -> doctorController.personalScheduleView(doctor);
            case 3 -> doctorController.upcomingAppointmentsView(doctor);
            case 4 -> doctorController.pendingAppointmentsView(doctor);
            case 5 -> doctorController.setToAvailable(doctor);
            case 6 -> doctorController.setToUnavailable(doctor);
            case 7 -> doctorController.acceptAppointmentRequest();
            case 8 -> doctorController.declineAppointmentRequest();
            case 9 -> doctorController.newPatientDiagnosis();
            case 10 -> doctorController.newPatientPrescription();
            case 11 -> doctorController.appointmentOutcomeRecord();
            default -> System.out.println("ERROR: Invalid choice, please try again.");
        }

            // Only prompt to continue if the user has not chosen to log out
            if (isRunning) {
            	Scanner sc=new Scanner(System.in);
            
            	
                System.out.println("\nDo you want to continue (Y/N): ");
                
                String continueInput = sc.nextLine().trim().toUpperCase();

                if (continueInput.equals("N")) {
                    isRunning = false;
                } else if (!continueInput.equals("Y")) {
                    System.out.println("Invalid input. Please enter Y or N.");
                } else {
                    System.out.println("==============================\n");
                }
            }
        }
        // Do not close the scanner here as it's used in the main UserView
    }

    // Implementing the display method to show the main menu
    private void displayMenu() {
        System.out.println("Please choose an option:");
        System.out.println("1. View Medical Records");
        System.out.println("2. View Personal Schedule");
        System.out.println("3. View Upcoming Appointments");
        System.out.println("4. View Pending Appointment Requests");
        System.out.println("5. Set Date to Available");
        System.out.println("6. Set Date to Unavailable");
        System.out.println("7. Accept Appointment Request");
        System.out.println("8. Decline Appointment Request");
        System.out.println("9. Add New Diagnosis for Patient");
        System.out.println("10. Add New Prescription for Patient");
        System.out.println("11. Record Appointment Outcome");
    }

    // Get user input with error checking
    private int getUserInput() {
        int choice = -1;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            showErrorMessage("Invalid input. Please enter a number between 1 and 10.");
        }
        return choice;
    }

   
    public void showSuccessMessage(String message) {
        System.out.println("SUCCESS: " + message);
    }

   
    public void showErrorMessage(String message) {
        System.out.println("ERROR: " + message);
    }
}


