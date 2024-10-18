package models;

public class InventoryData {

	    private String medicineName;
	    private int currentStock;
	    private int lowLevelAlert;

	    public InventoryData(String medicineName, int currentStock, int lowLevelAlert) {
	        this.medicineName = medicineName;
	        this.currentStock = currentStock;
	        this.lowLevelAlert = lowLevelAlert;
	    }

	    // Getters and setters
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

	    public int getLowLevelAlert() {
	        return lowLevelAlert;
	    }

	    public void setLowLevelAlert(int lowLevelAlert) {
	        this.lowLevelAlert = lowLevelAlert;
	    }
	}



