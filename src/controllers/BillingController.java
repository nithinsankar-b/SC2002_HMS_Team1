package controllers;


import models.Billing;
import services.BillingService;

import java.util.List;

public class BillingController {
    private final BillingService billingService;

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    public List<Billing> viewUnpaidBills(String patientId) {
        return billingService.getBillingByStatus(patientId, Billing.BillingStatus.UNPAID);
    }

    public List<Billing> viewPaidBills(String patientId) {
        return billingService.getBillingByStatus(patientId, Billing.BillingStatus.PAID);
    }

    public boolean payBill(String invoiceId) {
        return billingService.markBillAsPaid(invoiceId);
    }
}
