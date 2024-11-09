package views;

import interfaces.iPatientView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import models.Patient;
import controllers.PatientController;
import models.User;
import src.models.Billing;
import src.controllers.BillingController;
import java.util.List;
import services.PatientService;
import services.UserService;

public class PatientView implements iPatientView {
    private final Scanner scanner;
    private final PatientController patientController;
    private final UserService userService;
    private final BillingController billingController;

    public PatientView(PatientController patientController, UserService userService, BillingController billingController) {
        this.scanner = new Scanner(System.in); // Do not close scanner here, managed centrally by UserView
        this.patientController = patientController;
        this.userService = userService;
        this.billingController=billingController;
    }

    // Main method to handle different patient operations
    public void start(User user) {
        PatientService patientService = new PatientService(userService);
        boolean isRunning = true;

        // Load patient details from the User object
        Patient patient = patientService.getPatientById(user.getHospitalID());
        if (patient == null) {
            // Add logic to guide the user to register a new patient if not found.
            System.out.println("Patient record not found for user: " + user.getHospitalID());
            System.out.println("Do you want to register a new patient? (Y/N): ");
            String userInput = scanner.nextLine().trim().toUpperCase();

            if (userInput.equals("Y")) {
                // Prompt the user for patient information
                System.out.print("Enter Patient Name: ");
                String name = scanner.nextLine().trim();

                System.out.print("Enter Date of Birth (yyyy-MM-dd): ");
                String dobString = scanner.nextLine().trim();
                LocalDate dateOfBirth = LocalDate.parse(dobString, DateTimeFormatter.ISO_LOCAL_DATE);

                System.out.print("Enter Gender: ");
                String gender = scanner.nextLine().trim();

                System.out.print("Enter Blood Type: ");
                String bloodType = scanner.nextLine().trim();

                System.out.print("Enter Contact Information (email or phone): ");
                String contactInformation = scanner.nextLine().trim();

                // Register new patient using user data
                patient = new Patient(user, name, dateOfBirth, gender, bloodType, contactInformation);
                patientService.addPatient(patient);
                System.out.println("New patient registered successfully.");
            } else {
                System.out.println("Unable to proceed without patient information.");
                return;
            }
        }

        while (isRunning) {
            displayMenu();
            int choice = getUserInput();

            switch (choice) {
                case 1 -> patientController.viewPatientDetails(patient);
                case 2 -> patientController.viewAllocatedAppointments(patient);
                case 3 -> patientController.viewAppointmentHistory(patient);
                case 4 -> patientController.viewMedicalRecords(patient);
                case 5 -> patientController.updateContactInformation(patient);
                case 6 -> patientController.createAppointment(patient);
                case 7 -> patientController.cancelAppointment(patient);
                case 8 -> patientController.rescheduleAppointment(patient);
                case 9 -> patientController.viewAvailableAppointmentSlots();
                case 10 -> showBillingOptions(patient);
                case 11 -> {
                    System.out.println("Logging out...");
                    isRunning = false; // Exit the loop to log out
                }
                default -> showErrorMessage("Invalid choice, please try again.");
            }

            // Only prompt to continue if the user has not chosen to log out
            if (isRunning) {
                System.out.println("\nDo you want to continue (Y/N): ");
                String userInput = scanner.nextLine().trim().toUpperCase();

                if (userInput.equals("N") || userInput.equals("NO")) {
                    isRunning = false;
                } else {
                    System.out.println("==============================\n");
                }
            }
        }

        // Do not close the scanner here as it's used in the main UserView
    }

    // Implementing the display method to show the main menu
    public void displayMenu() {
        System.out.println("Please choose an option:");
        System.out.println("1. View Medical Record");
        System.out.println("2. View Allocated Appointments");
        System.out.println("3. View Appointment History");
        System.out.println("4. View Medical Records");
        System.out.println("5. Update Contact Information");
        System.out.println("6. Create Appointment");
        System.out.println("7. Cancel Appointment");
        System.out.println("8. Reschedule Appointment");
        System.out.println("9. View Available Appointment Slots");
        System.out.println("10. View Billing Details");
        System.out.println("11. Log Out");
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

    public void showBillingOptions(Patient patient) {
        System.out.println("1. View Unpaid Bills");
        System.out.println("2. View Paid Bills");
        System.out.print("Select an option: ");
        int choice = scanner.nextInt();

        if (choice == 1) {
            List<Billing> unpaidBills = billingController.viewUnpaidBills(patient.getHospitalID());
            displayBills(unpaidBills);

            System.out.print("Enter the Invoice ID to pay: ");
            String invoiceId = scanner.next();
            boolean paymentSuccess = billingController.payBill(invoiceId);
            if (paymentSuccess) {
                System.out.println("Payment successful.");
            } else {
                System.out.println("Payment failed or Invoice ID not found.");
            }
        } else if (choice == 2) {
            List<Billing> paidBills = billingController.viewPaidBills(patient.getHospitalID());
            displayBills(paidBills);
        }
    }

    private void displayBills(List<Billing> bills) {
        if (bills.isEmpty()) {
            System.out.println("No records found.");
        } else {
            for (Billing bill : bills) {
                System.out.println("Invoice ID: " + bill.getInvoiceId() + ", Amount: $" + bill.getTotalAmount() + ", Status: " + bill.getStatus());
            }
        }
    }
}