import controllers.UserController;
import services.UserService;
// import services.DataService;
// import stores.AuthStore;
// import stores.DataStore;

public class Main {
    public static void main(String[] args) {
        try {
            UserService userService = new UserService();
            UserController userController = new UserController(userService);
            userController.run();
        } catch (Exception e) {
            // Need to save data and log out
            
            // Display error message
            System.out.println("Error encountered. Please reboot HMS Application.");
            System.out.println("An error occurred: " + e.getMessage());
        }

    }
}
