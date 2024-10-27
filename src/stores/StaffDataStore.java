package stores;

import models.Staff;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class StaffDataStore {
    private Map<String, Staff> staffList = new HashMap<>();

    // Load staff data from CSV file
    public void loadStaffFromCSV(String csvFilePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] fields = line.split(",");
            // Assuming fields are: id, name, gender, role, age
            String id = fields[0];
            String name = fields[1];
            String gender = fields[2];
            String role = fields[3];
            int age = Integer.parseInt(fields[4]);

            Staff staff = new Staff(id, name, gender, role, age);
            staffList.put(id, staff);  // Add to staffList
        }
        reader.close();
    }

    // Write staff data to CSV file
    public void writeStaffToCSV(String csvFilePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath));
        for (Staff staff : staffList.values()) {
            writer.write(staff.getId() + "," + staff.getName() + "," +
                         staff.getGender() + "," + staff.getRole() + "," +
                         staff.getAge());
            writer.newLine();
        }
        writer.close();
    }

    // Method to add or update a staff member
    public void addOrUpdateStaff(Staff staff) {
        // If the staff ID exists, it will be updated; otherwise, it will be added
        staffList.put(staff.getId(), staff);
    }

    // Method to remove a staff member by ID
    public void removeStaff(String staffId) {
        staffList.remove(staffId);
    }

    // Getter for staffList
    public Map<String, Staff> getStaffList() {
        return staffList;
    }
}







