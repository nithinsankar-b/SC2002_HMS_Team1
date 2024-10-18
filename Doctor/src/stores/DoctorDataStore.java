package stores;

import interfaces.IDoctorService;

import models.Doctor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class DoctorDataStore {

    private static IDoctorService doctorDataService;
    private static Map<String, String> docFilePathsMap;
    private static Map<String, Doctor> doctorsData = new HashMap<>();

    // Private constructor to prevent instantiation
    private DoctorDataStore() {
    }

    /**
     * Initializes the DoctorDataStore with the provided service and file paths.
     *
     * @param doctorDataService The service responsible for loading and saving doctor data.
     * @param docFilePathsMap   A map containing file paths, with the key "doctor" pointing to the doctors CSV file.
     * @return true if initialization is successful, false otherwise.
     */
    public static boolean initDataStore(IDoctorService doctorDataService, Map<String, String> docFilePathsMap) {
        // Initialize the static variables
        DoctorDataStore.doctorDataService = doctorDataService;
        DoctorDataStore.docFilePathsMap = docFilePathsMap;

        // Import data from CSV and populate the doctorsData map
        List<Doctor> doctorList = doctorDataService.loadDoctorsFromCsv(docFilePathsMap.get("doctor"));
        if (doctorList == null) {
            System.err.println("Failed to load doctors from CSV.");
            return false;
        }

        for (Doctor doctor : doctorList) {
            doctorsData.put(doctor.getHospitalID(), doctor);
        }

        return true;
    }

    /**
     * Saves the current state of doctorsData to the CSV file.
     *
     * @return true if saving is successful, false otherwise.
     */
    public static boolean saveData() {
        if (doctorDataService == null || docFilePathsMap == null) {
            System.err.println("DoctorDataStore is not initialized properly.");
            return false;
        }

        // Convert Map to List
        List<Doctor> doctorList = new ArrayList<>(doctorsData.values());

        // Save to CSV
        doctorDataService.saveDoctorsToCsv(doctorList, docFilePathsMap.get("doctor"));
        return true;
    }

    /**
     * Retrieves the current doctors data.
     *
     * @return A map of doctor IDs to Doctor objects.
     */
    public static Map<String, Doctor> getDoctorsData() {
        return doctorsData;
    }

    /**
     * Sets the doctors data and saves it to the CSV file.
     *
     * @param doctorsData A map of doctor IDs to Doctor objects.
     */
    public static void setDoctorsData(Map<String, Doctor> doctorsData) {
        DoctorDataStore.doctorsData = doctorsData;
        saveData();
    }

 
}


/*package stores;
import interfaces.IDoctorService;
import models.Doctor;
import java.util.HashMap;
import java.util.Map;

public class DoctorDataStore {

	private static IDoctorService doctorDataService;
	private static Map<String,String> docFilePathsMap;
	private static Map<String, Doctor> doctorsData = new HashMap<String, Doctor>();
	
	private DoctorDataStore() {
		
	}
	public static boolean initDataStore(IDoctorService doctorDataService, Map<String,String> docFilePathsMap) {
		//initializing the instance variables doctorDataService & docFilePathsMap
		DoctorDataStore.docFilePathsMap = docFilePathsMap;
		DoctorDataStore.doctorDataService = doctorDataService;
		//import data
		DoctorDataStore.doctorsData = doctorDataService.importDoctorData(docFilePathsMap.get("doctor"));
		return true;
	}
	public static boolean saveData() {
		DoctorDataStore.setDoctorsData(doctorsData);
		return true;
	}
	public static Map<String,Doctor>getDoctorsData(){
		return DoctorDataStore.doctorsData;
	}
	public static void setDoctorsData(Map<String, Doctor>doctorsData) {
		DoctorDataStore.doctorsData = doctorsData;
		doctorDataService.exportDoctorData(docFilePathsMap.get("doctor"), doctorsData);
	}
}

// Map to store doctors data
/*private static Map<String, Doctor> doctorsData = new HashMap<>();

@Override
public Map<String, Doctor> importDoctorData(String doctorFilePath) {
    try (BufferedReader br = new BufferedReader(new FileReader(doctorFilePath))) {
        String line;
        // Skip header
        br.readLine();
        while ((line = br.readLine()) != null) {
            String[] values = line.split(",");
            if (values.length < 3) continue; // Ensure enough data
            String hospitalID = values[0];
            String name = values[1];
            String password = values[2];

            Doctor doc = new Doctor(hospitalID, name, password);
            doctorsData.put(hospitalID, doc);
        }
    } catch (IOException e) {
        System.out.println("Error reading doctor data: " + e.getMessage());
    }
    return doctorsData;
}

@Override
public boolean exportDoctorData(String doctorFilePath, Map<String, Doctor> doctorMap) {
    try (FileWriter writer = new FileWriter(doctorFilePath)) {
        writer.write("HospitalID,Name,Password\n"); // Write header
        for (Doctor doc : doctorMap.values()) {
            String doctorLine = String.format("%s,%s,%s\n",
                    doc.getHospitalID(),
                    doc.getName(),
                    doc.getPassword());
            writer.write(doctorLine);
        }
        return true;
    } catch (IOException e) {
        System.out.println("Error writing doctor data: " + e.getMessage());
        return false;
    }
}

public static Map<String, Doctor> getDoctorsData() {
    return doctorsData;
}*/



