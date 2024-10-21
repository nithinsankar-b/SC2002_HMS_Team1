package stores;
import models.Inventory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InventoryDataStore {



	    private List<Inventory> inventoryList;

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

	                inventoryList.add(new Inventory(medicineName, currentStock, lowLevelAlert));
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }

	    public List<Inventory> getInventoryList() {
	        return inventoryList;
	    }

	    public void updateInventory(Inventory inventoryData) {
	        for (Inventory data : inventoryList) {
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


