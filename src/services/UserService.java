package services;

import models.User;
import enums.UserRole;
import interfaces.IUserService;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class UserService implements IUserService {
    private final Map<String, User> users;

    // There is already a database in the CSV file
    public UserService() {
        users = new HashMap<>();
        loadUsersFromCSV( "data/User.csv"); // Load CSV
    }

    public boolean addUser(String hospitalID, String plaintextPassword, UserRole role) {
        if (users.containsKey(hospitalID)) {
            System.out.println("User with this hospital ID already exists.");
            return false; // User already exists
        }

        // Hash the password
        String hashedPassword = hashPassword(plaintextPassword);

        // Create and add the new user to the map
        User newUser = new User(hospitalID, hashedPassword, role);
        users.put(hospitalID, newUser);

        // Save the updated users map to CSV
        saveUsersToCSV();
        System.out.println("User added successfully.");
        return true;
    }

    // Hash password using SHA-256
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }

    // Save updated users back to CSV
    private void saveUsersToCSV() {
        try (FileWriter writer = new FileWriter("data/User.csv")) {
            writer.write("hospitalID,password,role\n"); // Write header
            for (User user : users.values()) {
                writer.write(user.getHospitalID() + "," + user.getPassword() + "," + user.getRole() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }

    // Load users from CSV
    private void loadUsersFromCSV(String filePath) {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while ((line = br.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length == 3) {
                    String hospitalID = userData[0].trim();
                    String password = userData[1].trim();
                    String roleString = userData[2].trim();
                    if(roleString.equals("role"))
                        continue;
                    UserRole role = UserRole.valueOf(userData[2].trim());
                    //System.out.println(hospitalID+password+userData[2].trim());
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
     * @param hashedInputPassword   The hashed password of the user.
     * @return True if authentication is successful, false otherwise.
     */
    @Override
    public boolean login(String hospitalID, String hashedInputPassword) {
        User user = users.get(hospitalID);
        return user != null && user.getPassword().equals(hashedInputPassword);
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
        if (user != null) {
            // Hash the old password input to check against the stored hashed password
            String hashedOldPassword = hashPassword(oldPassword);

            // If authentication pass aka old password input is correct then proceed
            if (user.getPassword().equals(hashedOldPassword)) {
                // Hash the new password and update the user's password
                String hashedNewPassword = hashPassword(newPassword);
                user.setPassword(hashedNewPassword);

                // Save updated users to CSV
                saveUsersToCSV();
                return true;
            }
        }
        return false; // Return false if user is not found or old password is incorrect
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
            return true;
        }
        return false;
    }
}
