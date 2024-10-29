package models;

import enums.InventoryStatus;
import enums.ReplenishmentStatus;

public class Inventory {

	    private String medicineName;
	    private int currentStock;
	    private int lowLevelAlert;
	    private ReplenishmentStatus replenishmentStatus;
	    private InventoryStatus inventoryStatus;

	    public Inventory(String medicineName, int currentStock, int lowLevelAlert, ReplenishmentStatus replenishmentStatus) {
	        this.medicineName = medicineName;
	        this.currentStock = currentStock;
	        this.lowLevelAlert = lowLevelAlert;
	        this.replenishmentStatus = replenishmentStatus;
	        this.inventoryStatus = inventoryStatus;
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
	    
	    public ReplenishmentStatus getReplenishmentStatus() {
	        return replenishmentStatus;
	    }

	    public void setReplenishmentStatus(ReplenishmentStatus replenishmentStatus) {
	        this.replenishmentStatus = replenishmentStatus;
	    }
	    
	    public void setInventoryStatus(InventoryStatus inventoryStatus) {
	        this.inventoryStatus = inventoryStatus;
	    }
	}


