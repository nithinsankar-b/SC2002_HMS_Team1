package services;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.stream.Collectors;

import enums.AppointmentStatus;
import enums.MedicationStatus;
import enums.UserRole;
import interfaces.IDoctorService;
import models.Doctor;
import models.Patient;
import models.Pharmacist;
import models.User;
import models.MedicalRecord;
import models.Medication;
import models.Appointment;
import models.AppointmentRequest;
import services.AppointmentRequestService;
import services.ScheduleService;
import services.MedicalRecordService;
import services.DoctorService;
import services.AppointmentService;
import services.UserService;
import models.Inventory;
import stores.InventoryDataStore;


/**
 * Service class for managing doctors, including operations for loading, saving,
 * updating doctor information, and handling appointment requests.
 */
public class DoctorService implements IDoctorService {

    private static final String CSV_FILE_PATH = "data/doctor.csv";
    private static final String DELIMITER = ",";
    private Map<String, Doctor> doctors;
    private final UserService userService;
    private final AppointmentRequestService appointmentRequestService;
    private final ScheduleService scheduleService;
    private final MedicalRecordService medicalRecordService;
    private final AppointmentService appointmentService;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Constructs a DoctorService instance with the specified services.
     *
     * @param userService the user service for managing user-related operations
     * @param scheduleService the schedule service for managing doctor availability
     * @param medicalRecordService the medical record service for managing patient records
     * @param appointmentService the appointment service for managing appointments
     */
    public DoctorService(UserService userService, ScheduleService scheduleService, 
                         MedicalRecordService medicalRecordService, AppointmentService appointmentService) {
        this.userService = userService;
        this.doctors = new HashMap<>();
        this.appointmentService = appointmentService;
        this.medicalRecordService = medicalRecordService;
        this.scheduleService = scheduleService;
        this.appointmentRequestService = new AppointmentRequestService(scheduleService, appointmentService);
        loadDoctorFromCSV();
    }
    //add doctor function added.
    public void addDoctor(Doctor doctor) {
        if (!doctors.containsKey(doctor.getHospitalID())) {
            doctors.put(doctor.getHospitalID(), doctor);
            saveDoctorsToCSV();
            System.out.println("Doctor added successfully.");
        } else {
            System.out.println("Doctor already exists.");
        }
    }

    /**
     * Retrieves a doctor by their hospital ID.
     *
     * @param hospitalID the ID of the doctor to retrieve
     * @return the Doctor object associated with the given ID, or null if not found
     */
    public Doctor getDoctorById(String hospitalID) {
        return doctors.get(hospitalID);
    }

    /**
     * Updates the contact information for a doctor identified by their hospital ID.
     *
     * @param hospitalID the ID of the doctor to update
     * @param newContactInformation the new contact information for the doctor
     * @return true if the update was successful, false otherwise
     */
    public boolean updateDoctorContact(String hospitalID, String newContactInformation) {
        Doctor doctor = doctors.get(hospitalID);
        if (doctor != null) {
            doctor.setContactInformation(newContactInformation);
            saveDoctorsToCSV(); // Save changes after update
            return true;
        }
        return false;
    }

    /**
     * Lists all doctors in the system by printing their information to the console.
     */
    public void listAllDoctors() {
        doctors.values().forEach(doc -> {
            System.out.println("Doctor ID: " + doc.getHospitalID());
            System.out.println("Name: " + doc.getName());
            System.out.println("Contact Information: " + doc.getContactInformation());
            System.out.println("------------------------");
        });
    }

    /**
     * Loads doctors from a CSV file into the service's internal collection.
     */
    public void loadDoctorFromCSV() {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            // Skip the header line
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] doctorData = line.split(DELIMITER);
                if (doctorData.length == 3) {
                    String doctorId = doctorData[0].trim();
                    String name = doctorData[1].trim();
                    String contactInformation = doctorData[2].trim();
                    User existingUser = userService.getUserById(doctorId);
                    String password = (existingUser != null) ? existingUser.getPassword() : "defaultPassword";
                    doctors.put(doctorId, new Doctor(existingUser != null ? existingUser : new User(doctorId, password, UserRole.DOCTOR), name, contactInformation));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Doctors CSV file: " + e.getMessage());
        }
    }

    /**
     * Saves the current list of doctors back to the CSV file.
     */
    public void saveDoctorsToCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            bw.write("Doctor ID,Name,Contact Information");
            bw.newLine();
            for (Doctor doctor : doctors.values()) {
                String line = String.join(DELIMITER,
                        doctor.getHospitalID(),
                        doctor.getName(),
                        doctor.getContactInformation());
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving the Doctors CSV file: " + e.getMessage());
        }
    }

    /**
     * Accepts an appointment request based on the provided request ID.
     *
     * @param requestId the ID of the appointment request to accept
     */
    public void acceptanceOfRequest(String requestId) {
        List<AppointmentRequest> pendingRequests = appointmentRequestService.getPendingRequests();
        for (AppointmentRequest request : pendingRequests) {
            if (request.getRequestId().equals(requestId)) {
                appointmentRequestService.acceptRequest(request);
                return; // Exit the function after processing
            }
        }
        System.out.println("No pending request found with the provided ID.");
    }

    /**
     * Declines an appointment request based on the provided request ID.
     *
     * @param requestId the ID of the appointment request to decline
     */
    public void declineRequest(String requestId) {
        List<AppointmentRequest> pendingRequests = appointmentRequestService.getPendingRequests();
        for (AppointmentRequest request : pendingRequests) {
            if (request.getRequestId().equals(requestId)) {
                appointmentRequestService.declineRequest(request);
                return; // Exit the function after processing
            }
        }
        System.out.println("No pending request found with the provided ID.");
    }

    /**
     * Sets the availability for a doctor on a specific date and time slot.
     *
     * @param doctor the Doctor object for which to set availability
     * @param dateStr the date in yyyy-MM-dd format
     * @param timeSlotStr the time slot in HH:mm format
     * @return true if the availability was set successfully, false otherwise
     */
    public boolean setAvailablity(Doctor doctor, String dateStr, String timeSlotStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr, DATE_FORMAT);
            LocalTime timeSlot = LocalTime.parse(timeSlotStr, TIME_FORMAT);
            scheduleService.setAvailable(doctor.getHospitalID(), date, timeSlot);
            return true; // Indicate success
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date or time format. Please use yyyy-MM-dd for date and HH:mm for time.");
            return false;
        }
    }

    /**
     * Sets the unavailability for a doctor on a specific date and time slot.
     *
     * @param doctor the Doctor object for which to set unavailability
     * @param dateStr the date in yyyy-MM-dd format
     * @param timeSlotStr the time slot in HH:mm format
     * @return true if the unavailability was set successfully, false otherwise
     */
    public boolean setUnavailability(Doctor doctor, String dateStr, String timeSlotStr) {
        try {
            LocalDate date = LocalDate.parse(dateStr, DATE_FORMAT);
            LocalTime timeSlot = LocalTime.parse(timeSlotStr, TIME_FORMAT);
            scheduleService.setUnavailable(doctor.getHospitalID(), date, timeSlot);
            return true;
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date or time format. Please use yyyy-MM-dd for date and HH:mm for time.");
            return false;
        }
    }

    /**
     * Updates a patient's medical record by adding a new diagnosis.
     *
     * @param patientID the ID of the patient whose record to update
     * @param newDiagnosis the new diagnosis to add
     */
    public void updatePatientDiagnosis(String patientID, String newDiagnosis) {
        MedicalRecord record = medicalRecordService.getMedicalRecord(patientID);
        if (record != null) {
            record.addNewDiagnosis(newDiagnosis);
            medicalRecordService.saveRecordsToCSV(); // Save the updated records
            System.out.println("Diagnosis added for Patient ID: " + patientID);
        } else {
            System.out.println("No medical record found for Patient ID: " + patientID);
        }
    }

    /**
     * Updates a patient's medical record by adding a new prescription.
     *
     * @param patientID the ID of the patient whose record to update
     * @param newPrescription the new prescription to add
     */
    public void updatePatientPrescription(String patientID, String newPrescription) {
        MedicalRecord record = medicalRecordService.getMedicalRecord(patientID);
        if (record != null) {
            record.addNewPrescription(newPrescription);
            medicalRecordService.saveRecordsToCSV(); // Save the updated records
            System.out.println("Prescription added for Patient ID: " + patientID);
        } else {
            System.out.println("No medical record found for Patient ID: " + patientID);
        }
    }
	 
    /**
     * Records the outcome of an appointment, including the services provided, 
     * prescribed medications, and any consultation notes. The method first 
     * checks if the appointment is in a PENDING state before proceeding to 
     * record the outcome.
     *
     * @param appointmentId the ID of the appointment to record the outcome for
     * @param serviceProvided a description of the services provided during the appointment
     * @param medicationsInput a comma-separated string of medication names to be prescribed
     * @param consultationNotes additional notes regarding the consultation
     */
    public void recordAppointmentOutcome(String appointmentId, String serviceProvided, String medicationsInput, String quantitiesInput, String consultationNotes) {
        Appointment appointment = appointmentService.getAppointment(appointmentId);

        // Check if the appointment exists and is in a CONFIRMED state
        if (appointment != null && appointment.getStatus() == AppointmentStatus.CONFIRMED) {
            // Split the input strings by commas to get medication names and quantities
            String[] medicationNames = medicationsInput.split(";");
            String[] quantityStrings = quantitiesInput.split(";");

            // Check if the number of medications matches the number of quantities
            if (medicationNames.length != quantityStrings.length) {
                System.out.println("Error: The number of medications does not match the number of quantities.");
                return;
            }

            // Retrieve the list of inventory items
            InventoryDataStore inventoryDataStore=new InventoryDataStore();
            List<Inventory> inventoryList = inventoryDataStore.getInventoryList();
            List<String> availableMedicineNames = inventoryList.stream()
                    .map(Inventory::getMedicineName)
                    .collect(Collectors.toList());

            // Create lists to hold Medication objects and quantities
            List<Medication> prescribedMedications = new ArrayList<>();
            List<Integer> prescribedQuantities = new ArrayList<>();

            // Create Medication objects from the medication names and quantities
            for (int i = 0; i < medicationNames.length; i++) {
                String medicationName = medicationNames[i].trim();
                int quantity = Integer.parseInt(quantityStrings[i].trim());

                // Check if the medication is in the inventory
                if (availableMedicineNames.contains(medicationName)) {
                    // Add the medication and its quantity
                    prescribedMedications.add(new Medication(medicationName, quantity, MedicationStatus.PENDING));
                    prescribedQuantities.add(quantity);
                } else {
                    System.out.println("Warning: Medication '" + medicationName + "' is not in inventory and will not be recorded.");
                }
            }

            // Proceed if there are valid medications to record
            // Check for inventory
            if (!prescribedMedications.isEmpty()) {
                // Record the appointment outcome with the created medications and quantities
                appointment.setServiceProvided(serviceProvided);
                appointment.setConsultationNotes(consultationNotes);
                appointment.setMedications(prescribedMedications);
                appointment.setQuantities(prescribedQuantities);
                appointment.setStatus(AppointmentStatus.COMPLETED);
                appointmentService.saveAppointmentsToCSV();

                System.out.println("Appointment outcome recorded successfully.");

                // Generate and write billing entry to Billing.csv
                writeBillingEntry(appointment);
            } else {
                System.out.println("Error: No valid medications found in inventory for this appointment.");
            }
        } else {
            System.out.println("Appointment not found or already completed.");
        }
    }


    /**
     * Writes a billing entry for a completed appointment to the Billing.csv file.
     *
     * @param appointment The completed appointment for which billing is recorded.
     */
    private void writeBillingEntry(Appointment appointment) {
        String billingFilePath = "data/Billing.csv";
        String invoiceID = UUID.randomUUID().toString();  // Generate a unique Invoice ID
        String patientID = appointment.getPatientId();
        String doctorID = appointment.getDoctorId();
        String appointmentID = appointment.getAppointmentId();
        double totalAmount = 0.0;
        String status = "UNPAID";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(billingFilePath, true))) {
            // Check if Billing.csv exists and write header if file is empty
            if (new java.io.File(billingFilePath).length() == 0) {
                writer.write("InvoiceID,PatientID,DoctorID,AppointmentID,Total_Amount,Status");
                writer.newLine();
            }
            
            // Write the billing entry
            String billingEntry = String.join(",",
                invoiceID, 
                patientID, 
                doctorID, 
                appointmentID, 
                String.valueOf(totalAmount), 
                status
            );
            writer.write(billingEntry);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error writing to Billing.csv: " + e.getMessage());
        }
    }


}