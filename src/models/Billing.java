package models;

public class Billing {
    private String invoiceId;
    private String patientId;
    private String doctorId;
    private String appointmentId;
    private double totalAmount;
    private String status;

    public Billing() {}

    public Billing(String invoiceId, String patientId, String doctorId, String appointmentId, double totalAmount, String status) {
        this.invoiceId = invoiceId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Getters and Setters
    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        // Enclose appointmentId in double quotes to avoid issues with large numbers
        return invoiceId + "," + patientId + "," + doctorId + ",\"" + appointmentId + "\","
                + totalAmount + "," + status;
    }

    public static Billing fromCSV(String line) {
        // Split the line by comma and remove surrounding quotes from appointmentId if present
        String[] fields = line.split(",");
        
        // Handle the case where appointmentId might have quotes around it
        String appointmentId = fields[3].replace("\"", "").trim();
        
        return new Billing(fields[0], fields[1], fields[2], appointmentId, Double.parseDouble(fields[4]), fields[5]);
    }
}
