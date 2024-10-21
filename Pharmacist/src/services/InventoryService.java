package services;
import interfaces.IInventoryService;
import models.Inventory;
import models.SubmitReplenishmentRequest;
import stores.InventoryDataStore;

import java.util.List;

public class InventoryService implements IInventoryService{


	    private final InventoryDataStore InventoryDataStore;

	    public InventoryService(InventoryDataStore store) {
	        this.InventoryDataStore = store;
	    }

	    @Override
	    public void submitReplenishmentRequest(String medicineName) {
	        List<Inventory> inventoryDataList = InventoryDataStore.getInventoryList();

	        for (Inventory data : inventoryDataList) {
	            if (data.getMedicineName().equalsIgnoreCase(medicineName)) {
	                if (data.getCurrentStock() <= data.getLowLevelAlert()) {
	                    SubmitReplenishmentRequest request = new SubmitReplenishmentRequest(
	                            data.getMedicineName(),
	                            data.getCurrentStock(),
	                            calculateReplenishmentAmount(data.getCurrentStock(), data.getLowLevelAlert())
	                    , data.getLowLevelAlert());
	                    sendReplenishmentRequest(request);
	                } else {
	                    System.out.println("Stock level is sufficient for: " + medicineName);
	                }
	                break;
	            }
	        }
	    }

	    private int calculateReplenishmentAmount(int currentStock, int lowLevelAlert) {
	        // Logic to determine how much to replenish (e.g., add enough to double the stock)
	        return (lowLevelAlert * 2) - currentStock;
	    }

	    private void sendReplenishmentRequest(SubmitReplenishmentRequest request) {
	        // Logic to send the replenishment request (e.g., send to admin or trigger an API call)
	        System.out.println("Replenishment Request Submitted for: " + request.getMedicineName() +
	                " | Current Stock: " + request.getCurrentStock() + " | Requested Amount: " + request.getRequestedAmount());
	    }
	}

	

