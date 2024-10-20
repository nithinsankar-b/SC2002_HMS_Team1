import controllers.UserController;
import services.UserService;
// import services.DataService;
// import stores.AuthStore;
// import stores.DataStore;

public class Main {
    public static void main(String[] args) {
        try {
            // Encapsulate main in while loop to allow multiple users to operate the application
            do { 
                // Need to initialize database

                // Need to set up authentication

                // Get user from authentication
                UserService userService = new UserService();
                UserController userController = new UserController(userService);
                userController.run(); // Don't know what this function is for

                // switch(user){
                //     case PATIENT:
                //          new PatientController().start();
                //          break;
                //     case DOCTOR:
                //         new DoctorController().start();
                //         break;
                //     case ADMIN:
                //         new AdminController().start();
                //         break;
                //     default:
                //         System.out.println("Invalid user. Please try again.");
                //         break;
            } while (true);
        } catch (Exception e) {
            // Need to save data and log out
            
            // Display error message
            System.out.println("Error encountered. Please reboot HMS Application.");
            System.out.println("An error occurred: " + e.getMessage());
        }

    }
}