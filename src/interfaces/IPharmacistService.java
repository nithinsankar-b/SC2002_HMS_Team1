package interfaces;

import models.Pharmacist;
import java.util.List;

/**
 * Interface for pharmacist service operations.
 * This interface defines methods for loading and saving pharmacist data from and to a CSV file.
 */
public interface IPharmacistService {

    /**
     * Loads a list of pharmacists from a CSV file.
     *
     * @param filePath The path to the CSV file containing pharmacist data.
     * @return A list of pharmacists loaded from the CSV file.
     */
    List<Pharmacist> loadPharmacistsFromCSV(String filePath);

    /**
     * Saves a list of pharmacists to a CSV file.
     *
     * @param pharmacists The list of pharmacists to save.
     * @param filePath The path to the CSV file where pharmacist data will be saved.
     */
    void savePharmacistsToCSV(List<Pharmacist> pharmacists, String filePath);
}
