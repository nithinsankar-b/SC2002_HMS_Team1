package enums;

/**
 * Enumeration representing the status of medical inventory.
 * This enum is used to indicate whether an inventory item is in stock or has low stock.
 */
public enum InventoryStatus {
    
    /**
     * Indicates that the inventory item is in stock.
     */
    INSTOCK,

    /**
     * Indicates that the inventory item has low stock and may need replenishment soon.
     */
    LOWSTOCK
}
