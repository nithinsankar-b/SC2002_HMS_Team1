package models;

public class SubmitReplenishmentRequest {

	    private String medicineName;
	    private int currentStock;
	    private int requestedAmount;
	    private int lowLevelAlert;

	    public SubmitReplenishmentRequest(String medicineName, int currentStock, int requestedAmount, int lowLevelAlert) {
	        this.medicineName = medicineName;
	        this.currentStock = currentStock;
	        this.requestedAmount = requestedAmount;
	        this.lowLevelAlert = lowLevelAlert;
	    }

	    // Getters and Setters
	    public String getMedicineName() {
	        return medicineName;
	    }

	    public void setMedicineName(String medicineName) {
	        this.medicineName = medicineName;
	    }

	    public int getCurrentStock() {
	        return currentStock;
	    }

	    public void setCurrentStock(int currentStock) {
	        this.currentStock = currentStock;
	    }

	    public int getRequestedAmount() {
	        return requestedAmount;
	    }
	    
	    public int getLowLevelAlert() {
	        return lowLevelAlert;
	    }

	    public void setRequestedAmount(int requestedAmount) {
	        this.requestedAmount = requestedAmount;
	    }
	}

	


