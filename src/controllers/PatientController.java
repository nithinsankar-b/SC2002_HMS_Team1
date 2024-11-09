package controllers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import models.Doctor;

import models.Patient;
import models.Appointment;
import services.AppointmentService;
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

public class PatientController {
    private final PatientService patientService;
    private final AppointmentService appointmentService;
    private final AllocatedAppointmentView allocatedAppointmentView;
    private final AppointmentHistoryView appointmentHistoryView;
    private final MedicalRecordView medicalRecordView;

    // Constructor
    public PatientController(PatientService patientService, AppointmentService appointmentService) {
        this.patientService = patientService;
        this.appointmentService = appointmentService;

        // Pass the AppointmentService instance to each view
        this.allocatedAppointmentView = new AllocatedAppointmentView(appointmentService);
        this.appointmentHistoryView = new AppointmentHistoryView(appointmentService);
        this.medicalRecordView = new MedicalRecordView(appointmentService);
    }

    // Method to view patient details
    public void viewPatientDetails(Patient patient) {
        UserService userService=new UserService();
        ScheduleService scheduleService=new services.ScheduleService();
        MedicalRecordService medicalRecordService=new MedicalRecordService();
        DoctorService doctorService=new DoctorService(userService, scheduleService,medicalRecordService,appointmentService);
        DoctorController doctorController=new controllers.DoctorController(doctorService,scheduleService,medicalRecordService, appointmentService);
        doctorController.medicalRecordsView(patient.getHospitalID());
    }

    // Method to view allocated appointments
    public void viewAllocatedAppointments(Patient patient) {
        if (patient != null) {
            allocatedAppointmentView.display(patient);
        } else {
            System.out.println("Patient not found.");
        }
    }

    // Method to view appointment history
    public void viewAppointmentHistory(Patient patient) {
        if (patient != null) {
            appointmentHistoryView.display(patient);
        } else {
            System.out.println("Patient not found.");
        }
    }

    // Method to view medical records
    public void viewMedicalRecords(Patient patient) {
        if (patient != null) {
            medicalRecordView.display(patient);
        } else {
            System.out.println("Patient not found.");
        }
    }

    // Method to update contact information
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
                System.out.println("Contact information updated successfully.\nUpdated information will be reflected upon next login.");
            } else {
                System.out.println("Failed to update contact information. Patient not found.");
            }
        } else {
            System.out.println("Patient not found.");
        }
    }

    // Method to create an appointment for a patient

    public void createAppointment(Patient patient) {
        if (patient != null) {
            Scanner scanner = new Scanner(System.in);

            // Load doctors from doctor.csv file and display their ID and Name
            try (BufferedReader br = new BufferedReader(new FileReader("data//doctor.csv"))) {
                String line;
                boolean headerSkipped = false;

                System.out.println("Available Doctors:");
                while ((line = br.readLine()) != null) {
                    // Skip the header line
                    if (!headerSkipped && line.startsWith("HospitalID")) {
                        headerSkipped = true;
                        continue;
                    }

                    // Split CSV line and get doctor ID and name
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
            //Scanner scanner = new Scanner(System.in);
            System.out.print("Enter Doctor ID: ");
            String doctorId = scanner.nextLine();

            // Ask for the date separately by day and month
            System.out.print("Enter Day (1-31): ");
            int day = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter Month (1-12): ");
            int month = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter Year (YYYY): ");
            int year = Integer.parseInt(scanner.nextLine());
            LocalDate localDate = LocalDate.of(year, month, day);

            // Display available slots for the given day and doctor
            List<LocalDateTime> availableSlots = appointmentService.getAvailableSlots(doctorId, localDate);
            if (availableSlots.isEmpty()) {
                System.out.println("No available slots for the given date.");
                return;
            } else {
                // Adjusted Formatter to show time in HH:mm format and include "HRS"
                DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm 'HRS'");
                System.out.println("Available time slots for " + localDate + ":");
                availableSlots.forEach(slot -> System.out.println(slot.toLocalTime().format(timeFormatter)));
            }

            // Ask user to pick a time
            System.out.print("Enter appointment time (24HRS format -> HH:mm): ");
            String time = scanner.nextLine();

            // Parse the time and combine with date
            time = time.replaceAll("[^0-9]", "");
            if (time.length() == 4) {
                time = time.substring(0, 2) + ":" + time.substring(2);
            }
            LocalTime localTime = LocalTime.parse(time);
            LocalDateTime dateTime = LocalDateTime.of(localDate, localTime);

            // Check if the patient already has an appointment on the same day
            List<Appointment> patientAppointments = appointmentService.viewScheduledAppointments();
            boolean hasAppointmentOnSameDay = patientAppointments.stream()
                    .anyMatch(appointment -> appointment.getPatientId().equals(patient.getHospitalID()) &&
                            appointment.getAppointmentDateTime().toLocalDate().equals(localDate));

            if (hasAppointmentOnSameDay) {
                System.out.println("You already have an appointment booked on the selected day. Please choose another day.");
                return;
            }

            // Check if the time slot is available
            if (availableSlots.contains(dateTime)) {
                String appointmentId = String.valueOf(System.currentTimeMillis());
                Appointment appointment = new Appointment(appointmentId, patient.getHospitalID(), doctorId, dateTime);

                boolean success = appointmentService.scheduleAppointment(appointment);
                if (success) {
                    System.out.println("Appointment created successfully.");
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


    // Method to cancel an appointment for a patient
    public void cancelAppointment(Patient patient) {
        viewAllocatedAppointments(patient);
        if (patient == null) {
            System.out.println("Patient not found.");
            return; // Exit if no patient is provided
        }

        // Retrieve all scheduled appointments and filter by hospitalID
        List<Appointment> appointments = appointmentService.viewScheduledAppointments();

        // Filter the list of appointments by patient's hospitalID
        List<Appointment> patientAppointments = appointments.stream()
                .filter(appointment -> appointment.getPatientId().equals(patient.getHospitalID()))
                .collect(Collectors.toList());

        // Check if the filtered list of appointments is empty
        if (patientAppointments.isEmpty()) {
            //System.out.println("You have no appointments to reschedule.");
            return; // Exit if no appointments found
        }
        if (patient != null) {
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter Appointment ID: ");
            String appointmentId = scanner.nextLine();

            boolean success = appointmentService.cancelAppointment(appointmentId);
            if (success) {
                System.out.println("Appointment cancelled successfully.");
            } else {
                System.out.println("Failed to cancel appointment. Please check the details and try again.");
            }
        } else {
            System.out.println("Patient not found.");
        }
    }

    // Method to reschedule an existing appointment
    public void rescheduleAppointment(Patient patient) {
        viewAllocatedAppointments(patient);
        if (patient == null) {
            System.out.println("Patient not found.");
            return; // Exit if no patient is provided
        }

        // Retrieve all scheduled appointments and filter by hospitalID
        List<Appointment> appointments = appointmentService.viewScheduledAppointments();

        // Filter the list of appointments by patient's hospitalID
        List<Appointment> patientAppointments = appointments.stream()
                .filter(appointment -> appointment.getPatientId().equals(patient.getHospitalID()))
                .collect(Collectors.toList());

        // Check if the filtered list of appointments is empty
        if (patientAppointments.isEmpty()) {
            //System.out.println("You have no appointments to reschedule.");
            return; // Exit if no appointments found
        }

        if (patient != null) {
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

                    // Ask for the new appointment date in yyyy-MM-dd format
                    System.out.print("Enter Day (1-31): ");
                    int day = Integer.parseInt(scanner.nextLine());

                    System.out.print("Enter Month (1-12): ");
                    int month = Integer.parseInt(scanner.nextLine());

                    System.out.print("Enter Year (YYYY): ");
                    int year = Integer.parseInt(scanner.nextLine());
                    LocalDate newDate = LocalDate.of(year, month, day);

                    // Fetch available slots for that doctor on the specified date
                    List<LocalDateTime> availableSlots = appointmentService.getAvailableSlots(doctorId, newDate);
                    if (availableSlots.isEmpty()) {
                        System.out.println("No available slots for the given date.");
                        return; // Exit the method if no slots are available
                    } else {
                        // Display the available slots to the user
                        System.out.println("Available slots:");
                        DateTimeFormatter slotFormatter = DateTimeFormatter.ofPattern("HH:mm 'HRS'");
                        availableSlots.forEach(slot -> System.out.println(slot.format(slotFormatter)));



                        System.out.print("Enter new appointment time (e.g., 1430, 1430HRS, or 14:30): ");
                        String newTimeStr = scanner.nextLine();

                        // Normalize the input to extract the numeric time
                        newTimeStr = newTimeStr.replaceAll("[^0-9]", "");

                        // Ensure the time is in the correct format (HH:mm)
                        if (newTimeStr.length() == 4) {
                            newTimeStr = newTimeStr.substring(0, 2) + ":" + newTimeStr.substring(2);
                        }

                        // Parse the time to LocalTime
                        LocalTime newTime = LocalTime.parse(newTimeStr);

                        // Combine the date and time to create a LocalDateTime object
                        LocalDateTime newDateTime = LocalDateTime.of(newDate, newTime);

                        // Validate that the entered time is one of the available slots
                        if (!availableSlots.contains(newDateTime)) {
                            System.out.println("The entered time is not available. Please choose from the available slots.");
                            return; // Exit the method if the time is not valid
                        }

                        // Check if the chosen slot is the same as the current appointment time
                        if (currentDateTime.isEqual(newDateTime)) {
                            System.out.println("The new date and time are the same as the existing appointment. No changes made.");
                            return; // Exit the method if no changes are needed
                        }

                        // Proceed to reschedule if the date or time is different
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
        } else {
            System.out.println("Patient not found.");
        }
    }

    // Method to view available appointment slots
    public void viewAvailableAppointmentSlots() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Doctor ID: ");
        String doctorId = scanner.nextLine();
        System.out.print("Enter date for available slots (yyyy-MM-dd): ");
        String dateStr = scanner.nextLine();

        LocalDate date = LocalDate.parse(dateStr);

        List<LocalDateTime> availableSlots = appointmentService.getAvailableSlots(doctorId, date);
        if (availableSlots.isEmpty()) {
            System.out.println("No available slots for the given date.");
        } else {
            System.out.println("Available slots:");

            // Define the date and time format in 24-hour format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm 'HRS'");

            // Format and display each slot
            availableSlots.forEach(slot ->
                    System.out.println(slot.format(formatter))
            );
        }
    }
}