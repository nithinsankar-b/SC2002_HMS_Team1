package services;

import interfaces.IAppointmentService;

import models.AppointmentRequest;
import stores.AppointmentRequestDataStore;
import stores.ScheduleDataStore;
import models.Schedule;

import java.util.List;

public class AppointmentService implements IAppointmentService {
    private final AppointmentRequestDataStore appointmentRequestStore;
    private final ScheduleDataStore scheduleDataStore;

    public AppointmentService(AppointmentRequestDataStore store, ScheduleDataStore scheduleStore) {
        this.appointmentRequestStore = store;
        this.scheduleDataStore = scheduleStore;
    }

    @Override
    public List<AppointmentRequest> getPendingRequestsForDoctor(String doctorId) {
        return appointmentRequestStore.getPendingRequestsForDoctorCurrentWeek(doctorId);
    }

    @Override
    public void acceptAppointmentRequest(String requestId) {
        AppointmentRequest request = appointmentRequestStore.getAppointmentRequestById(requestId);
        if (request != null && request.getStatus().equalsIgnoreCase("Pending")) {
            // Update the request status to "Accepted"
            request.setStatus("Accepted");
            appointmentRequestStore.updateAppointmentRequest(request);
            System.out.println("Appointment Request " + requestId + " has been accepted.");

            // Update the schedule with the patient ID
            updateScheduleWithPatient(request);
        } else {
            System.out.println("Appointment Request not found or already processed.");
        }
    }

    @Override
    public void declineAppointmentRequest(String requestId) {
        AppointmentRequest request = appointmentRequestStore.getAppointmentRequestById(requestId);
        if (request != null && request.getStatus().equalsIgnoreCase("Pending")) {
            // Update the request status to "Declined"
            request.setStatus("Declined");
            appointmentRequestStore.updateAppointmentRequest(request);
            System.out.println("Appointment Request " + requestId + " has been declined.");
        } else {
            System.out.println("Appointment Request not found or already processed.");
        }
    }
    /**
     * Retrieves upcoming appointments for a specific doctor.
     *
     * @param doctorId The ID of the doctor.
     * @return A list of AppointmentRequest objects for the upcoming week.
     */
    public List<AppointmentRequest> viewUpcomingAppointments(String doctorId) {
        return appointmentRequestStore.getUpcomingAppointmentsForDoctor(doctorId);
    }

    /**
     * Updates the schedule by setting the patient ID in the corresponding time slot.
     *
     * @param request The accepted AppointmentRequest.
     */
    private void updateScheduleWithPatient(AppointmentRequest request) {
        String doctorId = request.getDoctorId();
        String timeSlot = request.getRequestedTimeSlot();
        String dayOfWeek = request.getRequestedDate().getDayOfWeek().toString(); // e.g., MONDAY

        // Convert dayOfWeek to lowercase to match the schedule fields
        dayOfWeek = dayOfWeek.toLowerCase();

        // Fetch the schedule for the doctor
        List<Schedule> schedules = scheduleDataStore.getSchedulesByDoctorId(doctorId);

        boolean slotFound = false;
        for (Schedule schedule : schedules) {
            if (schedule.getTimeSlot().equalsIgnoreCase(timeSlot)) {
                // Set the patient ID in the specific day field
                switch (dayOfWeek) {
                    case "monday":
                        schedule.setMonday(request.getPatientId());
                        break;
                    case "tuesday":
                        schedule.setTuesday(request.getPatientId());
                        break;
                    case "wednesday":
                        schedule.setWednesday(request.getPatientId());
                        break;
                    case "thursday":
                        schedule.setThursday(request.getPatientId());
                        break;
                    case "friday":
                        schedule.setFriday(request.getPatientId());
                        break;
                    case "saturday":
                        schedule.setSaturday(request.getPatientId());
                        break;
                    case "sunday":
                        schedule.setSunday(request.getPatientId());
                        break;
                    default:
                        System.out.println("Invalid day of week.");
                        return;
                }
                // Update the schedule in the data store
                scheduleDataStore.updateSchedule(schedule);
                System.out.println("Schedule updated: " + timeSlot + " on " + dayOfWeek + " set to Patient ID " + request.getPatientId());
                slotFound = true;
                break;
            }
        }

        if (!slotFound) {
            System.out.println("Time slot not found in the schedule. Unable to update.");
        }
    }
}
