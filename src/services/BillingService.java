package services;

import models.Appointment;
import interfaces.IBillingService;

public class BillingService implements IBillingService {
    public static final double CONSULTATION_CHARGE = 100.0;
    public static final double PARACETAMOL_CHARGE = 10.0;
    public static final double IBUPROFEN_CHARGE = 20.0;
    public static final double AMOXICILLIN_CHARGE = 15.0;

    @Override
    public double calculateBill(Appointment appointment) {
        double totalBill = CONSULTATION_CHARGE; // Start with consultation charge

        for (int i = 0; i < appointment.getMedications().size(); i++) {
            String medicationName = appointment.getMedications().get(i).getName();
            int quantity = appointment.getQuantities().get(i);

            // Add medication charges based on the medication name
            switch (medicationName.toLowerCase()) {
                case "paracetamol" -> totalBill += PARACETAMOL_CHARGE * quantity;
                case "ibuprofen" -> totalBill += IBUPROFEN_CHARGE * quantity;
                case "amoxicillin" -> totalBill += AMOXICILLIN_CHARGE * quantity;
                default -> System.out.println("Unknown medication: " + medicationName + ". Skipping charge.");
            }
        }

        return totalBill;
    }
}
