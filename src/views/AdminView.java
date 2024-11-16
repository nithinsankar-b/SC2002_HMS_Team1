package views;

import enums.UserRole;
import interfaces.IAdministratorView;
import models.Staff;
import models.*;
import models.Appointment;
import models.Inventory;
import services.PatientService;
import services.ProjectAdminService;
import controllers.AdministratorController;
import services.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.lang.String;

/**
 * The AdminView class implements the IAdministratorView interface and provides
 * methods for displaying administrator-related views and gathering user input.
 */
public class AdminView implements IAdministratorView {

    private final Scanner scanner = new Scanner(System.in);
    private final AdministratorController adminController;
    private final ProjectAdminService adminService;
    private final UserService userService;
    private final PatientService patientService;

    /**
     * Constructs an AdminView with the specified AdministratorController and ProjectAdminService.
     *
     * @param adminController The controller managing administrator actions.
     * @param adminService    The service handling administrative operations.
     */
    public AdminView(AdministratorController adminController, ProjectAdminService adminService, UserService userService, PatientService patientService) {
        this.adminController = adminController;
        this.adminService = adminService;
        this.userService = userService;
        this.patientService = patientService;
    }


    /**
     * Displays the administrator's main menu.
     */
    public void displayMenu() {
        System.out.println("Please choose an option:");
        System.out.println("1. Manage Hospital Staff");
        System.out.println("2. Manage Inventory");
        System.out.println("3. View Appointments");
        System.out.println("4. Change Password");
        System.out.println("5. Log Out");
        System.out.print("Enter your choice: ");
    }

    @Override
    public void displayListOfStaff(List<Staff> staffList) {
        System.out.println("Hospital Staff List:");
        if (staffList.isEmpty()) {
            System.out.println("No staff members found.");
        } else {
            for (Staff staff : staffList) {
                System.out.println("Staff ID: " + staff.getId() + ", Name: " + staff.getName() +
                                   ", Role: " + staff.getRole() + ", Gender: " + staff.getGender() +
                                   ", Age: " + staff.getAge());
            }
        }
    }

    // Implementing displayAppointments from AdministratorView
    @Override
    public void displayAppointments(List<Appointment> appointments) {
        if (appointments.isEmpty()) {
            System.out.println("No appointments scheduled.");
        } else {
            System.out.println("Scheduled Appointments:");
            for (Appointment appointment : appointments) {
                System.out.println("Appointment ID: " + appointment.getAppointmentId());
                System.out.println("Patient ID: " + appointment.getPatientId());
                System.out.println("Doctor ID: " + appointment.getDoctorId());
                System.out.println("Date & Time: " + appointment.getAppointmentDateTime());
                System.out.println("Status: " + appointment.getStatus()); // Added line for status
                System.out.println("-------------------------------");
            }
        }
    }

    @Override
    public void displayInventory(List<Inventory> inventory) {
        System.out.println("Inventory List:");
        for (Inventory item : inventory) {
            System.out.println("Medicine: " + item.getMedicineName() +
                               ", Stock: " + item.getCurrentStock() +
                               ", Low Stock Alert: " + item.getLowLevelAlert() +
                               ", Replenishment Status: " + item.getReplenishmentStatus());
        }
    }

    /**
     * Retrieves the user's menu choice from the administrator's main menu.
     *
     * @return The selected menu option as an integer.
     */
    public int getMenuChoice() {
        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline
        return choice;
    }

    /**
     * Gathers details for a new or updated staff member.
     *
     * @return A new Staff object with the provided details.
     */
    public Staff getStaffDetails() {
        System.out.print("\nEnter Staff ID: ");
        String staffID = scanner.nextLine().trim();

        Staff existingStaff = adminService.getStaffById(staffID); // Check if staff already exists
        if (existingStaff != null) {
            System.out.println("\nStaff ID exists.");
            System.out.println("===========================================");
            System.out.println("1. Update existing staff details");
            System.out.println("2. Cancel");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            if (choice == 1) {
                System.out.println("\nDetails to update for existing staff...");
                System.out.println("===========================================");
                System.out.print("Enter New Name (Leave EMPTY if no change): ");
                String name = scanner.nextLine();
                name = name.isEmpty() ? existingStaff.getName() : name;

                System.out.print("Enter New Gender (Leave EMPTY if no change): ");
                String gender = scanner.nextLine();
                gender = gender.isEmpty() ? existingStaff.getGender() : gender;

                System.out.print("Enter New Age (Leave EMPTY if no change): ");
                String ageInput = scanner.nextLine();
                int age = ageInput.isEmpty() ? existingStaff.getAge() : Integer.parseInt(ageInput);

                return new Staff(existingStaff.getId(), name, existingStaff.getRole(), gender, age);
            } else {
                System.out.println("Operation cancelled.");
                return null;
            }
        }

        System.out.println("===========================================");
        System.out.println("Adding new staff...");
        System.out.println("===========================================");
        System.out.print("Enter Name: ");
        String name = scanner.nextLine();

        String role = staffID.startsWith("D") ? "Doctor" : "Pharmacist";
        System.out.println("Role: " + role);

        System.out.print("Enter Gender (Male/Female): ");
        String gender = scanner.nextLine();

        System.out.print("Enter Age: ");
        int age = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        return new Staff(staffID, name, role, gender, age);
    }

    /**
     * Prompts the user to enter a staff ID for removal.
     *
     * @return The staff ID entered by the user.
     */
    public String getStaffIDForRemoval() {
        System.out.print("Enter Staff ID to remove: ");
        return scanner.nextLine();
    }

    /**
     * Gathers details for a new medication to be added to the inventory.
     *
     * @return A new Inventory object with the provided details.
     */
    public Inventory getNewMedicationDetails() {
        System.out.print("Enter Medicine Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter Initial Stock Level: ");
        int stock = scanner.nextInt();

        System.out.print("Enter Low Stock Alert Level: ");
        int lowStockAlert = scanner.nextInt();

        scanner.nextLine(); // Consume newline
        return new Inventory(name, stock, lowStockAlert, null); // Default null for status
    }

    /**
     * Prompts the user to enter a medicine name for removal from the inventory.
     *
     * @return The medicine name entered by the user.
     */
    public String getMedicineNameForRemoval() {
        System.out.print("Enter Medicine Name to Remove: ");
        return scanner.nextLine();
    }

    /**
     * Prompts the user to enter a medicine name.
     *
     * @return The medicine name entered by the user.
     */
    public String getMedicineName() {
        System.out.print("Enter Medicine Name: ");
        return scanner.nextLine();
    }

    /**
     * Prompts the user to enter a quantity for a medication.
     *
     * @return The quantity entered by the user.
     */
    public int getMedicineQuantity() {
        System.out.print("Enter Medicine Quantity: ");
        return scanner.nextInt();
    }

    /**
     * Prompts the user to enter a new low stock alert level for a medication.
     *
     * @return The low stock alert level entered by the user.
     */
    public int getLowStockAlertLevel() {
        System.out.print("Enter the new low stock alert level: ");
        return scanner.nextInt();
    }

    /**
     * Gathers old and new passwords for changing the administrator's password.
     *
     * @param hospitalID The ID of the hospital for which the password is being changed.
     */
    public void displayChangePassword(String hospitalID) {
        System.out.print("Enter Current Password: ");
        String oldPassword = scanner.nextLine();

        System.out.print("Enter New Password: ");
        String newPassword = scanner.nextLine();

        boolean isSuccess = adminController.changePassword(hospitalID, oldPassword, newPassword);
        if (isSuccess) {
            System.out.println("Password changed successfully!");
        } else {
            System.out.println("Failed to change password. Please check your current password.");
        }
    }

    public String getRequestIdForReplenishment() {
        System.out.print("Enter Request ID: ");
        return scanner.nextLine();
    }

    public String getUserIDForPassword() {
        System.out.print("\nEnter User ID to view password: ");
        return scanner.nextLine();
    }

    // Add a method to get patient details
    public Patient getPatientDetails() {
        String patientID;
        Patient existingPatient;

        while (true) {
            System.out.print("\nEnter Patient ID: ");
            patientID = scanner.nextLine().trim();

            existingPatient = adminService.getPatientById(patientID);
            if (existingPatient == null) {
                break;
            }

            System.out.println("Patient ID already exists. Please enter a new ID.");
        }

        System.out.print("Enter Name: ");
        String name = scanner.nextLine();

        LocalDate dateOfBirth;
        while (true) {
            System.out.print("Enter Date of Birth (yyyy-MM-dd): ");
            String dobInput = scanner.nextLine().trim();
            try {
                dateOfBirth = LocalDate.parse(dobInput);
                break;
            } catch (Exception e) {
                System.out.println("Invalid date format. Please enter in yyyy-MM-dd format.");
            }
        }

        String gender;
        while (true) {
            System.out.print("Enter Gender (Male/Female): ");
            gender = scanner.nextLine().trim().toLowerCase();
            if (gender.equals("male") || gender.equals("female")) {
                break;
            }
            System.out.println("Invalid gender. Please enter 'Male' or 'Female'.");
        }

        String bloodType;
        while (true) {
            System.out.print("Enter Blood Type: ");
            bloodType = scanner.nextLine().trim().toUpperCase();
            if (bloodType.matches("^(A|B|AB|O)[+-]$")) {
                break;
            }
            System.out.println("Invalid blood type. Please enter a valid blood type.");
        }

        String contactInformation;
        while (true) {
            System.out.print("Enter Contact Information (email): ");
            contactInformation = scanner.nextLine().trim();
            if (contactInformation.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                break;
            }
            System.out.println("Invalid email format. Please enter a valid email address.");
        }

        return new Patient(
                new User(patientID, "password", UserRole.PATIENT),
                name, dateOfBirth, gender, bloodType, contactInformation
        );
    }


    // Add a new method to display the list of patients
    public void displayPatientList(List<Patient> patientList) {
        System.out.println("Patient List:");
        if (patientList.isEmpty()) {
            System.out.println("No patients found.");
        } else {
            for (Patient patient : patientList) {
                System.out.println("Patient ID: " + patient.getHospitalID() + ", Name: " + patient.getName() +
                        ", DOB: " + patient.getDateOfBirth() + ", Gender: " + patient.getGender() +
                        ", Blood Type: " + patient.getBloodType() + ", Contact: " + patient.getContactInformation());
            }
        }
    }

}



