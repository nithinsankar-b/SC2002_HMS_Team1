package models;

public class User {
    private String hospitalID;
    private String password;
    private String role;

    public User(String hospitalID, String password, String role) {
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

    public String getRole() {
        return role;
    }

    // Setters
    public void setHospitalID(String hospitalID) {
        this.hospitalID = hospitalID;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
