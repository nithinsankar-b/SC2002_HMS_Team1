package interfaces;

import models.Appointment;

public interface IBillingService {
    /**
     * Calculates the total bill for a given appointment.
     *
     * @param appointment The appointment containing the details of the consultation
     *                    and medications prescribed.
     * @return The total bill as a double.
     */
    double calculateBill(Appointment appointment);
}
