package stores;

import models.Staff;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * The StaffDataStore class is responsible for managing staff data,
 * including loading from and writing to a CSV file.
 */
public class StaffDataStore {

    private Map<String, Staff> staffList = new HashMap<>();

    /**
     * Loads staff data from a CSV file and populates the staff list.
     *
     * @param csvFilePath The path to the CSV file.
     * @throws IOException If an error occurs while reading the file.
     */
    public void loadStaffFromCSV(String csvFilePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] fields = line.split(",");
            // Assuming fields are: id, name, gender, role, age
            String id = fields[0];
            String name = fields[1];
            String role = fields[2];
            String gender = fields[3];
            int age = Integer.parseInt(fields[4]);

            Staff staff = new Staff(id, name, role, gender, age);
            staffList.put(id, staff);  // Add to staffList
        }
        reader.close();
    }

    /**
     * Writes the current staff list to a specified CSV file.
     *
     * @param csvFilePath The path to the CSV file.
     * @throws IOException If an error occurs while writing the file.
     */
    public void writeStaffToCSV(String csvFilePath) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath));
        for (Staff staff : staffList.values()) {
            writer.write(staff.getId() + "," + staff.getName() + "," +
                         staff.getRole() + "," + staff.getGender() + "," +
                         staff.getAge());
            writer.newLine();
        }
        writer.close();
    }

    /**
     * Adds a new staff member or updates an existing one.
     *
     * @param staff The Staff object to be added or updated.
     */
    public void addOrUpdateStaff(Staff staff) {
        staffList.put(staff.getId(), staff);
    }

    /**
     * Removes a staff member from the list by their ID.
     *
     * @param staffId The ID of the staff member to be removed.
     */
    public void removeStaff(String staffId) {
        staffList.remove(staffId);
    }

    /**
     * Retrieves the list of all staff members.
     *
     * @return A map of staff IDs to Staff objects.
     */
    public Map<String, Staff> getStaffList() {
        return staffList;
    }
}








