package models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents a combination of medicine and consultation pricing information.
 * Includes details like medicine name, price for ten units, doctor ID, and consultation fee per half hour.
 * The data can be loaded from a CSV file into a map for easy access.
 */
public class MedicineConsultation {
    private String medicine;
    private double medicinePriceForTen;
    private String doctorId;
    private double consultationFeePerHalfHour;
    private static Map<String, MedicineConsultation> consultationsMap = new HashMap<>();

    /**
     * Default constructor. Initializes the consultations map by loading data from a CSV file
     * if the map is empty.
     */
    public MedicineConsultation() {
        if (consultationsMap.isEmpty()) {
            loadConsultationsFromCSV("data//Medicine_and_Consultation_Prices.csv"); // Specify the path
        }
    }

    /**
     * Parameterized constructor to create a MedicineConsultation object.
     *
     * @param medicine                  The name of the medicine.
     * @param medicinePriceForTen       The price of ten units of the medicine.
     * @param doctorId                  The ID of the doctor associated with the consultation.
     * @param consultationFeePerHalfHour The consultation fee for a half-hour session.
     */
    public MedicineConsultation(String medicine, double medicinePriceForTen, String doctorId, double consultationFeePerHalfHour) {
        this.medicine = medicine;
        this.medicinePriceForTen = medicinePriceForTen;
        this.doctorId = doctorId;
        this.consultationFeePerHalfHour = consultationFeePerHalfHour;
    }

    /**
     * Loads medicine and consultation data from a CSV file and populates the consultations map.
     *
     * @param filePath The path to the CSV file containing the data.
     */
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

    /**
     * Gets the name of the medicine.
     *
     * @return The medicine name.
     */
    public String getMedicine() {
        return medicine;
    }

    /**
     * Sets the name of the medicine.
     *
     * @param medicine The medicine name to set.
     */
    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    /**
     * Gets the price for ten units of the medicine.
     *
     * @return The price for ten units of the medicine.
     */
    public double getMedicinePriceForTen() {
        return medicinePriceForTen;
    }

    /**
     * Sets the price for ten units of the medicine.
     *
     * @param medicinePriceForTen The price to set for ten units of the medicine.
     */
    public void setMedicinePriceForTen(double medicinePriceForTen) {
        this.medicinePriceForTen = medicinePriceForTen;
    }

    /**
     * Gets the ID of the doctor associated with the consultation.
     *
     * @return The doctor ID.
     */
    public String getDoctorId() {
        return doctorId;
    }

    /**
     * Sets the ID of the doctor associated with the consultation.
     *
     * @param doctorId The doctor ID to set.
     */
    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    /**
     * Gets the consultation fee for a half-hour session.
     *
     * @return The consultation fee.
     */
    public double getConsultationFee() {
        return consultationFeePerHalfHour;
    }

    /**
     * Sets the consultation fee for a half-hour session.
     *
     * @param consultationFeePerHalfHour The consultation fee to set.
     */
    public void setConsultationFee(double consultationFeePerHalfHour) {
        this.consultationFeePerHalfHour = consultationFeePerHalfHour;
    }

    /**
     * Converts the MedicineConsultation object to a CSV-formatted string.
     *
     * @return A CSV string representation of the object.
     */
    @Override
    public String toString() {
        return medicine + "," + medicinePriceForTen + "," + doctorId + "," + consultationFeePerHalfHour;
    }

    /**
     * Parses a CSV string to create a MedicineConsultation object.
     *
     * @param line A CSV-formatted string representing a medicine consultation.
     * @return A MedicineConsultation object created from the provided CSV string.
     */
    public static MedicineConsultation fromCSV(String line) {
        String[] fields = line.split(",");
        return new MedicineConsultation(fields[0], Double.parseDouble(fields[1]), fields[2], Double.parseDouble(fields[3]));
    }
}
