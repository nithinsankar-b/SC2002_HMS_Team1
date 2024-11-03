package services;

import models.User;
import enums.UserRole;
import interfaces.IUserService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.io.FileWriter;

/**
 * UserService is responsible for managing user-related operations
 * such as loading user data from a CSV file, authenticating users,
 * changing passwords, and retrieving user roles.
 */
public class UserService implements IUserService {
    private final Map<String, User> users;

    /**
     * Constructs a UserService instance and loads users from the specified CSV file.
     */
    public UserService() {
        users = new HashMap<>();
        loadUsersFromCSV("data/User.csv"); // Adjusted for relative path
    }

    /**
     * Loads users from a CSV file into the users map.
     *
     * @param filePath The path to the CSV file containing user data.
     */
    private void loadUsersFromCSV(String filePath) {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length == 3) {
                    String hospitalID = userData[0].trim();
                    String password = userData[1].trim();
                    String roleString = userData[2].trim();
                    if (roleString.equals("role")) {
                        continue; // Skip header row
                    }
                    UserRole role = UserRole.valueOf(roleString.trim());
                    users.put(hospitalID, new User(hospitalID, password, role));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
    }

    /**
     * Authenticates a user with the provided hospital ID and password.
     *
     * @param hospitalID The hospital ID of the user.
     * @param password   The password of the user.
     * @return True if authentication is successful, false otherwise.
     */
    @Override
    public boolean login(String hospitalID, String password) {
        User user = users.get(hospitalID);
        return user != null && user.getPassword().equals(password);
    }

    /**
     * Changes the password of the user identified by the hospital ID.
     *
     * @param hospitalID The hospital ID of the user.
     * @param oldPassword The old password of the user.
     * @param newPassword The new password to set for the user.
     * @return True if the password change is successful, false otherwise.
     */
    @Override
    public boolean changePassword(String hospitalID, String oldPassword, String newPassword) {
        User user = users.get(hospitalID);
        if (user != null && user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
            return true;
        }
        return false;
    }

    /**
     * Retrieves the role of the user identified by the hospital ID.
     *
     * @param hospitalID The hospital ID of the user.
     * @return The UserRole of the user, or null if not found.
     */
    @Override
    public UserRole getUserRole(String hospitalID) {
        User user = users.get(hospitalID);
        return user != null ? user.getRole() : null;
    }

    /**
     * Retrieves a User object by its hospital ID.
     *
     * @param hospitalID The hospital ID of the user.
     * @return The User object, or null if not found.
     */
    public User getUserById(String hospitalID) {
        return users.get(hospitalID);
    }

    /**
     * Updates the information of a user in the users map.
     *
     * @param user The User object containing updated information.
     * @return True if the update is successful, false otherwise.
     */
    public boolean updateUser(User user) {
        if (users.containsKey(user.getHospitalID())) {
            users.put(user.getHospitalID(), user);
            saveToCSV();
            return true;
        }
        return false;
    }

    public void saveToCSV() {
        try (FileWriter writer = new FileWriter("data/User.csv")) {
            writer.write("hospitalID,password,role\n"); // CSV header
            for (User user : users.values()) {
                writer.write(user.getHospitalID() + "," + user.getPassword() + "," + user.getRole() + "\n");
            }
            System.out.println("Users saved to CSV successfully.");
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }
}
