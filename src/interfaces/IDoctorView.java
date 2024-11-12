package interfaces;

public interface IDoctorView {

   /**
    * Displays the menu options for doctor operations.
    */
   void displayMenu();



   /**
    * Displays a success message to the user.
    *
    * @param message the message to be displayed
    */
   void showSuccessMessage(String message);

   /**
    * Displays an error message to the user.
    *
    * @param message the message to be displayed
    */
   void showErrorMessage(String message);

  
}
