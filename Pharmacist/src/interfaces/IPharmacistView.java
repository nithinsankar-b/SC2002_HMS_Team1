package interfaces;

import models.AppointmentOutcomeRecord;
import models.Pharmacist;
import enums.PrescriptionStatus;
import models.Patient;
import java.util.List;



public interface IPharmacistView {
    List<AppointmentOutcomeRecord> viewAppointmentOutcomeRecords();
    void updatePrescriptionStatus(String prescriptionId);
    void viewMedicationInventory();
    void submitReplenishmentRequest(String medicationName);

    void submitReplenishmentRequest(Pharmacist pharmacist, String medicineName, int quantity);

    Patient getPatientDetails(String patientId);
    List<AppointmentOutcomeRecord> getAppointmentRecords(String patientId);
    boolean updatePrescriptionStatus(String patientId, String medicineName, PrescriptionStatus status);
}


