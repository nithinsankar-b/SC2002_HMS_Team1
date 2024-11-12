// BillingController.java
package controllers;

import services.BillingService;

public class BillingController {
    private final BillingService billingService;

    public BillingController() {
        billingService = new BillingService();
    }

    public double calculateBill(String appointmentId) {
        return billingService.calculateTotalBill(appointmentId);
    }

    public boolean payBill(String appointmentId) {
        return billingService.payBill(appointmentId);
    }
}
