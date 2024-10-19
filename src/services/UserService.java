package services;

import models.User;
import enums.UserRole; // Import the UserRole enum
import interfaces.IUserService; // Import the IUserService interface

import java.util.HashMap;
import java.util.Map;

public class UserService implements IUserService {
    private Map<String, User> users;

    public UserService() {
        users = new HashMap<>();
        users.put("12345", new User("12345", "password", UserRole.PATIENT));
        users.put("67890", new User("67890", "password", UserRole.DOCTOR));
    }

    // Method to validate login
    @Override
    public boolean login(String hospitalID, String password) {
        User user = users.get(hospitalID);
        return user != null && user.getPassword().equals(password);
    }

    // Method to change user password
    @Override
    public boolean changePassword(String hospitalID, String oldPassword, String newPassword) {
        User user = users.get(hospitalID);
        if (user != null && user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
            return true;
        }
        return false;
    }

    // Method to get user role
    @Override
    public UserRole getUserRole(String hospitalID) {
        User user = users.get(hospitalID);
        return user != null ? user.getRole() : null;
    }
}