package models;

public class SubmitReplenishmentRequest {

	    private String medicineName;
	    private int currentStock;
	    private int requestedAmount;

	    public SubmitReplenishmentRequest(String medicineName, int currentStock, int requestedAmount) {
	        this.medicineName = medicineName;
	        this.currentStock = currentStock;
	        this.requestedAmount = requestedAmount;
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

	    public void setRequestedAmount(int requestedAmount) {
	        this.requestedAmount = requestedAmount;
	    }

        public void approve() {
            // TODO Auto-generated method stub
            throw new UnsupportedOperationException("Unimplemented method 'approve'");
        }
	}
