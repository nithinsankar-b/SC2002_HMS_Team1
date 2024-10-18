package services;

import interfaces.IPharmacistService;
import models.Pharmacist;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class csvPharmacistService implements IPharmacistService {
    private static final String DELIMITER = ",";

    // Load pharmacists from a CSV file
    @Override
    public List<Pharmacist> loadPharmacistsFromCsv(String filePath) {
        List<Pharmacist> pharmacists = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(DELIMITER);
                if (fields.length == 3) { // Assuming the CSV has 3 fields: id, name, contactNumber
                    String id = fields[0];
                    String name = fields[1];
                    String contactNumber = fields[2];
                    Pharmacist pharmacist = new Pharmacist(id, name, contactNumber);
                    pharmacists.add(pharmacist);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pharmacists;
    }

    // Save pharmacists to a CSV file
    @Override
    public void savePharmacistsToCsv(List<Pharmacist> pharmacists, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Pharmacist pharmacist : pharmacists) {
                String line = pharmacist.getHospitalID() + DELIMITER + pharmacist.getName() + DELIMITER + pharmacist.getPassword();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
