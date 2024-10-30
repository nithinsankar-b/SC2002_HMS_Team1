package services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import enums.UserRole;
import models.Pharmacist;
import models.User;
import models.Appointment;
import models.Medication;
import models.Patient;
import enums.MedicationStatus;

public class PharmacistService {
    private final Map<String, Pharmacist> pharmacists;
    private final UserService userService; // Instance of UserService
    private final AppointmentService appointmentService; // Instance of AppointmentService
    private final InventoryService inventoryService; // Instance of InventoryService
    private static final String CSV_FILE_PATH = "data/pharmacist.csv"; // Path to the CSV file
    private static final String DELIMITER = ",";

    // Constructor to initialize services and pharmacists map
    public PharmacistService(UserService userService, AppointmentService appointmentService, InventoryService inventoryService) {
        this.userService = userService; // UserService will provide user details
        this.appointmentService = appointmentService; // Initialize AppointmentService
        this.inventoryService = inventoryService; // Initialize InventoryService
        this.pharmacists = new HashMap<>();
        loadPharmacistsFromCSV(); // Load pharmacists from the CSV file
    }

  // Method to add a new pharmacist
    public void addPharmacist(Pharmacist pharmacist) {
        if (!pharmacists.containsKey(pharmacist.getHospitalID())) {
            pharmacists.put(pharmacist.getHospitalID(), pharmacist);
            savePharmacistsToCSV(); // Save the updated pharmacist list to CSV
            System.out.println("Pharmacist added successfully.");
        } else {
            System.out.println("Pharmacist already exists.");
        }
    }

    // Method to retrieve a pharmacist by their hospital ID
    public Pharmacist getPharmacistById(String hospitalID) {
        return pharmacists.get(hospitalID);
    }

    // Method to update pharmacist contact information
    public boolean updatePharmacistContact(String hospitalID, String newContactInformation) {
        Pharmacist pharmacist = pharmacists.get(hospitalID);
        if (pharmacist != null) {
            pharmacist.setContactInformation(newContactInformation);
            savePharmacistsToCSV(); // Save changes after update
            return true;
        }
        return false;
    }

    // Method to list all pharmacists
    public void listAllPharmacists() {
        pharmacists.values().forEach(pharmacist -> {
            System.out.println("Pharmacist ID: " + pharmacist.getHospitalID());
            System.out.println("Name: " + pharmacist.getName());
            System.out.println("Contact Information: " + pharmacist.getContactInformation());
            System.out.println("------------------------");
        });
    }

    // Method to load pharmacists from a CSV file
    public void loadPharmacistsFromCSV() {
        String line;

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            // Skip the header line
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] pharmacistData = line.split(DELIMITER);

                // Assuming the CSV has 3 fields: ID, Name, Contact Information
                if (pharmacistData.length == 3) {
                    String pharmacistId = pharmacistData[0].trim();
                    String name = pharmacistData[1].trim();
                    String contactInformation = pharmacistData[2].trim();
                    
                    // Try to find an existing user if applicable
                    User existingUser = userService.getUserById(pharmacistId);
                    String password = (existingUser != null) ? existingUser.getPassword() : "defaultPassword";

                    // Create a new Patient object and add it to the collection
                    pharmacists.put(pharmacistId, new Pharmacist(existingUser != null ? existingUser : new User(pharmacistId, password, UserRole.PATIENT), name, contactInformation));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading pharmacists CSV file: " + e.getMessage());
        }
    }

    // Method to save pharmacists back to the CSV file
    public void savePharmacistsToCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            bw.write("Pharmacist ID,Name,Contact Information");
            bw.newLine();
            for (Pharmacist pharmacist : pharmacists.values()) {
                String line = String.join(DELIMITER,
                        pharmacist.getHospitalID(),
                        pharmacist.getName(),
                        pharmacist.getContactInformation()); // Include contact information
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving the pharmacists CSV file: " + e.getMessage());
        }
    }

    // Method to retrieve appointments by patient ID
    private List<Appointment> getAppointmentsByPatientId(String patientId) {
        List<Appointment> allAppointments = appointmentService.viewScheduledAppointments(); // Retrieve all appointments
        List<Appointment> patientAppointments = new ArrayList<>();

        for (Appointment appointment : allAppointments) {
            if (appointment.getPatientId().equals(patientId)) {
                patientAppointments.add(appointment);
            }
        }
        return patientAppointments;
    }

 // Method to update prescription status and inventory
    public boolean updatePrescriptionStatus(String appointmentID) {
        Appointment appointment = appointmentService.getAppointmentById(appointmentID);
        System.out.println(appointment);// Assuming a method exists to get Appointment by ID
        if (appointment != null) {
            // Update the medication status in the appointment
            appointmentService.updateMedicationStatus(appointmentID);
            
            // Get medication and quantity from appointment
            String medication = appointment.getMedication();
            int quantity = appointment.getQuantity();

            // Update inventory stock
            inventoryService.updateStock(medication, quantity);

            return true;
        }
        return false;
    }


    

    
    }

