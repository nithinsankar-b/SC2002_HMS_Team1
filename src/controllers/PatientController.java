package controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import models.Medication;
import models.Doctor;

import models.Patient;
import models.Appointment;
import services.AppointmentService;
import models.AppointmentRequest;
import services.AppointmentRequestService;
import services.MedicalRecordService;
import services.PatientService;
import services.ScheduleService;
import services.DoctorService;
import services.UserService;
import views.AllocatedAppointmentView;
import views.AppointmentHistoryView;
import views.MedicalRecordView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import controllers.DoctorController;
import java.util.stream.Collectors;

/**
 * Controller class for managing patient-related operations.
 * This class handles patient functionalities such as viewing details, updating contact information,
 * viewing appointments, and creating new appointments.
 */
public class PatientController {

    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final AllocatedAppointmentView allocatedAppointmentView;
    private final AppointmentHistoryView appointmentHistoryView;
    private final MedicalRecordView medicalRecordView;

    /**
     * Constructor for the PatientController.
     *
     * @param patientService      The service for handling patient-related operations.
     * @param appointmentService  The service for managing appointments.
     */
    public PatientController(PatientService patientService, AppointmentService appointmentService) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;

        // Initialize views
        this.allocatedAppointmentView = new AllocatedAppointmentView(appointmentService);
        this.appointmentHistoryView = new AppointmentHistoryView(appointmentService);
        this.medicalRecordView = new MedicalRecordView(appointmentService);
    }

    /**
     * Displays patient details by fetching and viewing medical records.
     *
     * @param patient The patient object containing the details.
     */
    public void viewPatientDetails(Patient patient) {
        UserService userService = new UserService();
        ScheduleService scheduleService = new ScheduleService();
        MedicalRecordService medicalRecordService = new MedicalRecordService();
        DoctorService doctorService = new DoctorService(userService, scheduleService, medicalRecordService, appointmentService);
        DoctorController doctorController = new DoctorController(doctorService, scheduleService, medicalRecordService, appointmentService);
        doctorController.medicalRecordsView(patient.getHospitalID());
    }

    /**
     * Displays the allocated appointments for the patient.
     *
     * @param patient The patient object containing the details.
     */
    public void viewAllocatedAppointments(Patient patient) {
        if (patient != null) {
            allocatedAppointmentView.display(patient);
        } else {
            System.out.println("Patient not found.");
        }
    }

    /**
     * Displays the allocated appointments for the patient (alternative view).
     *
     * @param patient The patient object containing the details.
     */
    public void viewAllocatedAppointments2(Patient patient) {
        if (patient != null) {
            allocatedAppointmentView.display2(patient);
        } else {
            System.out.println("Patient not found.");
        }
    }

    /**
     * Displays the appointment history for the patient.
     *
     * @param patient The patient object containing the details.
     */
    public void viewAppointmentHistory(Patient patient) {
        if (patient != null) {
            appointmentHistoryView.display(patient);
        } else {
            System.out.println("Patient not found.");
        }
    }

    /**
     * Displays the medical records for the patient.
     *
     * @param patient The patient object containing the details.
     */
    public void viewMedicalRecords(Patient patient) {
        if (patient != null) {
            medicalRecordView.display(patient);
        } else {
            System.out.println("Patient not found.");
        }
    }

    /**
     * Updates the contact information of the patient (phone number or email).
     *
     * @param patient The patient object containing the details.
     */
    public void updateContactInformation(Patient patient) {
        if (patient != null) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("Which contact information would you like to update?");
            System.out.println("1. Phone Number");
            System.out.println("2. Email Address");
            System.out.print("Enter your choice (1 or 2): ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            String contactType = "";
            if (choice == 1) {
                contactType = "phone";
            } else if (choice == 2) {
                contactType = "email";
            } else {
                System.out.println("Invalid choice. Please select 1 or 2.");
                return;
            }

            System.out.print("Enter new " + (contactType.equals("phone") ? "phone number" : "email address") + ": ");
            String newContactInfo = scanner.nextLine();

            boolean success = patientService.updatePatientContact(patient.getHospitalID(), newContactInfo, contactType);
            if (success) {
                System.out.println("Contact information updated successfully.");
            } else {
                System.out.println("Failed to update contact information. Patient not found.");
            }
        } else {
            System.out.println("Patient not found.");
        }
    }

    /**
     * Creates an appointment for the patient with the selected doctor on a specified date and time.
     *
     * @param patient The patient object containing the details.
     */
    public void createAppointment(Patient patient) {
        if (patient != null) {
            Scanner scanner = new Scanner(System.in);

            // Load doctors from doctor.csv file and display their ID and Name, skipping the header
            try (BufferedReader br = new BufferedReader(new FileReader("data//doctor.csv"))) {
                String line;
                boolean headerSkipped = false;

                System.out.println("Available Doctors:");
                while ((line = br.readLine()) != null) {
                    if (!headerSkipped) {
                        headerSkipped = true; // Skip the header line
                        continue;
                    }

                    String[] values = line.split(",");
                    String hospitalID = values[0].trim();
                    String name = values[1].trim();

                    System.out.println("Doctor ID: " + hospitalID + ", Name: " + name);
                }
            } catch (IOException e) {
                System.out.println("Error reading doctor file: " + e.getMessage());
                return;
            }

            // Ask for Doctor ID
            System.out.print("Enter Doctor ID: ");
            String doctorId = scanner.nextLine();

            System.out.println("Enter a date in the upcoming month: ");
            int day = 0, month = 0, year = 0;
            boolean validInput = false;

            while (!validInput) {
                try {
                    System.out.print("Enter Day (1-31): ");
                    day = Integer.parseInt(scanner.nextLine());
                    if (day < 1 || day > 31) {
                        System.out.println("Invalid day! Please enter a number between 1 and 31.");
                        continue;
                    }

                    System.out.print("Enter Month (1-12): ");
                    month = Integer.parseInt(scanner.nextLine());
                    if (month < 1 || month > 12) {
                        System.out.println("Invalid month! Please enter a number between 1 and 12.");
                        continue;
                    }

                    System.out.print("Enter Year (YYYY): ");
                    year = Integer.parseInt(scanner.nextLine());
                    if (year < 1000 || year > 9999) {
                        System.out.println("Invalid year! Please enter a valid 4-digit year.");
                        continue;
                    }

                    // If all inputs are valid, exit the loop
                    validInput = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Please enter a valid number.");
                }
            }

            LocalDate localDate = LocalDate.of(year, month, day);

            LocalDate startDate = LocalDate.of(2024, 11, 17);
            LocalDate endDate = LocalDate.of(2024, 12, 18);

            if (localDate.isBefore(startDate) || localDate.isAfter(endDate)) {
                System.out.println("Appointments can only be booked for one month in advance.");
                return;
            }

            // Display available slots for the given day and doctor
            List<LocalDateTime> availableSlots = appointmentService.getAvailableSlots(doctorId, localDate);
            if (availableSlots.isEmpty()) {
                System.out.println("Invalid Doctor ID!");
                return;
            } else {
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm 'HRS'");
                System.out.println("Available time slots for " + localDate + ":");
                availableSlots.forEach(slot -> System.out.println(slot.toLocalTime().format(timeFormatter)));
            }

            // Ask user to pick a time
            System.out.print("Enter appointment time (24HRS format -> HH:mm): ");
            String time = scanner.nextLine();
            time = time.replaceAll("[^0-9]", "");
            if (time.length() == 4) {
                time = time.substring(0, 2) + ":" + time.substring(2);
            }
            LocalTime localTime = LocalTime.parse(time);
            LocalDateTime dateTime = LocalDateTime.of(localDate, localTime);

            List<Appointment> patientAppointments = appointmentService.viewScheduledAppointments();
            boolean hasAppointmentOnSameDay = patientAppointments.stream()
                    .anyMatch(appointment -> appointment.getPatientId().equals(patient.getHospitalID()) &&
                            appointment.getAppointmentDateTime().toLocalDate().equals(localDate));

            if (hasAppointmentOnSameDay) {
                System.out.println("You already have an appointment booked on the selected day. Please choose another day.");
                return;
            }

            if (availableSlots.contains(dateTime)) {
                String appointmentId = String.valueOf(System.currentTimeMillis()).substring(6);
                Appointment appointment = new Appointment(appointmentId, patient.getHospitalID(), doctorId, dateTime);

                boolean success = appointmentService.scheduleAppointment(appointment);
                if (success) {
                    System.out.println("Appointment created successfully.");

                    // Create and save the corresponding AppointmentRequest
                    AppointmentRequest appointmentRequest = new AppointmentRequest(
                            appointmentId,
                            patient.getHospitalID(),
                            doctorId,
                            localDate,
                            localTime,
                            "Pending"
                    );
                    ScheduleService scheduleService = new ScheduleService();
                    AppointmentRequestService appointmentRequestService = new AppointmentRequestService(scheduleService, appointmentService);
                    appointmentRequestService.save(appointmentRequest);
                    System.out.println("Appointment request created with ID: " + appointmentRequest.getRequestId());
                } else {
                    System.out.println("Failed to create appointment.");
                }
            } else {
                System.out.println("The selected time slot is not available. Please choose another time.");
            }
        } else {
            System.out.println("Patient not found.");
        }
    }

/**
 * Cancels an appointment for the given patient.
 * <p>
 * This method allows a patient to cancel an existing appointment by providing
 * the appointment ID. The list of scheduled appointments is filtered by the
 * patient's hospital ID to ensure that only their appointments are considered.
 * If a valid appointment is found, it is canceled.
 * </p>
 *
 * @param patient The patient whose appointment is to be canceled. The method
 *                verifies that the patient exists and has appointments before
 *                proceeding.
 */
public void cancelAppointment(Patient patient) {
    // Display the patient's allocated appointments
    viewAllocatedAppointments(patient);

    // Check if the patient is null
    if (patient == null) {
        System.out.println("Patient not found.");
        return; // Exit if no patient is provided
    }

    // Retrieve all scheduled appointments and filter by the patient's hospital ID
    List<Appointment> appointments = appointmentService.viewScheduledAppointments();

    // Filter the list of appointments by the patient's hospital ID
    List<Appointment> patientAppointments = appointments.stream()
            .filter(appointment -> appointment.getPatientId().equals(patient.getHospitalID()))
            .collect(Collectors.toList());

    // Check if the filtered list of appointments is empty
    if (patientAppointments.isEmpty()) {
        // No appointments found for the patient
        return; // Exit if no appointments are found
    }

    // If the patient has appointments, proceed to cancel
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter Appointment ID: ");
    String appointmentId = scanner.nextLine();

    // Attempt to cancel the appointment
    boolean success = appointmentService.cancelAppointment(appointmentId);
    if (success) {
        System.out.println("Appointment cancelled successfully.");
    } else {
        System.out.println("Failed to cancel appointment. Please check the details and try again.");
    }
}


/**
 * Reschedules an existing appointment for the given patient.
 * <p>
 * This method allows a patient to reschedule an existing appointment to a new date and time.
 * It checks the patient's existing appointments and ensures the selected date and time 
 * are within the allowed range and available for the specified doctor.
 * </p>
 *
 * @param patient The patient who wants to reschedule an appointment. 
 *                The method ensures that the patient exists and has valid appointments.
 */
public void rescheduleAppointment(Patient patient) {
    // Display the patient's allocated appointments
    viewAllocatedAppointments(patient);

    // Check if the patient is null
    if (patient == null) {
        System.out.println("Patient not found.");
        return; // Exit if no patient is provided
    }

    // Retrieve all scheduled appointments and filter by the patient's hospital ID
    List<Appointment> appointments = appointmentService.viewScheduledAppointments();

    // Filter the list of appointments by the patient's hospital ID
    List<Appointment> patientAppointments = appointments.stream()
            .filter(appointment -> appointment.getPatientId().equals(patient.getHospitalID()))
            .collect(Collectors.toList());

    // Check if the filtered list of appointments is empty
    if (patientAppointments.isEmpty()) {
        return; // Exit if no appointments found
    }

    // Proceed if the patient exists and has appointments
    Scanner scanner = new Scanner(System.in);
    System.out.print("Enter Appointment ID: ");
    String appointmentId = scanner.nextLine();

    try {
        // Retrieve the existing appointment details
        Appointment appointment = appointmentService.getAppointment(appointmentId);
        if (appointment != null && appointment.getPatientId().equals(patient.getHospitalID())) {
            String doctorId = appointment.getDoctorId();

            // Display current appointment date and time
            LocalDateTime currentDateTime = appointment.getAppointmentDateTime();
            DateTimeFormatter currentFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm 'HRS'");
            System.out.println("Current Appointment Slot: " + currentDateTime.format(currentFormatter));

            System.out.println("Enter a date in the upcoming month: ");
            // Ask for the new appointment date
            int day = 0, month = 0, year = 0;
            boolean validInput = false;

            // Validate the new date input
            while (!validInput) {
                try {
                    System.out.print("Enter Day (1-31): ");
                    day = Integer.parseInt(scanner.nextLine());
                    if (day < 1 || day > 31) {
                        System.out.println("Invalid day! Please enter a number between 1 and 31.");
                        continue;
                    }

                    System.out.print("Enter Month (1-12): ");
                    month = Integer.parseInt(scanner.nextLine());
                    if (month < 1 || month > 12) {
                        System.out.println("Invalid month! Please enter a number between 1 and 12.");
                        continue;
                    }

                    System.out.print("Enter Year (YYYY): ");
                    year = Integer.parseInt(scanner.nextLine());
                    if (year < 1000 || year > 9999) {
                        System.out.println("Invalid year! Please enter a valid 4-digit year.");
                        continue;
                    }

                    validInput = true; // Exit loop if all inputs are valid
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Please enter a valid number.");
                }
            }

            LocalDate newDate = LocalDate.of(year, month, day);

            // Define the allowed date range for rescheduling
            LocalDate rangeStartDate = LocalDate.of(2024, 11, 17);
            LocalDate rangeEndDate = LocalDate.of(2024, 12, 18);

            // Check if the new date is within the allowed range
            if (newDate.isBefore(rangeStartDate) || newDate.isAfter(rangeEndDate)) {
                System.out.println("Appointments can only be booked for one month in advance.");
                return; // Exit if the new date is outside the allowed range
            }

            // Fetch available slots for the specified doctor on the new date
            List<LocalDateTime> availableSlots = appointmentService.getAvailableSlots(doctorId, newDate);
            if (availableSlots.isEmpty()) {
                System.out.println("Invalid Doctor ID.");
                return; // Exit if no slots are available
            } else {
                // Display available slots
                System.out.println("Available slots:");
                DateTimeFormatter slotFormatter = DateTimeFormatter.ofPattern("HH:mm 'HRS'");
                availableSlots.forEach(slot -> System.out.println(slot.format(slotFormatter)));

                System.out.print("Enter new appointment time (e.g., 1430, 1430HRS, or 14:30): ");
                String newTimeStr = scanner.nextLine();

                // Normalize and parse the time input
                newTimeStr = newTimeStr.replaceAll("[^0-9]", "");
                if (newTimeStr.length() == 4) {
                    newTimeStr = newTimeStr.substring(0, 2) + ":" + newTimeStr.substring(2);
                }
                LocalTime newTime = LocalTime.parse(newTimeStr);

                // Combine the date and time for the new appointment
                LocalDateTime newDateTime = LocalDateTime.of(newDate, newTime);

                // Validate that the entered time is one of the available slots
                if (!availableSlots.contains(newDateTime)) {
                    System.out.println("The entered time is not available. Please choose from the available slots.");
                    return; // Exit if the time is invalid
                }

                // Check if the new slot is the same as the current appointment slot
                if (currentDateTime.isEqual(newDateTime)) {
                    System.out.println("The new date and time are the same as the existing appointment. No changes made.");
                    return; // Exit if no changes are needed
                }

                // Proceed to reschedule the appointment
                Appointment newAppointment = new Appointment(
                        appointment.getAppointmentId(),
                        appointment.getPatientId(),
                        appointment.getDoctorId(),
                        newDateTime
                );
                newAppointment.setServiceProvided("Rescheduled");
                newAppointment.setStatus(appointment.getStatus());
                newAppointment.setConsultationNotes(appointment.getConsultationNotes());
                newAppointment.getMedications().addAll(appointment.getMedications());

                boolean success = appointmentService.rescheduleAppointment(appointmentId, newAppointment);
                if (success) {
                    System.out.println("Appointment rescheduled successfully.");
                } else {
                    System.out.println("Failed to reschedule appointment. Please check the details and try again.");
                }
            }
        } else {
            System.out.println("Appointment not found or does not belong to the patient.");
        }
    } catch (Exception e) {
        System.out.println("Invalid input. Please try again.");
    }
}


/**
 * Displays available appointment slots for a specific doctor on a specified date.
 * <p>
 * This method reads doctor details from a CSV file, allows the user to select a doctor,
 * and input a valid date to view available appointment slots. It validates the date input
 * to ensure it's within the allowed booking window.
 * </p>
 */
public void viewAvailableAppointmentSlots() {
    Scanner scanner = new Scanner(System.in);

    // Display available doctors from the CSV file
    try (BufferedReader br = new BufferedReader(new FileReader("data//doctor.csv"))) {
        String line;
        boolean headerSkipped = false;

        System.out.println("Available Doctors:");
        while ((line = br.readLine()) != null) {
            if (!headerSkipped) {
                headerSkipped = true; // Skip the header line
                continue;
            }

            String[] values = line.split(",");
            String hospitalID = values[0].trim();
            String name = values[1].trim();

            System.out.println("Doctor ID: " + hospitalID + ", Name: " + name);
        }
    } catch (IOException e) {
        System.out.println("Error reading doctor file: " + e.getMessage());
        return;
    }

    // Ask for Doctor ID
    System.out.print("Enter Doctor ID: ");
    String doctorId = scanner.nextLine();

    // Ask for appointment date
    int day = 0, month = 0, year = 0;
    boolean validInput = false;

    while (!validInput) {
        try {
            System.out.print("Enter Day (1-31): ");
            day = Integer.parseInt(scanner.nextLine());
            if (day < 1 || day > 31) {
                System.out.println("Invalid day! Please enter a number between 1 and 31.");
                continue;
            }

            System.out.print("Enter Month (1-12): ");
            month = Integer.parseInt(scanner.nextLine());
            if (month < 1 || month > 12) {
                System.out.println("Invalid month! Please enter a number between 1 and 12.");
                continue;
            }

            System.out.print("Enter Year (YYYY): ");
            year = Integer.parseInt(scanner.nextLine());
            if (year < 1000 || year > 9999) {
                System.out.println("Invalid year! Please enter a valid 4-digit year.");
                continue;
            }

            // If all inputs are valid, exit the loop
            validInput = true;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a valid number.");
        }
    }

    // Parse and validate the date
    LocalDate date;
    try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        date = LocalDate.parse(String.format("%02d-%02d-%04d", day, month, year), formatter);
    } catch (DateTimeParseException e) {
        System.out.println("Invalid date format.");
        return;
    }

    // Define appointment booking window
    LocalDate startDate = LocalDate.of(2024, 11, 17);
    LocalDate endDate = LocalDate.of(2024, 12, 18);
    LocalDateTime startDateTime = LocalDateTime.of(startDate, LocalTime.of(11, 30));
    LocalDateTime endDateTime = LocalDateTime.of(endDate, LocalTime.of(11, 0));

    if (date.isBefore(startDate) || date.isAfter(endDate) ||
            (date.isEqual(startDate) && date.atStartOfDay().isBefore(startDateTime))) {
        System.out.println("Appointments can only be booked for one month in advance.");
        return;
    }

    // Get available slots from the appointment service
    List<LocalDateTime> availableSlots = appointmentService.getAvailableSlots(doctorId, date);
    if (availableSlots.isEmpty()) {
        System.out.println("Invalid Doctor ID!");
    } else {
        System.out.println("Available slots:");

        DateTimeFormatter displayFormatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm 'HRS'");
        availableSlots.forEach(slot -> System.out.println(slot.format(displayFormatter)));
    }
}

/**
 * Displays past completed appointments for a specific patient.
 * <p>
 * This method fetches all scheduled appointments, filters out those
 * marked as completed and associated with the given patient, and
 * displays their details, including medications and consultation notes.
 * </p>
 *
 * @param patient The patient whose past completed records are to be viewed.
 */
public void viewPastRecords(Patient patient) {
    // Fetch all the appointments for this patient
    AppointmentService appointmentService = new AppointmentService();
    List<Appointment> allAppointments = appointmentService.viewScheduledAppointments();

    // Filter out the appointments that are completed and belong to this patient
    List<Appointment> completedAppointments = new ArrayList<>();
    for (Appointment appointment : allAppointments) {
        if (appointment.getPatientId().equals(patient.getHospitalID()) &&
                enums.AppointmentStatus.COMPLETED.equals(appointment.getStatus())) {
            completedAppointments.add(appointment);
        }
    }

    // If there are any completed appointments, display them
    if (completedAppointments.isEmpty()) {
        System.out.println("No past completed appointments found for Patient ID: " + patient.getHospitalID());
    } else {
        System.out.println("Displaying Past Completed Appointments for Patient: " + patient.getName() + " (ID: " + patient.getHospitalID() + ")");
        for (Appointment appointment : completedAppointments) {
            List<Medication> medications = appointment.getMedications();
            System.out.println("Appointment ID      : " + appointment.getAppointmentId());
            System.out.println("Patient ID          : " + appointment.getPatientId());
            System.out.println("Doctor ID           : " + appointment.getDoctorId());
            System.out.println("Appointment Date and Time: " + appointment.getAppointmentDateTime());
            System.out.println("Status              : " + appointment.getStatus());
            System.out.println("Consultation Notes  : " + appointment.getConsultationNotes());
            System.out.println("Service Provided    : " + appointment.getServiceProvided());

            // Printing medications neatly
            if (medications != null && !medications.isEmpty()) {
                System.out.println("Medications:");
                for (Medication medication : medications) {
                    System.out.println("    - Name            : " + medication.getName());
                    System.out.println("      Quantity        : " + medication.getQuantity());
                    //System.out.println("      Status          : " + medication.getStatus());
                }
            } else {
                System.out.println("Medications: None");
            }
            System.out.println("Status              : " + appointment.getMedicationStatus());

            //System.out.println("Medication Status   : " + appointment.getMedicationStatus());
            System.out.println("-------------------------------");
        }
    }
}


/**
 * Allows a patient to change their password by verifying the old password and setting a new one.
 * <p>
 * This method prompts the patient to enter their old password and a new password,
 * and then delegates the password change operation to the {@link UserService}.
 * </p>
 *
 * @param patient The patient who wants to change their password.
 */
public void changePassword(Patient patient) {
    // Initialize UserService to handle password change
    UserService userService = new UserService();
    Scanner scanner = new Scanner(System.in);

    // Prompt for old password
    System.out.print("Enter your old password: ");
    String oldPassword = scanner.nextLine();

    // Prompt for new password
    System.out.print("Enter your new password: ");
    String newPassword = scanner.nextLine();

    // Delegate the password change to UserService
    userService.changePassword(patient.getHospitalID(), oldPassword, newPassword);
}

}