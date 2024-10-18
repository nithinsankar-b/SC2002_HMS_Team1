package services;

import interfaces.IDoctorScheduleManagement;
import models.AppointmentRequest;
import models.Schedule;
import stores.AppointmentRequestDataStore;
import stores.ScheduleDataStore;

import java.util.List;

public class DoctorScheduleManagement implements IDoctorScheduleManagement {

    private ScheduleDataStore scheduleDataStore;
    private AppointmentRequestDataStore appointmentRequestDataStore;

    public DoctorScheduleManagement(ScheduleDataStore scheduleDataStore,
                                    AppointmentRequestDataStore appointmentRequestDataStore) {
        this.scheduleDataStore = scheduleDataStore;
        this.appointmentRequestDataStore = appointmentRequestDataStore;
    }

    @Override
    public void setAvailable(String doctorId, String timeSlot, String day) {
        List<Schedule> schedules = scheduleDataStore.getSchedulesByDoctorId(doctorId);
        for (Schedule schedule : schedules) {
            if (schedule.getTimeSlot().equalsIgnoreCase(timeSlot)) {
                setAvailability(schedule, day, "Available");
                scheduleDataStore.updateSchedule(schedule);
                System.out.println("Set " + timeSlot + " on " + day + " to Available.");
                return;
            }
        }
        System.out.println("Time slot not found.");
    }

    @Override
    public void setUnavailable(String doctorId, String timeSlot, String day) {
        List<Schedule> schedules = scheduleDataStore.getSchedulesByDoctorId(doctorId);
        for (Schedule schedule : schedules) {
            if (schedule.getTimeSlot().equalsIgnoreCase(timeSlot)) {
                setAvailability(schedule, day, "Blocked");
                scheduleDataStore.updateSchedule(schedule);
                System.out.println("Set " + timeSlot + " on " + day + " to Blocked.");
                return;
            }
        }
        System.out.println("Time slot not found.");
    }

    @Override
    public void acceptAppointmentRequest(String requestId) {
        AppointmentRequest request = appointmentRequestDataStore.getAppointmentRequestById(requestId);
        if (request != null && request.getStatus().equalsIgnoreCase("Pending")) {
            request.setStatus("Accepted");
            appointmentRequestDataStore.updateAppointmentRequest(request);
            System.out.println("Appointment Request " + requestId + " accepted.");
            // Optionally, create a confirmed appointment entry here
        } else {
            System.out.println("Appointment Request not found or already processed.");
        }
    }

    @Override
    public void declineAppointmentRequest(String requestId) {
        AppointmentRequest request = appointmentRequestDataStore.getAppointmentRequestById(requestId);
        if (request != null && request.getStatus().equalsIgnoreCase("Pending")) {
            request.setStatus("Declined");
            appointmentRequestDataStore.updateAppointmentRequest(request);
            System.out.println("Appointment Request " + requestId + " declined.");
        } else {
            System.out.println("Appointment Request not found or already processed.");
        }
    }

    private void setAvailability(Schedule schedule, String day, String status) {
        switch (day.toLowerCase()) {
            case "monday":
                schedule.setMonday(status);
                break;
            case "tuesday":
                schedule.setTuesday(status);
                break;
            case "wednesday":
                schedule.setWednesday(status);
                break;
            case "thursday":
                schedule.setThursday(status);
                break;
            case "friday":
                schedule.setFriday(status);
                break;
            case "saturday":
                schedule.setSaturday(status);
                break;
            case "sunday":
                schedule.setSunday(status);
                break;
            default:
                System.out.println("Invalid day provided.");
        }
    }
}
