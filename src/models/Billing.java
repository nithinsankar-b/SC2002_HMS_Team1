package models;

import java.util.Objects;

public class Billing {
    public enum BillingStatus { PAID, UNPAID }

    private String invoiceId;
    private String patientId;
    private String doctorId;
    private String appointmentId;
    private double totalAmount;
    private BillingStatus status;

    public Billing(String invoiceId, String patientId, String doctorId, String appointmentId, double totalAmount, BillingStatus status) {
        this.invoiceId = invoiceId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Getters and setters
    public String getInvoiceId() { return invoiceId; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public String getAppointmentId() { return appointmentId; }
    public double getTotalAmount() { return totalAmount; }
    public BillingStatus getStatus() { return status; }
    public void setStatus(BillingStatus status) { this.status = status; }

    // Convert to CSV format
    public String toCsvString() {
        return invoiceId + "," + patientId + "," + doctorId + "," + appointmentId + "," + totalAmount + "," + status;
    }

    // Create Billing object from CSV format
    public static Billing fromCsvString(String csvLine) {
        String[] parts = csvLine.split(",");
        return new Billing(parts[0], parts[1], parts[2], parts[3], Double.parseDouble(parts[4]), BillingStatus.valueOf(parts[5]));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Billing)) return false;
        Billing billing = (Billing) o;
        return Objects.equals(invoiceId, billing.invoiceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(invoiceId);
    }
}