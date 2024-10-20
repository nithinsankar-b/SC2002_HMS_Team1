package views;

import interfaces.iPatientView;
import java.util.Scanner;
import models.Patient;
import controllers.PatientController;

public class PatientView implements iPatientView {
    private final Scanner scanner;
    private final PatientController patientController;

    public PatientView(PatientController patientController) {
        this.scanner = new Scanner(System.in);
        this.patientController = patientController;
    }

    // Main method to handle different patient operations
    public void start() {
        boolean isRunning = true;

        while (isRunning) {
            displayMenu();
            int choice = getUserInput();

            switch (choice) {
                case 1 -> patientController.viewPatientDetails(scanner);
                case 2 -> patientController.viewAllocatedAppointments(scanner);
                case 3 -> patientController.viewAppointmentHistory(scanner);
                case 4 -> patientController.viewMedicalRecords(scanner);
                case 5 -> patientController.updateContactInformation(scanner);
                case 6 -> patientController.createAppointment(scanner);
                case 7 -> patientController.cancelAppointment(scanner);
                case 8 -> patientController.rescheduleAppointment(scanner);
                case 9 -> patientController.viewAvailableAppointmentSlots(scanner);
                case 10 -> isRunning = false;
                default -> showErrorMessage("Invalid choice, please try again.");
            }
        }
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
    public void display(String patientID) {
        System.out.println("Displaying information for patient ID: " + patientID);
        start();
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
}
