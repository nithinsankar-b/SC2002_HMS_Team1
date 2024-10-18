package stores;

import models.MedicalRecord;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MedicalRecordDataStore {

    private String medicalRecordFilePath;
    private List<MedicalRecord> medicalRecords;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public MedicalRecordDataStore(String medicalRecordFilePath) {
        this.medicalRecordFilePath = medicalRecordFilePath;
        this.medicalRecords = loadMedicalRecords();
    }

    /**
     * Loads medical records from the CSV file.
     *
     * @return A list of MedicalRecord objects.
     */
    private List<MedicalRecord> loadMedicalRecords() {
        List<MedicalRecord> loadedRecords = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(medicalRecordFilePath))) {
            String header = br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",", -1); // -1 to include trailing empty strings
                if (fields.length == 6) {
                    String patientId = fields[0].trim();
                    String doctorId = fields[1].trim();
                    String diagnosis = fields[2].trim();
                    String prescriptions = fields[3].trim();
                    String treatmentPlan = fields[4].trim();
                    LocalDate lastUpdated = LocalDate.parse(fields[5].trim(), DATE_FORMAT);

                    MedicalRecord record = new MedicalRecord(patientId, doctorId, diagnosis,
                            prescriptions, treatmentPlan, lastUpdated);
                    loadedRecords.add(record);
                } else {
                    System.err.println("Invalid medical record entry: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading medical records: " + e.getMessage());
        }
        return loadedRecords;
    }

    /**
     * Retrieves medical records for a specific doctor.
     *
     * @param doctorId The ID of the doctor.
     * @return A list of MedicalRecord objects.
     */
    public List<MedicalRecord> getRecordsForDoctor(String doctorId) {
        return medicalRecords.stream()
                .filter(record -> record.getDoctorId().equalsIgnoreCase(doctorId))
                .collect(Collectors.toList());
    }

    /**
     * Updates a specific field in a patient's medical record and persists changes to the CSV.
     *
     * @param patientId The ID of the patient.
     * @param field     The field to update ("diagnosis", "prescriptions", "treatmentPlan").
     * @param newValue  The new value for the field.
     */
    public void updateMedicalRecord(String patientId, String field, String newValue) {
        for (int i = 0; i < medicalRecords.size(); i++) {
            MedicalRecord record = medicalRecords.get(i);
            if (record.getPatientId().equalsIgnoreCase(patientId)) {
                switch (field.toLowerCase()) {
                    case "diagnosis":
                        record.setDiagnosis(newValue);
                        break;
                    case "prescriptions":
                        record.setPrescriptions(newValue);
                        break;
                    case "treatmentplan":
                        record.setTreatmentPlan(newValue);
                        break;
                    default:
                        System.out.println("Invalid field provided.");
                        return;
                }
                medicalRecords.set(i, record);
                saveMedicalRecords();
                return;
            }
        }
        System.out.println("Medical record not found for Patient ID: " + patientId);
    }

    /**
     * Saves all medical records back to the CSV file.
     */
    private void saveMedicalRecords() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(medicalRecordFilePath))) {
            // Write header
            bw.write("PatientID,DoctorID,Diagnosis,Prescriptions,TreatmentPlan,LastUpdated");
            bw.newLine();
            for (MedicalRecord record : medicalRecords) {
                String line = String.join(",",
                        record.getPatientId(),
                        record.getDoctorId(),
                        record.getDiagnosis(),
                        record.getPrescriptions(),
                        record.getTreatmentPlan(),
                        record.getLastUpdated().format(DATE_FORMAT));
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving medical records: " + e.getMessage());
        }
    }
}
