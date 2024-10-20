package models;

import utils.CSVUtils;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class Administrator extends User {

    private HashMap<String, User> hospitalStaff;   
    private List<Appointment> appointments;        
    private Inventory inventory;                   

    // Constructor to load data from CSV files
    public Administrator(String userID, String password, String name, String email, String staffFilePath, String inventoryFilePath) {
        super(userID, password, name, email);  
        this.hospitalStaff = CSVUtils.loadStaffFromCSV(staffFilePath);  // Load staff from CSV
        this.inventory = CSVUtils.loadInventoryFromCSV(inventoryFilePath);  // Load inventory from CSV
        this.appointments = new ArrayList<>();  // Assuming appointments are initialized later
    }

    // Getters and Setters
    public HashMap<String, User> getHospitalStaff() {
        return hospitalStaff;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }
}















