package interfaces;
import models.Inventory;
import java.util.List;

public interface IInventoryService {
    List<Inventory> getInventoryList();
    void updateInventory(Inventory updatedInventory);
    void addMedication(Inventory newMedication);
    boolean removeMedication(String medicineName);
    boolean updateStockLevel(String medicineName, int quantity);
    boolean updateLowStockAlert(String medicineName, int lowStockLevel);
    void submitReplenishmentRequest(String medicineName);
	public boolean approveReplenishmentRequest(String medicineName);
}




