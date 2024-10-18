package stores;

import interfaces.IPharmacistService;
import models.Pharmacist;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * Singleton class to manage Pharmacist data storage and retrieval.
 */
public class PharmacistDataStore {

    private static IPharmacistService pharmacistDataService;
    private static Map<String, String> pharmFilePathsMap;
    private static Map<String, Pharmacist> pharmacistsData = new HashMap<>();

    // Private constructor to prevent instantiation
    private PharmacistDataStore() {
    }

    /**
     * Initializes the PharmacyDataStore with the provided service and file paths.
     *
     * @param pharmacistDataService The service responsible for loading and saving pharmacist data.
     * @param pharmFilePathsMap     A map containing file paths, with the key "pharmacist" pointing to the pharmacists CSV file.
     * @return true if initialization is successful, false otherwise.
     */
    public static boolean initDataStore(IPharmacistService pharmacistDataService, Map<String, String> pharmFilePathsMap) {
        // Initialize the static variables
        PharmacistDataStore.pharmacistDataService = pharmacistDataService;
        PharmacistDataStore.pharmFilePathsMap = pharmFilePathsMap;

        // Import data from CSV and populate the pharmacistsData map
        List<Pharmacist> pharmacistList = pharmacistDataService.loadPharmacistsFromCsv(pharmFilePathsMap.get("pharmacist"));
        if (pharmacistList == null) {
            System.err.println("Failed to load pharmacists from CSV.");
            return false;
        }

        for (Pharmacist pharmacist : pharmacistList) {
            pharmacistsData.put(pharmacist.getHospitalID(), pharmacist);
        }

        return true;
    }

    /**
     * Saves the current state of pharmacistsData to the CSV file.
     *
     * @return true if saving is successful, false otherwise.
     */
    public static boolean saveData() {
        if (pharmacistDataService == null || pharmFilePathsMap == null) {
            System.err.println("PharmacyDataStore is not initialized properly.");
            return false;
        }

        // Convert Map to List
        List<Pharmacist> pharmacistList = new ArrayList<>(pharmacistsData.values());

        // Save to CSV
        pharmacistDataService.savePharmacistsToCsv(pharmacistList, pharmFilePathsMap.get("pharmacist"));
        return true;
    }

    /**
     * Retrieves the current pharmacists data.
     *
     * @return A map of pharmacist IDs to Pharmacist objects.
     */
    public static Map<String, Pharmacist> getPharmacistData() {
        return pharmacistsData;
    }

    /**
     * Sets the pharmacists data and saves it to the CSV file.
     *
     * @param pharmacistsData A map of pharmacist IDs to Pharmacist objects.
     */
    public static void setPharmacistsData(Map<String, Pharmacist> pharmacistsData) {
        PharmacistDataStore.pharmacistsData = pharmacistsData;
        saveData();
    }

    /**
     * Adds a new pharmacist to the data store.
     *
     * @param pharmacist The Pharmacist object to add.
     */
    public static void addPharmacist(Pharmacist pharmacist) {
        pharmacistsData.put(pharmacist.getHospitalID(), pharmacist);
    }

    /**
     * Removes a pharmacist from the data store by ID.
     *
     * @param pharmacistId The ID of the pharmacist to remove.
     * @return true if removal is successful, false if pharmacist not found.
     */
    public static boolean removePharmacist(String pharmacistId) {
        if (pharmacistsData.containsKey(pharmacistId)) {
            pharmacistsData.remove(pharmacistId);
            return true;
        }
        return false;
    }

    /**
     * Updates an existing pharmacist's information.
     *
     * @param pharmacist The Pharmacist object with updated information.
     * @return true if update is successful, false if pharmacist not found.
     */
    public static boolean updatePharmacist(Pharmacist pharmacist) {
        if (pharmacistsData.containsKey(pharmacist.getHospitalID())) {


pharmacistsData.put(pharmacist.getHospitalID(), pharmacist);
            return true;
        }
        return false;
    }
}