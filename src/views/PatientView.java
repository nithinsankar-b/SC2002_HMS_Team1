package views;

import interfaces.iPatientView;

import java.time.LocalDate;
import java.util.Scanner;
import models.Patient;
import controllers.PatientController;
import models.User;
import services.AppointmentService;
import services.PatientService;

public class PatientView implements iPatientView {
    private final Scanner scanner;
    private final PatientController patientController;
    private Patient patient;

    public PatientView(PatientController patientController) {
        this.scanner = new Scanner(System.in);
        this.patientController = patientController;
    }

    // Main method to handle different patient operations
    public void start(User user) {
        boolean isRunning = true;
        Scanner scanner = new Scanner(System.in);

        while (isRunning) {
            displayMenu();
            int choice = getUserInput();

            // Need to modify it such that we get the patient details instead of manually keying it in.

            patient = new Patient(user.getHospitalID(), user.getPassword(), user.getRole(), "James", LocalDate.of(2001, 1, 1), "Male", "O+", "james@gmail.com");

            switch (choice) {
                case 1 -> patientController.viewPatientDetails(patient);
                case 2 -> patientController.viewAllocatedAppointments(patient);
                case 3 -> patientController.viewAppointmentHistory(patient);
                case 4 -> patientController.viewMedicalRecords(patient);
                case 5 -> patientController.updateContactInformation(patient);
                case 6 -> patientController.createAppointment(patient);
                case 7 -> patientController.cancelAppointment(patient);
                case 8 -> patientController.rescheduleAppointment(patient);
                // case 9 -> patientController.viewAvailableAppointmentSlots();
                case 10 -> {
                    isRunning = false;
                }
                default -> showErrorMessage("Invalid choice, please try again.");
            }

            // Only prompt to continue if the user has not chosen to exit
            if (isRunning) {
                System.out.println("\nDo you want to continue (Y/N): ");
                String userInput = scanner.nextLine().trim().toUpperCase();

                if (userInput.equals("N")) {
                    isRunning = false;
                } else {
                    System.out.println("==============================\n");
                }
            }
        }

        scanner.close(); // Close the scanner when done
        System.out.println("Thank You. Exiting HMS.");
        System.exit(0);
    }


    // Implementing the display method to show the main menu
    public void displayMenu() {
        System.out.println("Please choose an option:");
        System.out.println("1. View Patient Details");
        System.out.println("2. View Allocated Appointments");
        System.out.println("3. View Appointment History");
        System.out.println("4. View Medical Records");
        System.out.println("5. Update Contact Information");
        System.out.println("6. Create Appointment");
        System.out.println("7. Cancel Appointment");
        System.out.println("8. Reschedule Appointment");
        System.out.println("9. View Available Appointment Slots");
        System.out.println("10. Exit");
    }

    @Override
    public void display(Patient patient) {
        System.out.println("Displaying information for patient ID: " + patient.getHospitalID());
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

    @Override
    public void showPatientDetails(Patient patient) {
        if (patient != null) {
            System.out.println("Patient ID: " + patient.getHospitalID());
            System.out.println("Name: " + patient.getName());
            System.out.println("Contact: " + patient.getContactInformation());
            // Display other patient details as needed
        } else {
            showErrorMessage("Patient details not found.");
        }
    }

    @Override
    public void showSuccessMessage(String message) {
        System.out.println("SUCCESS: " + message);
    }

    @Override
    public void showErrorMessage(String message) {
        System.out.println("ERROR: " + message);
    }

//    public static void main(String[] args) {
//        AppointmentService appointmentService = new AppointmentService();
//        PatientService patientService = new PatientService();
//        PatientController patientController = new PatientController(patientService, appointmentService);
//        PatientView patientView = new PatientView(patientController);
//        patientView.start();
//    }
}
