package controller;

import controller.UserController;
import services.UserService;


public class Main {
    public static void main(String[] args) {
        try {
            // Get user from authentication
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