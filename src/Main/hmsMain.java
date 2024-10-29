import controllers.AuthController;
import controllers.FYPCoordinatorController;
import controllers.PatientController;
import controllers.StudentController;
import controllers.SupervisorController;
import models.User;
import services.DataService;
import stores.AuthStore;
import stores.DataStore;

public class hmsMain{

    // Constructor
    // Set to private access to prevent instantiation
    private hmsMain(){
    }
    public static void main(String[] args) {
        // Using exception handling to catch any exceptions
        try{
            // Encapsulate in a while loop to allow multiple users to operate the application
            do{
                // Initialize datastore

                // Authentication

                // Start session
                // Get User from authentication
                switch(user){
                    case "Patient":
                         new PatientController().start();
                         break;
                    case "Doctor":
                        new DoctorController().start();
                        break;
                    case "Admin":
                        new AdminController().start();
                        break;
                    default:
                        System.out.println("Invalid user. Please try again.");
                        break;
                }
            } while(true);
        } catch (Exception e){
            // Save data and log out
            
            // Display error message
            System.out.println("Error encountered. Please reboot HMS Application.")
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}