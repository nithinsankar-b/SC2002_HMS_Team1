package services;

import interfaces.IDoctorService;


import models.Doctor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorService implements IDoctorService {
    private static final String DELIMITER = ",";

    // Load doctors from a CSV file
    @Override
    public List<Doctor> loadDoctorsFromCsv(String filePath) {
        List<Doctor> doctors = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(DELIMITER);
                if (fields.length == 3) { // Assuming the CSV has 3 fields: id, name, password
                    String id = fields[0];
                    String name = fields[1];
                    String password = fields[2];
                    Doctor doctor = new Doctor(id, name, password);
                    doctors.add(doctor);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doctors;
    }

    // Save doctors to a CSV file
    @Override
    public void saveDoctorsToCsv(List<Doctor> doctors, String filePath) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (Doctor doctor : doctors) {
                String line = doctor.getHospitalID() + DELIMITER + doctor.getName() + DELIMITER + doctor.getPassword();
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
