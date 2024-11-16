
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

    /**
     * Constructor for `ReplenishmentService`.
     * <p>
     * Ensures that the replenishment requests CSV file exists when the service is initialized.
     * </p>
     */
    public ReplenishmentService() {
        generateCsvIfNotExists();
    }

    /**
     * Creates new replenishment requests for the provided list of medicines.
     * <p>
     * Skips medicines that already have pending requests.
     * Writes new requests to the CSV file.
     * </p>
     *
     * @param medicines List of medicines for which replenishment requests need to be created.
     * @return A list of newly created `ReplenishmentRequest` objects.
     */
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

    /**
     * Writes a single replenishment request to the CSV file.
     *
     * @param request The `ReplenishmentRequest` object to be written to the CSV file.
     */
    private void writeToCsv(ReplenishmentRequest request) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(csvFile, true))) {
            for (String medicine : request.getMedicines()) {
                String uniqueRequestId = request.getId(); // Generate a unique ID for this medicine
                System.out.println(uniqueRequestId);
                String csvRow = String.format("%s,%s,%s",
                        uniqueRequestId,
                        medicine,
                        request.getStatus().name());
                writer.write(csvRow);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves all replenishment requests from the CSV file.
     * <p>
     * Reads the file, skipping the header line, and parses each row into a `ReplenishmentRequest` object.
     * </p>
     *
     * @return A list of all `ReplenishmentRequest` objects stored in the CSV file.
     */
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

    /**
     * Checks if a replenishment request for a specific medicine already exists in the CSV with status `PENDING`.
     *
     * @param medicineName The name of the medicine to check.
     * @return `true` if a request with `PENDING` status exists; otherwise, `false`.
     */
    private boolean isMedicineAlreadyRequested(String medicineName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFile))) {
            String line;
            // Skip header
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String existingMedicine = parts[1].trim(); // Medicine name
                    String status = parts[2].trim();          // Status
                    if (existingMedicine.equalsIgnoreCase(medicineName)
                            && status.equalsIgnoreCase(StatusEnum.PENDING.name())) {
                        return true; // Medicine already requested with PENDING status
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Retrieves replenishment requests by their status.
     *
     * @param status The status to filter requests by.
     * @return A list of `ReplenishmentRequest` objects matching the specified status.
     */
    public List<ReplenishmentRequest> getRequestsByStatus(StatusEnum status) {
        return getAllRequests().stream()
                .filter(request -> request.getStatus() == status)
                .collect(Collectors.toList());
    }

    /**
     * Parses a single CSV row into a `ReplenishmentRequest` object.
     *
     * @param csvRow The CSV row to parse.
     * @return A `ReplenishmentRequest` object or `null` if the row is invalid.
     */
    private ReplenishmentRequest parseCsvRow(String csvRow) {
        String[] parts = csvRow.split(",");
        if (parts.length < 3) return null;

        String id = parts[0].trim();
        List<String> medicines = List.of(parts[1].split(";")); // Medicines separated by semicolons

        StatusEnum status;
        try {
            status = StatusEnum.valueOf(parts[2].trim());
        } catch (IllegalArgumentException e) {
            status = StatusEnum.PENDING;
            System.out.println("Invalid status value, defaulting to PENDING.");
        }

        return new ReplenishmentRequest(id, medicines, status);
    }

    /**
     * Generates the CSV file with headers if it does not already exist.
     */
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

    /**
     * Approves a replenishment request by changing its status to `APPROVED`.
     *
     * @param requestId The ID of the replenishment request to approve.
     * @return The name of the medicine in the approved request, or `null` if not found.
     */
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
            saveRequestsToCSV(requests);
            System.out.println("Replenishment request approved for: " + medicineName);
            return medicineName;
        } else {
            System.out.println("Request ID not found or already approved.");
            return null;
        }
    }

    /**
     * Rejects a replenishment request by changing its status to `REJECTED`.
     *
     * @param requestId The ID of the replenishment request to reject.
     * @return `true` if the request was successfully rejected; otherwise, `false`.
     */
    public boolean rejectRequest(String requestId) {
        boolean result = updateRequestStatus(requestId, StatusEnum.REJECTED);
        if (!result) {
            System.out.println("Request ID " + requestId + " not found. Rejection failed.");
        }
        return result;
    }

    /**
     * Updates the status of a specific replenishment request by its ID.
     *
     * @param requestId The ID of the request to update.
     * @param newStatus The new status to set.
     * @return `true` if the status was successfully updated; otherwise, `false`.
     */
    public boolean updateRequestStatus(String requestId, StatusEnum newStatus) {
        boolean found = false;

        try {
            List<String> lines = Files.readAllLines(Paths.get(csvFile));
            List<String> updatedLines = new ArrayList<>();

            for (String line : lines) {
                if (line.startsWith(requestId + ",")) {
                    found = true;
                    String[] parts = line.split(",");
                    parts[2] = newStatus.name(); // Update status
                    line = String.join(",", parts);
                }
                updatedLines.add(line);
            }

            if (found) {
                Files.write(Paths.get(csvFile), updatedLines);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return found;
    }

    /**
     * Saves the list of replenishment requests back to the CSV file after updates.
     *
     * @param requests The list of requests to save.
     */
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
