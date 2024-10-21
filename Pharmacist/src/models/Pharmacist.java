package models;

import enums.UserRole;

public class Pharmacist extends User {
    
    public Pharmacist(String staffID, String name, String password) {
        super(staffID, name, password);  // Using hospitalID as doctorID
        super.setRole(UserRole.PHARMACIST);
    }
}
