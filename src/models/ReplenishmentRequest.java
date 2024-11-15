package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import enums.StatusEnum;

public class ReplenishmentRequest {


	// ReplenishmentRequest.java


	    private String id;
	    private List<String> medicines;
	    private StatusEnum status;
	    private LocalDateTime requestDate;

	    public ReplenishmentRequest(List<String> medicines) {
	        this.id = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmmss"));
	        this.medicines = medicines;
	        this.status = StatusEnum.PENDING;
	        this.requestDate = LocalDateTime.now();
	    }

	    public String getId() {
	        return id;
	    }

	    public List<String> getMedicines() {
	        return medicines;
	    }

	    public StatusEnum getStatus() {
	        return status;
	    }

	    public void setStatus(StatusEnum status) {
	        this.status = status;
	    }
	    
	    public void setId(String id) {
	        this.id=id;
	    }

	    
	    public String toCsvFormat() {
	        // Use a simple format for CSV, making sure all data is properly separated
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"); // Specify your desired date format
	        String formattedDate = requestDate.format(formatter); // Format the request date

	        // Concatenate the medicines using semicolon as delimiter
	        String medicinesConcatenated = String.join(";", medicines); // Change delimiter to ";"

	        // Return a CSV formatted string (columns: ID, Medicines, Status, Request Date)
	        return id + "," + medicinesConcatenated + "," + status + "," + formattedDate;
	    }


	}