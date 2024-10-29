package models;

import enums.UserRole;

public class User {
    private String hospitalID;
    private String password;
    private UserRole role;

    public User(String hospitalID, String password, UserRole role) {
        this.hospitalID = hospitalID;
        this.password = password;
        this.role = role;
    }

    // Getters
    public String getHospitalID() {
        return hospitalID;
    }

    public String getPassword() {
        return password;
    }

    public UserRole getRole() {
        return role;
    }

    // Setters
    public void setHospitalID(String hospitalID) {
        this.hospitalID = hospitalID;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }
}