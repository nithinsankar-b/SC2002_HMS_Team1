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

public class UserService implements IUserService {
    private final Map<String, User> users;

    // There is already a database in the CSV file
    public UserService() {
        users = new HashMap<>();
        loadUsersFromCSV( "data/User.csv"); // Adjusted for relative path
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
    
 // Save users to CSV
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


    // Login method
    @Override
    public boolean login(String hospitalID, String password) {
        User user = users.get(hospitalID);
        return user != null && user.getPassword().equals(password);
    }

    // Change password
    @Override
    public boolean changePassword(String hospitalID, String oldPassword, String newPassword) {
        User user = users.get(hospitalID);
        if (user != null && user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
            saveToCSV();
            loadUsersFromCSV("data/User.csv");
            return true;
        }
        return false;
    }

    // Get user role
    @Override
    public UserRole getUserRole(String hospitalID) {
        User user = users.get(hospitalID);
        return user != null ? user.getRole() : null;
    }

    // Returns a user by the UserID
    public User getUserById(String hospitalID) {
        return users.get(hospitalID);
    }

    public boolean updateUser(User user) {
        if (users.containsKey(user.getHospitalID())) {
            users.put(user.getHospitalID(), user);
            saveToCSV();
            return true;
        }
        return false;
    }


}
