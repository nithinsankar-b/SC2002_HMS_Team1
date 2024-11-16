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
import models.Pharmacist;
import enums.MedicationStatus;

import services.UserService;
import services.AppointmentService;
import services.InventoryService;

public class PharmacistService {
    
    private final Map<String, Pharmacist> pharmacists;
    private final UserService userService;
    private final AppointmentService appointmentService;
    private final InventoryService inventoryService;
    private static final String CSV_FILE_PATH = "data/pharmacist.csv";
    private static final String DELIMITER = ",";

    /**
     * Constructs a {@code PharmacistService} with the specified user, appointment, and inventory services.
     *
     * @param userService the user service instance for retrieving user details
     * @param appointmentService the appointment service instance for managing appointments
     * @param inventoryService the inventory service instance for managing medication stock
     */
    public PharmacistService(UserService userService, AppointmentService appointmentService, InventoryService inventoryService) {
        this.userService = userService;
        this.appointmentService = appointmentService;
        this.inventoryService = inventoryService;
        this.pharmacists = new HashMap<>();
        loadPharmacistsFromCSV();
    }

    /**
     * Adds a new pharmacist to the system.
     *
     * @param pharmacist the pharmacist to add
     */
    public void addPharmacist(Pharmacist pharmacist) {
        if (!pharmacists.containsKey(pharmacist.getHospitalID())) {
            pharmacists.put(pharmacist.getHospitalID(), pharmacist);
            savePharmacistsToCSV();
            System.out.println("Pharmacist added successfully.");
        } else {
            System.out.println("Pharmacist already exists.");
        }
    }

    /**
     * Retrieves a pharmacist by their hospital ID.
     *
     * @param hospitalID the hospital ID of the pharmacist
     * @return the pharmacist with the specified hospital ID, or {@code null} if not found
     */
    public Pharmacist getPharmacistById(String hospitalID) {
        return pharmacists.get(hospitalID);
    }

    /**
     * Updates the contact information of a pharmacist.
     *
     * @param hospitalID the hospital ID of the pharmacist
     * @param newContactInformation the new contact information to set
     * @return {@code true} if the update was successful, {@code false} otherwise
     */
    public boolean updatePharmacistContact(String hospitalID, String newContactInformation) {
        Pharmacist pharmacist = pharmacists.get(hospitalID);
        if (pharmacist != null) {
            pharmacist.setContactInformation(newContactInformation);
            savePharmacistsToCSV();
            return true;
        }
        return false;
    }

    /**
     * Lists all pharmacists in the system.
     */
    public void listAllPharmacists() {
        pharmacists.values().forEach(pharmacist -> {
            System.out.println("Pharmacist ID: " + pharmacist.getHospitalID());
            System.out.println("Name: " + pharmacist.getName());
            System.out.println("Contact Information: " + pharmacist.getContactInformation());
            System.out.println("------------------------");
        });
    }

    /**
     * Loads pharmacists from a CSV file.
     */
    public void loadPharmacistsFromCSV() {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            br.readLine(); // Skip the header line
            while ((line = br.readLine()) != null) {
                String[] pharmacistData = line.split(DELIMITER);
                if (pharmacistData.length == 3) {
                    String pharmacistId = pharmacistData[0].trim();
                    String name = pharmacistData[1].trim();
                    String contactInformation = pharmacistData[2].trim();
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

    /**
     * Saves the current list of pharmacists to a CSV file.
     */
    public void savePharmacistsToCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            bw.write("Pharmacist ID,Name,Contact Information");
            bw.newLine();
            for (Pharmacist pharmacist : pharmacists.values()) {
                String line = String.join(DELIMITER,
                        pharmacist.getHospitalID(),
                        pharmacist.getName(),
                        pharmacist.getContactInformation());
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving the pharmacists CSV file: " + e.getMessage());
        }
    }

    /**
     * Retrieves appointments for a given patient ID.
     *
     * @param patientId the ID of the patient
     * @return a list of appointments for the specified patient
     */
    private List<Appointment> getAppointmentsByPatientId(String patientId) {
        List<Appointment> allAppointments = appointmentService.viewScheduledAppointments();
        List<Appointment> patientAppointments = new ArrayList<>();
        for (Appointment appointment : allAppointments) {
            if (appointment.getPatientId().equals(patientId)) {
                patientAppointments.add(appointment);
            }
        }
        return patientAppointments;
    }

    /**
     * Updates the prescription status for a given appointment and updates inventory accordingly.
     *
     * @param appointmentID the ID of the appointment to update
     * @return {@code true} if the update was successful, {@code false} otherwise
     */
    public boolean updatePrescriptionStatus(String appointmentID) {
        Appointment appointment = appointmentService.getAppointmentById(appointmentID);

        if (appointment != null) {
            // Update the medication status in the appointment to DISPENSED
            //appointmentService.updateMedicationStatus(appointmentID, MedicationStatus.DISPENSED);

            // Get medications and quantities from the appointment
            List<Medication> medications = appointment.getMedications();
            List<Integer> quantities = appointment.getQuantities();

            // Ensure that the number of medications and quantities match
            if (medications.size() == quantities.size()) {
                // Update inventory stock for each medication and quantity
                for (int i = 0; i < medications.size(); i++) {
                    Medication medication = medications.get(i);
                    int quantity = quantities.get(i); // Get the corresponding quantity

                    // Update the stock for the medication
                    inventoryService.updateStock(medication.getName(), quantity, appointmentID);  // Assuming `getName` method for Medication class
                }
                // Set the medication status to DISPENSED
                appointment.setMedicationStatus(MedicationStatus.DISPENSED); // Assuming `setMedicationStatus` method in Medication class

                return true;
            } else {
                System.out.println("Error: Medications and quantities lists do not match.");
            }
        }

        return false;
    }







}

