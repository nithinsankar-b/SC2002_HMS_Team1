package models;
import enums.UserRole;
import java.util.HashMap;

public class Administrator extends User {
    private HashMap<String, Staff> hospitalStaff;  // Changed to Staff type for clarity

    // Constructor
    public Administrator(String hospitalID, String password,UserRole role) {
        super(hospitalID, password, role);
        super.setRole(UserRole.ADMINISTRATOR);  // Set appropriate role
        this.hospitalStaff = new HashMap<>();
    }

    // Getter and Setter for hospital staff
    public HashMap<String, Staff> getHospitalStaff() {
        return hospitalStaff;
    }

    public void setHospitalStaff(HashMap<String, Staff> hospitalStaff) {
        this.hospitalStaff = hospitalStaff;
    }

}

















