package boundary;

import interfaces.IDoctorView;

import models.AppointmentRequest;
import models.Doctor;
import interfaces.IAppointmentService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

/**
 * Allows doctors to view their upcoming appointments based on a specific date and time.
 */
public class ViewUpcomingAppointments implements IDoctorView {

    private final IAppointmentService appointmentService;

    public ViewUpcomingAppointments(IAppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Override
    public void displayDoctorView(Object obj) {
        if (!(obj instanceof Doctor)) {
            System.out.println("Invalid object passed to ViewUpcomingAppointments.");
            return;
        }

        Doctor doctor = (Doctor) obj;
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        while (true) {
            System.out.println("\n=== View Upcoming Appointments ===");
            System.out.print("Enter current date (yyyy-MM-dd) or type 'exit' to return: ");
            String dateInput = scanner.nextLine().trim();

            if (dateInput.equalsIgnoreCase("exit")) {
                return;
            }

            LocalDate currentDate;
            try {
                currentDate = LocalDate.parse(dateInput);
            } catch (Exception e) {
                System.out.println("Invalid date format. Please try again.");
                continue;
            }

            System.out.print("Enter current time (HH:mm) or type 'exit' to return: ");
            String timeInput = scanner.nextLine().trim();

            if (timeInput.equalsIgnoreCase("exit")) {
                return;
            }

            LocalTime currentTime;
            try {
                currentTime = LocalTime.parse(timeInput, timeFormatter);
            } catch (Exception e) {
                System.out.println("Invalid time format. Please try again.");
                continue;
            }

            // Fetch all upcoming appointment requests for the doctor
            List<AppointmentRequest> upcomingRequests = appointmentService.viewUpcomingAppointments(doctor.getHospitalID());

            // Filter requests based on the current date and time
            List<AppointmentRequest> filteredRequests = upcomingRequests.stream()
                    .filter(request -> {
                        if (request.getRequestedDate().isAfter(currentDate)) {
                            return true;
                        } else if (request.getRequestedDate().isEqual(currentDate)) {
                            // Extract start time from timeSlot
                            String[] times = request.getRequestedTimeSlot().split("-");
                            if (times.length != 2) return false;
                            LocalTime appointmentTime = LocalTime.parse(times[0].trim(), timeFormatter);
                            return appointmentTime.isAfter(currentTime);
                        }
                        return false;
                    })
                    .sorted((a1, a2) -> {
                        if (a1.getRequestedDate().isEqual(a2.getRequestedDate())) {
                            return a1.getRequestedTimeSlot().compareTo(a2.getRequestedTimeSlot());
                        }
                        return a1.getRequestedDate().compareTo(a2.getRequestedDate());
                    })
                    .toList();

            if (filteredRequests.isEmpty()) {
                System.out.println("No upcoming appointments after " + currentDate + " " + currentTime + ".");
            } else {
                System.out.println("\nUpcoming Appointments:");
                for (AppointmentRequest request : filteredRequests) {
                    System.out.println(request);
                }
            }

            System.out.println("\nOptions:");
            System.out.println("1. Refresh Appointments");
            System.out.println("2. Return to Doctor Menu");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    // Loop continues to refresh appointments
                    break;
                case "2":
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }
}
