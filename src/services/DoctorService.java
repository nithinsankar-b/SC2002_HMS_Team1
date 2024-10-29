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
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import enums.AppointmentStatus;
import enums.MedicationStatus;
import enums.UserRole;
import models.Doctor;
import models.Patient;
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

public class DoctorService {
    private static final String CSV_FILE_PATH = "data/doctor.csv" ;
	private static final String DELIMITER = ",";
	private Map<String, Doctor> doctors;
    private final UserService userService;
    private final AppointmentRequestService appointmentRequestService;
    private final ScheduleService scheduleService;
    private final MedicalRecordService medicalRecordService;
    private final AppointmentService appointmentService;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm");
   

    public DoctorService(UserService userService,ScheduleService scheduleService,MedicalRecordService medicalRecordService, AppointmentService appointmentService ) {
        this.userService = userService;
        this.doctors = new HashMap<>();
        this.appointmentService = appointmentService;
        this.medicalRecordService = medicalRecordService;
        this.scheduleService = scheduleService;
        this.appointmentRequestService = new AppointmentRequestService(scheduleService, appointmentService);
        this.doctors = new HashMap<>();
		loadDoctorFromCSV();
    }
    
    public Doctor getDoctorById(String hospitalID) {
    	System.out.println(doctors.get(hospitalID));
    	return doctors.get(hospitalID);
    }
    
    public boolean updateDoctorContact(String hospitalID, String newContactInformation) {
        Doctor doctor = doctors.get(hospitalID);
        if (doctor != null) {
            doctor.setContactInformation(newContactInformation);
            saveDoctorsToCSV(); // Save changes after update
            return true;
        }
        return false;
    }
    
    public void listAllDoctors() {
        doctors.values().forEach(doc -> {
            System.out.println("Doctor ID: " + doc.getHospitalID());
            System.out.println("Name: " + doc.getName());
            System.out.println("Contact Information: " + doc.getContactInformation());
            System.out.println("------------------------");
        });
    }
    
 // Method to load doctors from a CSV file
    public void loadDoctorFromCSV() {
        String line;
        

        try (BufferedReader br = new BufferedReader(new FileReader(CSV_FILE_PATH))) {
            // Skip the header line
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] doctorData = line.split(DELIMITER);

                // Assuming the CSV has 3 fields: ID, Name, Contact Information
                if (doctorData.length == 3) {
                    String doctorId = doctorData[0].trim();
                    String name = doctorData[1].trim();
                    String contactInformation = doctorData[2].trim();
                    
                    // Try to find an existing user if applicable
                    User existingUser = userService.getUserById(doctorId);
                    String password = (existingUser != null) ? existingUser.getPassword() : "defaultPassword";

                    // Create a new doctor object and add it to the collection
                    doctors.put(doctorId, new Doctor(existingUser != null ? existingUser : new User(doctorId, password, UserRole.DOCTOR), name, contactInformation));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading Doctors CSV file: " + e.getMessage());
        }
    }
    
    // Method to save Doctors back to the CSV file
    public void saveDoctorsToCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CSV_FILE_PATH))) {
            bw.write("Doctor ID,Name,Contact Information");
            bw.newLine();
            for (Doctor Doctor : doctors.values()) {
                String line = String.join(DELIMITER,
                        Doctor.getHospitalID(),
                        Doctor.getName(),
                        Doctor.getContactInformation()); // Include contact information
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving the Doctors CSV file: " + e.getMessage());
        }
    }
    
    
    // Function to accept the appointment request based on request ID
    public void acceptanceOfRequest(String requestId) {
        // Retrieve the list of pending requests
        List<AppointmentRequest> pendingRequests = appointmentRequestService.getPendingRequests();

        // Search for the request ID in the list of pending requests
        for (AppointmentRequest request : pendingRequests) {
            if (request.getRequestId().equals(requestId)) {
                // If a match is found, accept the request
                appointmentRequestService.acceptRequest(request);
                return; // Exit the function after processing
            }
        }

        // If no matching request was found
        System.out.println("No pending request found with the provided ID.");
    }
    
  //get input
	  // Function to decline the appointment request based on request ID
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
  
//Setting Availability for doctor
  public boolean setAvailablity(Doctor doctor, String dateStr, String timeSlotStr) {
      try {
          // Convert the date and time from String to LocalDate and LocalTime
          LocalDate date = LocalDate.parse(dateStr, DATE_FORMAT);
          LocalTime timeSlot = LocalTime.parse(timeSlotStr, TIME_FORMAT);

          // Call the setAvailable method from ScheduleService
          scheduleService.setAvailable(doctor.getHospitalID(), date, timeSlot);
           return true;// Indicate success
      } catch (DateTimeParseException e) {
          System.out.println("Invalid date or time format. Please use yyyy-MM-dd for date and HH:mm for time.");
          return false;
      }
  }
  
  //collect input 
  //Set Unavailability for doctor
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
  
  //in the controller have the input 
  //Updating MedicalRecords by adding new diagnosis / new prescription 
  public void updatePatientDiagnosis(String patientID, String newDiagnosis) {
      // Get the medical record for the patient
      MedicalRecord record = medicalRecordService.getMedicalRecord(patientID);
      
      if (record != null) {
          // Call addNewDiagnosis on the MedicalRecord instance
          record.addNewDiagnosis(newDiagnosis);
          medicalRecordService.saveRecordsToCSV(); // Save the updated records
          System.out.println("Diagnosis added for Patient ID: " + patientID);
      } else {
          System.out.println("No medical record found for Patient ID: " + patientID);
      }
  }
  
  //in the controller have the input 
	 public void updatePatientPrescription(String patientID, String newPrescription) {
 	 // Get the medical record for the patient
     MedicalRecord record = medicalRecordService.getMedicalRecord(patientID);
     
     if (record != null) {
         // Call addNewPrescription on the MedicalRecord instance
         record.addNewPrescription(newPrescription);
         medicalRecordService.saveRecordsToCSV(); // Save the updated records
         System.out.println("Diagnosis added for Patient ID: " + patientID);
     } else {
         System.out.println("No medical record found for Patient ID: " + patientID);
     }
 }
	 
	//get input 
	  public void recordAppointmentOutcome(String appointmentId, String serviceProvided, String medicationsInput, String consultationNotes) {
	        Appointment appointment = appointmentService.getAppointment(appointmentId);
	        
	        // Check if the appointment exists and is in a PENDING state
	        if (appointment != null && appointment.getStatus() == AppointmentStatus.PENDING) {
	            // Create a list to hold Medication objects
	            List<Medication> prescribedMedications = new ArrayList<>();

	            // Split the input string by commas to get medication names
	            String[] medicationNames = medicationsInput.split(",");

	            // Create Medication objects from the medication names
	            for (String medicationName : medicationNames) {
	                // Trim whitespace and create Medication object with PENDING status
	                prescribedMedications.add(new Medication(medicationName.trim(), 1, MedicationStatus.PENDING)); // Default quantity to 1, adjust as needed
	            }

	            // Record the appointment outcome with the created medications
	            appointmentService.recordAppointmentOutcome(appointmentId, serviceProvided, prescribedMedications, consultationNotes);
	            System.out.println("Appointment outcome recorded successfully.");
	        } else {
	            System.out.println("Appointment not found or already completed.");
	        }
	   }
    
    



}