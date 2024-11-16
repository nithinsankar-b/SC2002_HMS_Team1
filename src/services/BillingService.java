package services;

import models.Appointment;

/**
 * Service class for calculating bills for appointments, including charges for consultations and medications.
 * <p>
 * The `BillingService` provides a method to calculate the total cost of an appointment based on the 
 * consultation fee and medication costs. It uses fixed charges for each medication type.
 * </p>
 */
public class BillingService {

    /** The fixed charge for a consultation. */
    public static final double CONSULTATION_CHARGE = 100.0;

    /** The fixed charge per unit of Paracetamol. */
    public static final double PARACETAMOL_CHARGE = 10.0;

    /** The fixed charge per unit of Ibuprofen. */
    public static final double IBUPROFEN_CHARGE = 20.0;

    /** The fixed charge per unit of Amoxicillin. */
    public static final double AMOXICILLIN_CHARGE = 15.0;

    /**
     * Calculates the total bill for an appointment, including the consultation charge and medication costs.
     * <p>
     * This method calculates the total bill by adding the consultation fee to the charges for each medication
     * prescribed in the appointment. The medication charges are determined based on the type and quantity of 
     * medications listed in the appointment.
     * </p>
     *
     * @param appointment The appointment for which the bill is calculated.
     *                    This includes details about the medications prescribed and their quantities.
     * @return The total calculated bill amount as a double.
     */
    public double calculateBill(Appointment appointment) {
        double totalBill = CONSULTATION_CHARGE; // Start with the consultation charge

        // Iterate through the medications and add charges based on their names and quantities
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


