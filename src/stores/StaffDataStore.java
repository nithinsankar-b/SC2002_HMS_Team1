package stores;

import models.Staff;
import java.io.*;
import java.util.*;

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
        boolean skipHeader = true; // Skip the header row
        while ((line = reader.readLine()) != null) {
            if (skipHeader) {
                skipHeader = false;
                continue;
            }
            String[] fields = line.split(",");
            if (fields.length != 5) {
                System.err.println("Skipping malformed line: " + line);
                continue;
            }
            try {
                String id = fields[0];
                String name = fields[1];
                String role = fields[2];
                String gender = fields[3];
                int age = Integer.parseInt(fields[4].trim());
                Staff staff = new Staff(id, name, role, gender, age);
                staffList.put(id, staff);
            } catch (NumberFormatException e) {
                System.err.println("Skipping line with invalid age: " + line);
            }
        }
        reader.close();
    }

    public void sortStaffList() {
        List<Staff> sortedStaff = new ArrayList<>(staffList.values());
        sortedStaff.sort(Comparator.comparing(Staff::getId)); // Ensures sorting by staffID
        staffList = new LinkedHashMap<>(); // Preserve insertion order
        for (Staff staff : sortedStaff) {
            staffList.put(staff.getId(), staff);
        }
    }

    /**
     * Writes the current staff list to a specified CSV file.
     *
     * @param csvFilePath The path to the CSV file.
     * @throws IOException If an error occurs while writing the file.
     */
    public void writeStaffToCSV(String csvFilePath) throws IOException {
        sortStaffList();
        BufferedWriter writer = new BufferedWriter(new FileWriter(csvFilePath));
        writer.write("staffID,name,role,gender,age"); // Add header
        writer.newLine();
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








