package controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import models.Patient;
import models.Appointment;
import services.AppointmentService;
import services.PatientService;
import views.AllocatedAppointmentView;
import views.AppointmentHistoryView;
import views.MedicalRecordView;

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
        if (patient != null) {
            allocatedAppointmentView.showPatientDetails(patient);
        } else {
            allocatedAppointmentView.showErrorMessage("Patient not found.");
        }
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
            System.out.print("Enter new contact information: ");
            String newContactInfo = scanner.nextLine();

            boolean success = patientService.updatePatientContact(patient.getHospitalID(), newContactInfo);
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
            System.out.print("Enter Doctor ID (Case Sensitive): ");
            String doctorId = scanner.nextLine();

            System.out.print("Enter appointment date (yyyy-MM-dd): ");
            String date = scanner.nextLine();

            try {
                // Parse the date
                LocalDate localDate = LocalDate.parse(date);

                // Display available slots for the given day
                List<LocalDateTime> availableSlots = appointmentService.getAvailableSlots(doctorId, localDate);
                if (availableSlots.isEmpty()) {
                    System.out.println("No available slots for the given date.");
                    return;
                } else {
                    // Adjusted Formatter to show time in HH:mm format and include "HRS"
                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm 'HRS'");
                    System.out.println("Available time slots:");
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
            } catch (Exception e) {
                System.out.println("Invalid date or time format. Please use yyyy-MM-dd for date and HH:mm for time.");
            }
        } else {
            System.out.println("Patient not found.");
        }
    }

    // Method to cancel an appointment for a patient
    public void cancelAppointment(Patient patient) {
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
                    System.out.print("Enter new appointment date (yyyy-MM-dd): ");
                    String newDateStr = scanner.nextLine();

                    // Define the formatter for date
                    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                    // Parse the date
                    LocalDate newDate = LocalDate.parse(newDateStr, dateFormatter);

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

                        // Ask the user to enter a new time from the available options
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