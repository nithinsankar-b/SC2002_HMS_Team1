
package services;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import enums.StatusEnum;
import models.ReplenishmentRequest;

public class ReplenishmentService {

    private final String csvFile = "data/replenishment_requests.csv";

    // Constructor to ensure CSV file exists
    public ReplenishmentService() {
        generateCsvIfNotExists();
    }

    public List<ReplenishmentRequest> createReplenishmentRequest(List<String> medicines) {
        List<ReplenishmentRequest> newRequests = new ArrayList<>();

        for (String medicine : medicines) {
            // Check if the medicine already has a pending request
            if (isMedicineAlreadyRequested(medicine)) {
                System.out.println("Replenishment request for " + medicine + " already exists. It will not be added again.");
                continue;  // Skip this medicine
            }

            // Create a new request for the medicine
            List<String> singleMedicineList = List.of(medicine);  // Single-item list
            ReplenishmentRequest request = new ReplenishmentRequest(singleMedicineList);
            writeToCsv(request);  // Write this single request to CSV
            newRequests.add(request);
        }

        return newRequests;  // Return the list of newly created requests
    }

    private void writeToCsv(ReplenishmentRequest request) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile, true))) {
            for (String medicine : request.getMedicines()) {
            	
                String uniqueRequestId = request.getId(); 
                System.out.println(uniqueRequestId);// Generate a unique ID for this medicine
                String csvRow = String.format("%s,%s,%s",
                                               uniqueRequestId,
                                               medicine,
                                               request.getStatus().name()
                                               );
                writer.write(csvRow);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // Retrieves all replenishment requests from the CSV file (skips header)
    public List<ReplenishmentRequest> getAllRequests() {
        List<ReplenishmentRequest> requests = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            // Skip the header line
            String line = reader.readLine(); // Read and ignore the first line (header)
            
            while ((line = reader.readLine()) != null) { // Read the rest of the file
                ReplenishmentRequest request = parseCsvRow(line); // Process each row
                if (request != null) {
                    requests.add(request);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return requests;
    }

    // Method to check if a medicine already exists in the CSV with status PENDING
    private boolean isMedicineAlreadyRequested(String medicineName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            // Skip header
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String existingMedicine = parts[1].trim(); // Medicine name is in the second column
                    String status = parts[2].trim();          // Status is in the third column
                    if(existingMedicine.equalsIgnoreCase(medicineName) && status.equalsIgnoreCase(StatusEnum.PENDING.name())) {
                        return true; // Medicine already requested with PENDING status
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false; // Medicine not found in any pending request
    }

    // Retrieves replenishment requests by status
    public List<ReplenishmentRequest> getRequestsByStatus(StatusEnum status) {
        return getAllRequests().stream()
                .filter(request -> request.getStatus() == status)
                .collect(Collectors.toList());
    }

    

   // Method to parse each row from the CSV file
   private ReplenishmentRequest parseCsvRow(String csvRow) {
    String[] parts = csvRow.split(",");
    if (parts.length < 3) return null;

    String id = parts[0].trim();  // Use the ID from the CSV
    List<String> medicines = List.of(parts[1].split(";")); // Assumes medicines are separated by semicolons

    StatusEnum status;
    try {
        status = StatusEnum.valueOf(parts[2].trim());
    } catch (IllegalArgumentException e) {
        status = StatusEnum.PENDING;
        System.out.println("Invalid status value, defaulting to PENDING.");
    }

    return new ReplenishmentRequest(id, medicines, status);
}

    // Generates the CSV file with headers if it does not exist
    private void generateCsvIfNotExists() {
        File file = new File(csvFile);
        if (!file.exists()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
                writer.write("Request ID,Medicines,Status");
                writer.newLine();
                System.out.println("CSV file created with header.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    
    // Approves a replenishment request by changing its status to APPROVED
    public String approveRequest(String requestId) {
        boolean found = false;
        String medicineName = null;
    
        List<ReplenishmentRequest> requests = getAllRequests();
        for (ReplenishmentRequest request : requests) {
            if (request.getId().equals(requestId) && request.getStatus() == StatusEnum.PENDING) {
                request.setStatus(StatusEnum.APPROVED);
                medicineName = request.getMedicines().get(0); // Assuming single medicine per request
                found = true;
                break;
            }
        }
    
        if (found) {
            // Save the updated status to CSV
            saveRequestsToCSV(requests);
            System.out.println("Replenishment request approved for: " + medicineName);
            return medicineName;
        } else {
            System.out.println("Request ID not found or already approved.");
            return null;
        }
    }

    // Rejects a replenishment request by changing its status to REJECTED
    public boolean rejectRequest(String requestId) {
        boolean result = updateRequestStatus(requestId, StatusEnum.REJECTED);
        if (!result) {
            System.out.println("Request ID " + requestId + " not found. Rejection failed.");
        }
        return result;
    }

    		
 // Updates the status of a specific replenishment request by ID
    public boolean updateRequestStatus(String requestId, StatusEnum newStatus) {
        boolean found = false;

        try {
            List<String> lines = Files.readAllLines(Paths.get(csvFile));
            List<String> updatedLines = new ArrayList<>();

            for (String line : lines) {
                if (line.startsWith(requestId + ",")) {  // Check if the line starts with the request ID
                    found = true;  // Mark as found if the request ID matches
                    String[] parts = line.split(",");
                    parts[2] = newStatus.name();  // Update the status part
                    line = String.join(",", parts);  // Re-join the line with the updated status
                }
                updatedLines.add(line);
            }

            if (found) {
                Files.write(Paths.get(csvFile), updatedLines);  // Overwrite the file with updated content only if found
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return found;  // Return true if the request was found and updated, false otherwise
    }
// Helper method to save the requests list back to CSV after updating statuses
private void saveRequestsToCSV(List<ReplenishmentRequest> requests) {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile))) {
        writer.write("Request ID,Medicines,Status");
        writer.newLine();
        for (ReplenishmentRequest request : requests) {
            writer.write(request.toCsvFormat());
            writer.newLine();
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}
