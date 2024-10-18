package Models;

import enums.UserRole;

// Represents a User in the HMS application
/*
 * Users contains
 *      - Unique ID
 *      - Name
 *      - Email Address
 *      - Password
 *      - Role (Patient, Doctor, Admin)
 */

public class User {
    // Attributes
    private String userID;
    private String name;
    private String email;
    private String password;
    private UserRole role;

    // Constructor
    public User(String userID, String name, String email, String password){
        this.userID = userID;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getters
    public String getUserID(){
        return this.userID;
    }

    public String getName(){
        return this.name;
    }

    public String getEmail(){
        return this.email;
    }

    public String getPassword(){
        return this.password;
    }

    public UserRole getRole(){
        return this.role;
    }

    // Setters
    public Booelan setName(String name){
        this.name = name;
        return true;
    }

    public Boolean setEmail(String email){
        this.email = email;
        return true;
    }
    
    public Boolean setPassword(String oldPassword, String newPassword){
        // Check if old password is the same as new password to be set
        if (oldPassword.equals(newPassword)){
            return false;
        } else{
            this.password = newPassword;
            return true;
        }
    }

    // Set Role
    public Boolean setRole(UserRole role){
        this.role = role;
        return true;
    }
}
