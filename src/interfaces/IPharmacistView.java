package interfaces;

/**
 * Interface for pharmacist view operations.
 * This interface defines methods for displaying various information related to patients and appointments.
 */
public interface IPharmacistView {

    /**
     * Displays the menu options available to the pharmacist.
     */
   void displayMenu();
    
    /**
     * Views appointment outcome records.
     */
    void viewAppointmentOutcomeRecords();
}
