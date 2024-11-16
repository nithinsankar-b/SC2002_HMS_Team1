package models;

/**
 * Represents a billing record for a patient's appointment.
 * This class includes details such as the invoice ID, patient ID, doctor ID,
 * appointment ID, total amount, and billing status.
 */
public class Billing {
    private String invoiceId;
    private String patientId;
    private String doctorId;
    private String appointmentId;
    private double totalAmount;
    private String status;

    /**
     * Default constructor for creating an empty Billing object.
     */
    public Billing() {}

    /**
     * Constructs a Billing object with all the required attributes.
     *
     * @param invoiceId The unique identifier for the invoice.
     * @param patientId The unique identifier for the patient.
     * @param doctorId The unique identifier for the doctor.
     * @param appointmentId The unique identifier for the appointment.
     * @param totalAmount The total amount to be billed.
     * @param status The status of the billing (e.g., "Paid", "Pending").
     */
    public Billing(String invoiceId, String patientId, String doctorId, String appointmentId, double totalAmount, String status) {
        this.invoiceId = invoiceId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.appointmentId = appointmentId;
        this.totalAmount = totalAmount;
        this.status = status;
    }

    // Getters and Setters

    /**
     * Gets the unique identifier for the invoice.
     *
     * @return The invoice ID.
     */
    public String getInvoiceId() {
        return invoiceId;
    }

    /**
     * Sets the unique identifier for the invoice.
     *
     * @param invoiceId The new invoice ID.
     */
    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    /**
     * Gets the unique identifier for the patient.
     *
     * @return The patient ID.
     */
    public String getPatientId() {
        return patientId;
    }

    /**
     * Sets the unique identifier for the patient.
     *
     * @param patientId The new patient ID.
     */
    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    /**
     * Gets the unique identifier for the doctor.
     *
     * @return The doctor ID.
     */
    public String getDoctorId() {
        return doctorId;
    }

    /**
     * Sets the unique identifier for the doctor.
     *
     * @param doctorId The new doctor ID.
     */
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    /**
     * Gets the unique identifier for the appointment.
     *
     * @return The appointment ID.
     */
    public String getAppointmentId() {
        return appointmentId;
    }

    /**
     * Sets the unique identifier for the appointment.
     *
     * @param appointmentId The new appointment ID.
     */
    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    /**
     * Gets the total amount to be billed.
     *
     * @return The total amount.
     */
    public double getTotalAmount() {
        return totalAmount;
    }

    /**
     * Sets the total amount to be billed.
     *
     * @param totalAmount The new total amount.
     */
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * Gets the billing status (e.g., "Paid", "Pending").
     *
     * @return The billing status.
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets the billing status.
     *
     * @param status The new billing status.
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Converts the Billing object to a CSV-formatted string.
     * The appointment ID is enclosed in quotes to prevent issues with large numeric IDs.
     *
     * @return A CSV string representation of the Billing object.
     */
    @Override
    public String toString() {
        return invoiceId + "," + patientId + "," + doctorId + ",\"" + appointmentId + "\"," +
                totalAmount + "," + status;
    }

    /**
     * Creates a Billing object from a CSV string.
     * This method parses a CSV line into the corresponding Billing attributes.
     *
     * @param line The CSV string representing a Billing record.
     * @return A Billing object created from the provided CSV string.
     */
    public static Billing fromCSV(String line) {
        // Split the line by comma and remove surrounding quotes from appointmentId if present
        String[] fields = line.split(",");

        // Handle the case where appointmentId might have quotes around it
        String appointmentId = fields[3].replace("\"", "").trim();

        return new Billing(fields[0], fields[1], fields[2], appointmentId, Double.parseDouble(fields[4]), fields[5]);
    }
}

