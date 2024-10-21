package interfaces;

import models.Appointment;
import models.Pharmacist;
import enums.MedicationStatus;
import models.Patient;
import java.util.List;



public interface IPharmacistView {
    List<Appointment> viewAppointmentOutcomeRecords();
    void updatePrescriptionStatus(String prescriptionId);
    void viewMedicationInventory();
    void submitReplenishmentRequest(String medicationName);

    void submitReplenishmentRequest(Pharmacist pharmacist, String medicineName, int quantity);

    Patient getPatientDetails(String patientId);
    List<Appointment> getAppointmentRecords(String patientId);
    boolean updatePrescriptionStatus(String patientId, String medicineName, MedicationStatus status);
}


