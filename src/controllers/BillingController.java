package controllers;

import models.Appointment;
import services.BillingService;
import enums.AppointmentStatus;
import models.PaymentLogger;
import java.util.List;

/**
 * The BillingController class handles the billing and payment processes
 * for appointments. It calculates bills, processes payments, and fetches
 * pending payments for completed appointments.
 */
public class BillingController {

    private final BillingService billingService;

    /**
     * Constructor for BillingController.
     * Initializes the BillingService.
     */
    public BillingController() {
        this.billingService = new BillingService();
    }

    /**
     * Calculates the bill for a completed appointment.
     *
     * @param appointmentId The ID of the appointment for which the bill is to be calculated.
     * @return A formatted string containing the bill details, or an error message if the appointment is invalid.
     */
    public String calculateBill(String appointmentId) {
        Appointment appointment = Appointment.getAppointmentById(appointmentId); // Assume Appointment has a static lookup
        if (appointment == null) {
            return "No appointment found with ID: " + appointmentId;
        }

        if (!appointment.getStatus().equals(AppointmentStatus.COMPLETED)) {
            return "Billing is only available for completed appointments.";
        }

        double billAmount = billingService.calculateBill(appointment);

        // Format and return the bill
        StringBuilder billDetails = new StringBuilder();
        billDetails.append("=== BILL DETAILS ===\n");
        billDetails.append("Appointment ID: ").append(appointmentId).append("\n");
        billDetails.append("Service Provided: ").append(appointment.getServiceProvided()).append("\n");
        billDetails.append("Consultation Charge: $").append(BillingService.CONSULTATION_CHARGE).append("\n");

        for (int i = 0; i < appointment.getMedications().size(); i++) {
            String medicationName = appointment.getMedications().get(i).getName();
            int quantity = appointment.getQuantities().get(i);
            double cost = switch (medicationName.toLowerCase()) {
                case "paracetamol" -> BillingService.PARACETAMOL_CHARGE;
                case "ibuprofen" -> BillingService.IBUPROFEN_CHARGE;
                case "amoxicillin" -> BillingService.AMOXICILLIN_CHARGE;
                default -> 0.0;
            };
            billDetails.append(String.format("%s x%d: $%.2f\n", medicationName, quantity, cost * quantity));
        }

        billDetails.append("Total Bill: $").append(billAmount).append("\n");
        billDetails.append("=====================\n");
        return billDetails.toString();
    }

    /**
     * Processes the payment for a completed appointment.
     *
     * @param appointmentId The ID of the appointment for which the payment is to be processed.
     * @return True if the payment was successfully logged, false otherwise.
     */
    public boolean payBill(String appointmentId) {
        Appointment appointment = Appointment.getAppointmentById(appointmentId);
        if (appointment == null || !appointment.getStatus().equals(AppointmentStatus.COMPLETED)) {
            System.out.println("Payment cannot be processed for Appointment ID: " + appointmentId);
            return false;
        }

        double billAmount = billingService.calculateBill(appointment);

        // Log the payment
        PaymentLogger.logPayment(appointmentId, billAmount, appointment.getServiceProvided());

        System.out.println("Payment successfully logged for Appointment ID: " + appointmentId);
        return true;
    }

    /**
     * Retrieves the list of completed appointments that have not been paid.
     *
     * @param allAppointments A list of all appointments.
     * @return A list of appointments with pending payments.
     */
    public List<Appointment> getPendingPayments(List<Appointment> allAppointments) {
        List<String> completedPaymentIds = PaymentLogger.getLoggedPayments()
                .stream()
                .map(log -> log.split(",")[0]) // Extract appointment IDs
                .toList();

        return allAppointments.stream()
                .filter(app -> app.getStatus().equals(AppointmentStatus.COMPLETED))
                .filter(app -> !completedPaymentIds.contains(app.getAppointmentId())) // Exclude completed ones
                .toList();
    }
}

