package stores;

import models.AppointmentRequest;

import java.io.*;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AppointmentRequestDataStore {

    private String appointmentRequestFilePath;
    private List<AppointmentRequest> appointmentRequests;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public AppointmentRequestDataStore(String appointmentRequestFilePath) {
        this.appointmentRequestFilePath = appointmentRequestFilePath;
        this.appointmentRequests = loadAppointmentRequests();
    }

    /**
     * Loads appointment requests from the CSV file.
     *
     * @return A list of AppointmentRequest objects.
     */
    private List<AppointmentRequest> loadAppointmentRequests() {
        List<AppointmentRequest> loadedRequests = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(appointmentRequestFilePath))) {
            String header = br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",", -1); // -1 to include trailing empty strings
                if (fields.length == 6) {
                    String requestId = fields[0].trim();
                    String doctorId = fields[1].trim();
                    String patientId = fields[2].trim();
                    LocalDate requestedDate = LocalDate.parse(fields[3].trim(), DATE_FORMAT);
                    String requestedTimeSlot = fields[4].trim();
                    String status = fields[5].trim();

                    AppointmentRequest request = new AppointmentRequest(requestId, doctorId, patientId,
                            requestedDate, requestedTimeSlot, status);
                    loadedRequests.add(request);
                } else {
                    System.err.println("Invalid appointment request entry: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading appointment requests: " + e.getMessage());
        }
        return loadedRequests;
    }

    /**
     * Retrieves pending appointment requests for a doctor within the current week.
     *
     * @param doctorId The ID of the doctor.
     * @return A list of pending AppointmentRequest objects for the current week.
     */
    public List<AppointmentRequest> getPendingRequestsForDoctorCurrentWeek(String doctorId) {
        LocalDate today = LocalDate.now();
        LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
        LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

        return appointmentRequests.stream()
                .filter(request -> request.getDoctorId().equalsIgnoreCase(doctorId))
                .filter(request -> request.getStatus().equalsIgnoreCase("Pending"))
                .filter(request -> !request.getRequestedDate().isBefore(startOfWeek) &&
                                   !request.getRequestedDate().isAfter(endOfWeek))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves an appointment request by its ID.
     *
     * @param requestId The ID of the appointment request.
     * @return The AppointmentRequest object if found; otherwise, null.
     */
    public AppointmentRequest getAppointmentRequestById(String requestId) {
        return appointmentRequests.stream()
                .filter(request -> request.getRequestId().equalsIgnoreCase(requestId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Updates an existing appointment request and persists changes to the CSV.
     *
     * @param updatedRequest The updated AppointmentRequest object.
     */
    public void updateAppointmentRequest(AppointmentRequest updatedRequest) {
        for (int i = 0; i < appointmentRequests.size(); i++) {
            AppointmentRequest request = appointmentRequests.get(i);
            if (request.getRequestId().equalsIgnoreCase(updatedRequest.getRequestId())) {
                appointmentRequests.set(i, updatedRequest);
                saveAppointmentRequests();
                return;
            }
        }
        System.out.println("Appointment request not found for update.");
    }

    /**
     * Saves all appointment requests back to the CSV file.
     */
    private void saveAppointmentRequests() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(appointmentRequestFilePath))) {
            // Write header
            bw.write("RequestID,DoctorID,PatientID,RequestedDate,RequestedTimeSlot,Status");
            bw.newLine();
            for (AppointmentRequest request : appointmentRequests) {
                String line = String.join(",",
                        request.getRequestId(),
                        request.getDoctorId(),
                        request.getPatientId(),
                        request.getRequestedDate().format(DATE_FORMAT),
                        request.getRequestedTimeSlot(),
                        request.getStatus());
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving appointment requests: " + e.getMessage());
        }
    }
    /**
     * Retrieves all upcoming appointments for a doctor within the next week.
     *
     * @param doctorId The ID of the doctor.
     * @return A list of AppointmentRequest objects for the upcoming week.
     */
    public List<AppointmentRequest> getUpcomingAppointmentsForDoctor(String doctorId) {
        LocalDate today = LocalDate.now();
        LocalDate startOfNextWeek = today.plusWeeks(1).with(DayOfWeek.MONDAY);
        LocalDate endOfNextWeek = today.plusWeeks(1).with(DayOfWeek.SUNDAY);

        return appointmentRequests.stream()
                .filter(request -> request.getDoctorId().equalsIgnoreCase(doctorId))
                .filter(request -> !request.getRequestedDate().isBefore(startOfNextWeek) &&
                                   !request.getRequestedDate().isAfter(endOfNextWeek))
                .collect(Collectors.toList());
    }

}
