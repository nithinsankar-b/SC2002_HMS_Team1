package models;

public class MedicineConsultation {
    private String medicine;
    private double medicinePriceForTen;
    private String doctorId;
    private double consultationFeePerHalfHour;

    public MedicineConsultation() {}
    public MedicineConsultation(String medicine, double medicinePriceForTen, String doctorId, double consultationFeePerHalfHour) {
        this.medicine = medicine;
        this.medicinePriceForTen = medicinePriceForTen;
        this.doctorId = doctorId;
        this.consultationFeePerHalfHour = consultationFeePerHalfHour;
    }

    // Getters and Setters
    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public double getMedicinePriceForTen() {
        return medicinePriceForTen;
    }

    public void setMedicinePriceForTen(double medicinePriceForTen) {
        this.medicinePriceForTen = medicinePriceForTen;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public double getConsultationFee() {
        return consultationFeePerHalfHour;
    }

    public void setConsultationFee(double consultationFeePerHalfHour) {
        this.consultationFeePerHalfHour = consultationFeePerHalfHour;
    }

    @Override
    public String toString() {
        return medicine + "," + medicinePriceForTen + "," + doctorId + "," + consultationFeePerHalfHour;
    }

    public static MedicineConsultation fromCSV(String line) {
        String[] fields = line.split(",");
        return new MedicineConsultation(fields[0], Double.parseDouble(fields[1]), fields[2], Double.parseDouble(fields[3]));
    }
}
