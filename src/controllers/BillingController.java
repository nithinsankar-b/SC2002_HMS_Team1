package controllers;

import models.Appointment;
import services.BillingService;
import enums.AppointmentStatus;
import models.PaymentLogger;
import java.util.List;
public class BillingController {
    private final BillingService billingService;

    public BillingController() {
        this.billingService = new BillingService();
    }

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
