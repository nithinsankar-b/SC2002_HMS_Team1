package interfaces;

/**
 * Interface for doctor-related view operations.
 * Defines methods for displaying menus, success messages, and error messages
 * in the doctor interface of the system.
 */
public interface IDoctorView {

   /**
    * Displays the main menu for doctor operations.
    * This menu allows the doctor to navigate through various functionalities
    * such as viewing schedules, managing appointments, and updating patient records.
    */
   void displayMenu();

   /**
    * Displays a success message to the user.
    * This is used to provide positive feedback when an operation is successfully completed.
    *
    * @param message The success message to be displayed.
    */
   void showSuccessMessage(String message);

   /**
    * Displays an error message to the user.
    * This is used to inform the user of any issues or failures during an operation.
    *
    * @param message The error message to be displayed.
    */
   void showErrorMessage(String message);
}

