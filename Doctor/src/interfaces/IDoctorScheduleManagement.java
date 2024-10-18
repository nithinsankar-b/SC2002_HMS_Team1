package interfaces;

public interface IDoctorScheduleManagement {
    void setAvailable(String doctorId, String timeSlot, String day);
    void setUnavailable(String doctorId, String timeSlot, String day);
    void acceptAppointmentRequest(String requestId);
    void declineAppointmentRequest(String requestId);
}
