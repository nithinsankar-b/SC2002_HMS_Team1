package models;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MedicineConsultation {
    private String medicine;
    private double medicinePriceForTen;
    private String doctorId;
    private double consultationFeePerHalfHour;
    private static Map<String, MedicineConsultation> consultationsMap = new HashMap<>();;

    public MedicineConsultation() {
        if (consultationsMap.isEmpty()) {
            loadConsultationsFromCSV("data//Medicine_and_Consultation_Prices.csv"); // Specify the path
        }
    }
    public MedicineConsultation(String medicine, double medicinePriceForTen, String doctorId, double consultationFeePerHalfHour) {
        this.medicine = medicine;
        this.medicinePriceForTen = medicinePriceForTen;
        this.doctorId = doctorId;
        this.consultationFeePerHalfHour = consultationFeePerHalfHour;
    }

    private static void loadConsultationsFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                MedicineConsultation consultation = MedicineConsultation.fromCSV(line);
                consultationsMap.put(consultation.getMedicine(), consultation);
            }
            System.out.println("Medicine consultations loaded successfully.");
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }
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
