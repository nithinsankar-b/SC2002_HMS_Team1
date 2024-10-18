package boundary;

import interfaces.IDoctorView;
import models.AppointmentRequest;
import models.Doctor;
import interfaces.IAppointmentService;

import java.util.List;
import java.util.Scanner;

public class ViewAppointmentRequest implements IDoctorView {

    private final IAppointmentService appointmentService;

    public ViewAppointmentRequest(IAppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Override
    public void displayDoctorView(Object obj) {
        if (!(obj instanceof Doctor)) {
            System.out.println("Invalid object passed to ViewAppointmentRequests.");
            return;
        }

        Doctor doctor = (Doctor) obj;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Appointment Requests ===");

            // Fetch pending requests for current week
            List<AppointmentRequest> requests = appointmentService.getPendingRequestsForDoctor(doctor.getHospitalID());

            if (requests.isEmpty()) {
                System.out.println("No pending appointment requests for the current week.");
            } else {
                for (AppointmentRequest request : requests) {
                    System.out.println(request);
                }
            }

            System.out.println("\nOptions:");
            System.out.println("1. Accept Appointment Request");
            System.out.println("2. Decline Appointment Request");
            System.out.println("3. Return to Doctor Menu");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    manageAppointmentRequest(scanner, "accept");
                    break;
                case "2":
                    manageAppointmentRequest(scanner, "decline");
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    /**
     * Manages accepting or declining an appointment request based on user input.
     *
     * @param scanner The Scanner object for user input.
     * @param action  The action to perform ("accept" or "decline").
     */
    private void manageAppointmentRequest(Scanner scanner, String action) {
        System.out.print("Enter Request ID to " + action + " (or type 'exit' to cancel): ");
        String requestId = scanner.nextLine().trim();

        if (requestId.equalsIgnoreCase("exit")) {
            System.out.println("Action canceled.");
            return;
        }

        if (action.equalsIgnoreCase("accept")) {
            appointmentService.acceptAppointmentRequest(requestId);
        } else if (action.equalsIgnoreCase("decline")) {
            appointmentService.declineAppointmentRequest(requestId);
        }

        // After action, display the updated list of pending requests
        System.out.println("\nUpdated List of Pending Appointment Requests:");
    }
}
