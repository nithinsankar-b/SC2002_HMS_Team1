package views;

import interfaces.iPatientView;
import models.Patient;
import models.User;
import models.Billing;
import controllers.PatientController;
import controllers.BillingController;
import services.PatientService;
import services.UserService;
import models.Appointment;
import models.PaymentLogger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class PatientView implements iPatientView {
    private final Scanner scanner;
    private final PatientController patientController;
    private final UserService userService;
    private final BillingController billingController;

    public PatientView(PatientController patientController, UserService userService, BillingController billingController) {
        this.scanner = new Scanner(System.in); // Centralized Scanner management
        this.patientController = patientController;
        this.userService = userService;
        this.billingController = billingController;
    }

    public void start(User user) {
        PatientService patientService = new PatientService(userService);
        if (patientService.checkAndPromptPasswordChange(user.getHospitalID(), scanner)) {
            System.out.println("Proceeding after password change.");
        }
        boolean isRunning = true;

        Patient patient = patientService.getPatientById(user.getHospitalID());
        if (patient == null) {
            System.out.println("Patient record not found for user: " + user.getHospitalID());
            System.out.println("Do you want to register a new patient? (Y/N): ");
            String userInput = scanner.nextLine().trim().toUpperCase();

            if (userInput.equals("Y")) {
                registerNewPatient(user);
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
                case 3 -> patientController.viewAllocatedAppointments2(patient);
                case 4 -> patientController.viewAppointmentHistory(patient);
                case 5 -> patientController.updateContactInformation(patient);
                case 6 -> patientController.createAppointment(patient);
                case 7 -> patientController.cancelAppointment(patient);
                case 8 -> patientController.rescheduleAppointment(patient);
                case 9 -> patientController.viewAvailableAppointmentSlots();
                case 10 -> patientController.viewPastRecords(patient);
                case 11 -> showBillingOptions(patient);
                case 12-> patientController.changePassword(patient);
                case 13 -> {
                    System.out.println("Logging out...");
                    isRunning = false;
                }
                default -> showErrorMessage("Invalid choice, please try again.");
            }

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
    }

    private void registerNewPatient(User user) {
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

        Patient patient = new Patient(user, name, dateOfBirth, gender, bloodType, contactInformation);
        new PatientService(userService).addPatient(patient);
        System.out.println("New patient registered successfully.");
    }

    public void displayMenu() {
        System.out.println("Please choose an option:");
        System.out.println("1. View Medical Record");
        System.out.println("2. View your Appointment Requests");
        System.out.println("3. View Upcoming Appointments");
        System.out.println("4. View Appointment History");
        System.out.println("5. Update Contact Information");
        System.out.println("6. Create Appointment");
        System.out.println("7. Cancel Appointment");
        System.out.println("8. Reschedule Appointment");
        System.out.println("9. View Available Appointment Slots");
        System.out.println("10. View Past Outcome Records");
        System.out.println("11. View Billing Details");
        System.out.println("12. Change Password");
        System.out.println("13. Log Out");
    }

    private int getUserInput() {
        int choice = -1;
        try {
            choice = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            showErrorMessage("Invalid input. Please enter a number between 1 and 12.");
        }
        return choice;
    }

    public void showBillingOptions(Patient patient) {
        boolean isBillingMenuRunning = true;

        while (isBillingMenuRunning) {
            System.out.println("\n==== BILLING OPTIONS ====");
            System.out.println("1. View Pending Payments");
            System.out.println("2. View Completed Payments");
            System.out.println("3. Pay a Bill");
            System.out.println("4. Exit Billing Menu");
            System.out.print("Enter your choice: ");

            int choice = getUserInput();

            switch (choice) {
                case 1 -> {
                    List<Appointment> pendingPayments = billingController.getPendingPayments(patient.getAppointments());
                    if (pendingPayments.isEmpty()) {
                        System.out.println("No pending payments found.");
                    } else {
                        System.out.println("\nPending Payments:");
                        for (Appointment app : pendingPayments) {
                            System.out.println("Appointment ID: " + app.getAppointmentId());
                            System.out.println("Service: " + app.getServiceProvided());
                            System.out.println("---------------------");
                        }
                    }
                }
                case 2 -> {
                    List<String> completedPayments = PaymentLogger.getLoggedPayments();
                    if (completedPayments.isEmpty()) {
                        System.out.println("No completed payments found.");
                    } else {
                        System.out.println("\nCompleted Payments:");
                        for (String payment : completedPayments) {
                            String[] parts = payment.split(",");
                            System.out.println("Appointment ID: " + parts[0]);
                            System.out.println("Amount Paid: $" + parts[1]);
                            System.out.println("Service Provided: " + parts[2]);
                            System.out.println("---------------------");
                        }
                    }
                }
                case 3 -> {
                    System.out.print("Enter Appointment ID to pay bill: ");
                    String appointmentId = scanner.nextLine().trim();

                    String billDetails = billingController.calculateBill(appointmentId);
                    System.out.println(billDetails);

                    if (billDetails.contains("Total Bill")) {
                        System.out.print("Do you want to pay this bill? (Y/N): ");
                        String paymentConfirmation = scanner.nextLine().trim().toUpperCase();

                        if (paymentConfirmation.equals("Y") || paymentConfirmation.equals("YES")) {
                            boolean isPaid = billingController.payBill(appointmentId);
                            if (isPaid) {
                                System.out.println("Payment successful.");
                            } else {
                                System.out.println("Payment failed. Please try again.");
                            }
                        } else {
                            System.out.println("Bill payment canceled.");
                        }
                    }
                }
                case 4 -> {
                    System.out.println("Exiting Billing Menu...");
                    isBillingMenuRunning = false;
                }
                default -> System.out.println("Invalid choice! Please try again.");
            }
        }
    }



    @Override
    public void display(Patient patient) {
        System.out.println("Displaying information for patient ID: " + patient.getHospitalID());
    }

    @Override
    public void showPatientDetails(Patient patient) {
        if (patient != null) {
            System.out.println("Patient ID: " + patient.getHospitalID());
            System.out.println("Name: " + patient.getName());
            System.out.println("Contact: " + patient.getContactInformation());
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
