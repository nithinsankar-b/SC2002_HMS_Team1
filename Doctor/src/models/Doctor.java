package models;

import enums.UserRole;

public class Doctor extends User {
    
    public Doctor(String doctorID, String name, String password) {
        super(doctorID, name, password);  // Using hospitalID as doctorID
        super.setRole(UserRole.DOCTOR);
    }
}
