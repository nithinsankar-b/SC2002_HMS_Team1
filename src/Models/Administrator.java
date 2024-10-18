package models;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Administrator extends User {
    private HashMap<String, User> hospitalStaff;   
    private List<Appointment> appointments;       
    private Inventory inventory;                   

    // Constructor
    public Administrator(String userID, String password, String name, String email) {
        super(userID, password, name, email);  
        this.hospitalStaff = new HashMap<>();  
        this.appointments = new ArrayList<>();  
        this.inventory = new Inventory();  
    }

    // Getters
    public HashMap<String, User> getHospitalStaff() {
        return hospitalStaff;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public Inventory getInventory() {
        return inventory;
    }

    // Setters
    public void setHospitalStaff(HashMap<String, User> hospitalStaff) {
        this.hospitalStaff = hospitalStaff;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}














