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

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class UserService implements IUserService {
    private final Map<String, User> users;

    private static final String ALGORITHM = "AES";
    // For encryption
    private static final String SECRET_KEY = "MySuperSecretKey";

    // There is already a database in the CSV file
    public UserService() {
        users = new HashMap<>();
        loadUsersFromCSV( "data/User.csv"); // Adjusted for relative path
    }

    static String encryptPassword(String plainPassword) throws Exception {
        SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(plainPassword.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    private static String decryptPassword(String encryptedPassword) throws Exception {
        SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedPassword);
        return new String(cipher.doFinal(decodedBytes));
    }

    // Utility function to encrypt existing passwords (To be removed)
    public void encryptExistingPasswords() {
        try {
            for (User user : users.values()) {
                String currentPassword = user.getPassword();
                if (!currentPassword.startsWith("ENC(")) {
                    try {
                        String encryptedPassword = encryptPassword(currentPassword);
                        user.setPassword("ENC(" + encryptedPassword + ")");
                    } catch (Exception e) {
                        System.out.println("Error encrypting password for user: " + user.getHospitalID());
                    }
                }
            }
            saveToCSV();
            System.out.println("Passwords have been encrypted successfully!");
        } catch (Exception e) {
            throw new RuntimeException("Error during password encryption process", e);
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
                    if (roleString.equalsIgnoreCase("role")) continue;

                    UserRole role = UserRole.valueOf(roleString.toUpperCase());
                    users.put(hospitalID, new User(hospitalID, password, role));
                } else {
                    System.out.println("Skipping malformed line in CSV: " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading CSV file: " + e.getMessage());
        }
    }

    // Save users to CSV include sorting
    public void saveToCSV() {
        try (FileWriter writer = new FileWriter("data/User.csv")) {
            writer.write("hospitalID,password,role\n");
            users.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey()) // Sort by hospitalID
                    .forEach(entry -> {
                        try {
                            User user = entry.getValue();
                            writer.write(user.getHospitalID() + "," + user.getPassword() + "," + user.getRole() + "\n");
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
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
        if (user != null) {
            try {
                String storedPassword = user.getPassword();
                if (storedPassword.startsWith("ENC(") && storedPassword.endsWith(")")) {
                    // Decrypt the encrypted password
                    String decryptedPassword = decryptPassword(storedPassword.substring(4, storedPassword.length() - 1));
                    return decryptedPassword.equals(password);
                } else {
                    // If the password is not encrypted, compare directly
                    return storedPassword.equals(password);
                }
            } catch (Exception e) {
                System.out.println("Error during password verification: " + e.getMessage());
            }
        }
        return false;
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
            try {
                String encryptedPassword = user.getPassword();
                if (encryptedPassword.startsWith("ENC(") && encryptedPassword.endsWith(")")) {
                    String decryptedPassword = decryptPassword(encryptedPassword.substring(4, encryptedPassword.length() - 1));
                    if (decryptedPassword.equals(oldPassword)) {
                        String newEncryptedPassword = encryptPassword(newPassword);
                        user.setPassword("ENC(" + newEncryptedPassword + ")");
                        users.put(hospitalID, user);
                        saveToCSV();
                        System.out.println("Password changed successfully!");
                        return true;
                    }
                }
            } catch (Exception e) {
                System.out.println("Error during password change: " + e.getMessage());
            }
        }
        System.out.println("Error! Wrong details entered or unable to process.");
        return false;
    }

    public void reloadUserData() {
        users.clear(); // Clear existing data
        loadUsersFromCSV("data/User.csv"); // Reload data from CSV
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

    public void addUser(User user) {
        users.put(user.getHospitalID(), user);
        saveUsersToCSV();
    }

    public boolean removeUser(String hospitalID) {
        if (users.remove(hospitalID) != null) {
            saveUsersToCSV();
            return true;
        }
        return false;
    }

    private void saveUsersToCSV() {
        try (FileWriter writer = new FileWriter("data/User.csv")) {
            for (User user : users.values()) {
                writer.write(user.getHospitalID() + "," + user.getPassword() + "," + user.getRole() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }
    private void writeUsersToCSV() {
        try (FileWriter writer = new FileWriter("data/User.csv")) {
            // Write header, if needed
            writer.write("hospitalID,password,role\n");
    
            // Write each user's details to the CSV
            for (User user : users.values()) {
                writer.write(user.getHospitalID() + "," + user.getPassword() + "," + user.getRole() + "\n");
            }
    
            System.out.println("User data saved successfully to user.csv.");
        } catch (IOException e) {
            System.out.println("Error writing to CSV file: " + e.getMessage());
        }
    }
    
}

