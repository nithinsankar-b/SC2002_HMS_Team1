// File: services/AppointmentService.java
package services;

import enums.AppointmentStatus;
import interfaces.IAppointmentService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import models.Appointment;
import models.Medication;

public class AppointmentService implements IAppointmentService { // Implement the interface
    private final List<Appointment> appointments = new ArrayList<>(); // Consider thread safety if used in multi-threaded environments

    @Override
    public boolean scheduleAppointment(Appointment appointment) {
        return appointments.add(appointment);
    }

    @Override
    public boolean cancelAppointment(String appointmentId) {
        return appointments.removeIf(appointment -> appointment.getAppointmentId().equals(appointmentId));
    }

    @Override
    public boolean rescheduleAppointment(String appointmentId, Appointment newAppointment) {
        for (int i = 0; i < appointments.size(); i++) {
            if (appointments.get(i).getAppointmentId().equals(appointmentId)) {
                appointments.set(i, newAppointment);
                return true; // Appointment rescheduled
            }
        }
        return false; // Appointment not found
    }

    @Override
    public List<Appointment> viewScheduledAppointments() {
        return appointments;
    }

    @Override
    public Appointment getAppointment(String appointmentId) {
        for (Appointment appointment : appointments) {
            if (appointment.getAppointmentId().equals(appointmentId)) {
                return appointment;
            }
        }
        return null; // Appointment not found
    }

    @Override
    public void recordAppointmentOutcome(String appointmentId, String serviceProvided, List<Medication> prescribedMedications, String consultationNotes) {
        Appointment appointment = getAppointment(appointmentId);
        if (appointment != null && appointment.getStatus() == AppointmentStatus.PENDING) {
            appointment.setStatus(AppointmentStatus.COMPLETED); // Change status to completed
            appointment.setAppointmentOutcomeDate(LocalDateTime.now()); // Record date of outcome
            appointment.setServiceProvided(serviceProvided);
            appointment.getMedications().clear(); // Clear existing medications
            prescribedMedications.forEach(appointment::addMedication); // Add new prescribed medications
            appointment.setConsultationNotes(consultationNotes); // Set consultation notes
        }
    }

    @Override
    public List<LocalDateTime> getAvailableSlots(String doctorId, LocalDate date) {
        List<LocalDateTime> availableSlots = new ArrayList<>();
        
        // Assuming working hours for doctors: 0900 to 1700HRS
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(17, 0);

        // Create slots at 30-minute intervals between start and end time
        LocalDateTime currentSlot = LocalDateTime.of(date, startTime);
        LocalDateTime endSlot = LocalDateTime.of(date, endTime);

        while (currentSlot.isBefore(endSlot)) {
            boolean slotTaken = false;
            
            // Check if the slot is already booked by any existing appointment for the given doctor
            for (Appointment appointment : appointments) {
                if (appointment.getDoctorId().equals(doctorId) && 
                    appointment.getAppointmentDateTime().isEqual(currentSlot)) {
                    slotTaken = true;
                    break;
                }
            }

            if (!slotTaken) {
                availableSlots.add(currentSlot);
            }

            currentSlot = currentSlot.plusMinutes(30); // Move to the next 30-minute slot
        }

        return availableSlots;
    }
}
