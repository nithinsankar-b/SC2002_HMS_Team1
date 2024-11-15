package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import enums.StatusEnum;

public class ReplenishmentRequest {

    private String id;
    private List<String> medicines;
    private StatusEnum status;
    private LocalDateTime requestDate;

    // Constructor for new requests (generates new ID)
    public ReplenishmentRequest(List<String> medicines) {
        this.id = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMddHHmmss"));
        this.medicines = medicines;
        this.status = StatusEnum.PENDING;
        this.requestDate = LocalDateTime.now();
    }

    // Constructor for loading from CSV (uses existing ID)
    public ReplenishmentRequest(String id, List<String> medicines, StatusEnum status) {
        this.id = id;
        this.medicines = medicines;
        this.status = status;
        this.requestDate = LocalDateTime.now(); // Adjust as necessary
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
        this.id = id;
    }

    public String toCsvFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedDate = requestDate.format(formatter);
        String medicinesConcatenated = String.join(";", medicines);

        return id + "," + medicinesConcatenated + "," + status + "," + formattedDate;
    }
}

