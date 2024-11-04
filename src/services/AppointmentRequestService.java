package services;

import models.Appointment;
import models.AppointmentRequest;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for handling appointment requests in the healthcare system.
 * This class provides methods to accept, decline, and process appointment requests,
 * as well as to save and retrieve appointment request data from a CSV file.
 */
public class AppointmentRequestService {
    private final ScheduleService scheduleService; // Service for managing doctor schedules
    private final AppointmentService appointmentService; // Service for managing appointments
    private final String appointmentRequestFile = "data/appointment_request.csv"; // Path to the CSV file

    /**
     * Constructs an AppointmentRequestService with the specified services.
     *
     * @param scheduleService The service for managing doctor schedules.
     * @param appointmentService The service for managing appointments.
     */
    public AppointmentRequestService(ScheduleService scheduleService, AppointmentService appointmentService) {
        this.scheduleService = scheduleService;
        this.appointmentService = appointmentService;
    }

    /**
     * Accepts an appointment request, updates its status, and schedules the appointment.
     *
     * @param request The appointment request to be accepted.
     */
    public void acceptRequest(AppointmentRequest request) {
        // Change request status to accepted
        request.setStatus("Accepted");

        // Create new appointment record based on the request
        LocalDateTime appointmentDateTime = LocalDateTime.of(request.getRequestedDate(), request.getRequestedTimeSlot());

        // Create appointment object
        Appointment appointment = new Appointment(
            request.getRequestId(), // Assuming this will serve as the appointmentId
            request.getPatientId(),
            request.getDoctorId(),
            appointmentDateTime
        );

        // Update the schedule from "Available" to PatientID
        if (scheduleService.bookAppointment(request.getDoctorId(), request.getRequestedDate(), request.getRequestedTimeSlot(), request.getPatientId())) {
            // If successfully booked, update the schedule with PatientID
            scheduleService.setUnavailable(request.getDoctorId(), request.getRequestedDate(), request.getRequestedTimeSlot());

            // Schedule the appointment
            appointmentService.scheduleAppointment(appointment); // Save the appointment record
            saveAppointmentRequest(request); // Save the updated request to CSV
            System.out.println("Appointment accepted and scheduled successfully.");
        } else {
            System.out.println("Failed to book the appointment due to unavailability.");
        }
    }

    /**
     * Retrieves a list of all pending appointment requests from the CSV file.
     *
     * @return A list of pending appointment requests.
     */
    public List<AppointmentRequest> getPendingRequests() {
        List<AppointmentRequest> pendingRequests = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(appointmentRequestFile))) {
            String line;
            // Skip header line
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                AppointmentRequest request = AppointmentRequest.fromString(line);
                if (request != null && request.getStatus().equals("Pending")) {
                    pendingRequests.add(request);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading appointment requests: " + e.getMessage());
        }
        return pendingRequests;
    }

    /**
     * Processes and displays pending appointment requests for a specific doctor.
     *
     * @param doctorID The ID of the doctor whose requests are to be processed.
     */
    public void processPendingRequests(String doctorID) {
        List<AppointmentRequest> pendingRequests = getPendingRequests();

        System.out.println("Pending Appointment Requests for Doctor ID: " + doctorID);
        boolean found = false; // Flag to check if there are any requests for the given doctorID

        for (AppointmentRequest request : pendingRequests) {
            // Check if the request's doctorID matches the provided doctorID
            if (request.getDoctorId().equals(doctorID)) {
                found = true; // Set the flag to true if a matching request is found
                System.out.println("Request ID: " + request.getRequestId() +
                                   ", Requested Date: " + request.getRequestedDate() +
                                   ", Requested Time Slot: " + request.getRequestedTimeSlot());
            }
        }

        if (!found) {
            System.out.println("No pending requests found for Doctor ID: " + doctorID);
        }
    }

    /**
     * Declines an appointment request and updates its status.
     *
     * @param request The appointment request to be declined.
     */
    public void declineRequest(AppointmentRequest request) {
        // Change request status to declined
        request.setStatus("Declined");
        saveAppointmentRequest(request); // Save the updated request to CSV
        System.out.println("Appointment request declined.");
    }

    /**
     * Saves an appointment request to the CSV file.
     *
     * @param request The appointment request to be saved.
     */
    private void saveAppointmentRequest(AppointmentRequest request) {
        try {
            // Read all existing requests
            List<AppointmentRequest> requests = getAllRequests();

            // Replace the existing request status
            for (int i = 0; i < requests.size(); i++) {
                if (requests.get(i).getRequestId().equals(request.getRequestId())) {
                    requests.set(i, request);
                    break;
                }
            }

            // Write back to CSV
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(appointmentRequestFile))) {
                // Write header
                writer.write("RequestID,DoctorID,PatientID,RequestedDate,RequestedTimeSlot,Status");
                writer.newLine();
                for (AppointmentRequest req : requests) {
                    writer.write(req.toString());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving appointment request: " + e.getMessage());
        }
    }

    /**
     * Loads all appointment requests from the CSV file.
     *
     * @return A list of all appointment requests.
     */
    private List<AppointmentRequest> getAllRequests() {
        List<AppointmentRequest> requests = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(appointmentRequestFile))) {
            String line;
            // Skip header line
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                AppointmentRequest request = AppointmentRequest.fromString(line);
                if (request != null) {
                    requests.add(request);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading appointment requests: " + e.getMessage());
        }
        return requests;
    }
}
