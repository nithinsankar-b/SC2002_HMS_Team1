package stores;
import models.InventoryData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InventoryDataStore {



	    private List<InventoryData> inventoryList;

	    public InventoryDataStore() {
	        this.inventoryList = new ArrayList<>();
	        loadInventoryFromCSV();
	    }

	    private void loadInventoryFromCSV() {
	        String line;
	        try (BufferedReader br = new BufferedReader(new FileReader("inventory.csv"))) {
	            while ((line = br.readLine()) != null) {
	                String[] values = line.split(",");
	                String medicineName = values[0];
	                int currentStock = Integer.parseInt(values[1]);
	                int lowLevelAlert = Integer.parseInt(values[2]);

	                inventoryList.add(new InventoryData(medicineName, currentStock, lowLevelAlert));
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    public List<InventoryData> getInventoryList() {
	        return inventoryList;
	    }

	    public void updateInventory(InventoryData inventoryData) {
	        for (InventoryData data : inventoryList) {
	            if (data.getMedicineName().equalsIgnoreCase(inventoryData.getMedicineName())) {
	                data.setCurrentStock(inventoryData.getCurrentStock());
	                break;
	            }
	        }
	    }
	    public void submitReplenishmentRequest(String medicineName) {
	        // Add logic for submitting a replenishment request
	        System.out.println("Replenishment request submitted for: " + medicineName);
	    }

	}


