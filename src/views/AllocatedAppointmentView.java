package views;

import enums.AppointmentStatus;
import interfaces.iPatientView;

import java.time.format.DateTimeFormatter;
import java.util.List;
import models.Appointment;
import models.Patient;
//import services.*;
import services.AppointmentService;
import services.DoctorService;
import services.MedicalRecordService;
import services.UserService;
import services.ScheduleService;
import models.Doctor;

/**
 * Represents a view for displaying allocated appointments for a patient.
 * Implements the iPatientView interface to manage the patient-specific display operations.
 */
public class AllocatedAppointmentView implements iPatientView {
    private final AppointmentService appointmentService;

    /**
     * Constructor for AllocatedAppointmentView.
     *
     * @param appointmentService the AppointmentService instance used to fetch appointment details.
     */
    public AllocatedAppointmentView(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * Displays the details of a given patient.
     *
     * @param patient the Patient object whose details are to be displayed.
     */
    @Override
    public void showPatientDetails(Patient patient) {
        System.out.println("Patient ID: " + patient.getHospitalID());
        System.out.println("Name: " + patient.getName());
        System.out.println("Date of Birth: " + patient.getDateOfBirth());
        System.out.println("Gender: " + patient.getGender());
        System.out.println("Blood Type: " + patient.getBloodType());
        System.out.println("Contact Information: " + patient.getContactInformation());
    }

    /**
     * Displays a success message to the user.
     *
     * @param message the success message to be displayed.
     */
    @Override
    public void showSuccessMessage(String message) {
        System.out.println("SUCCESS: " + message);
    }

    /**
     * Displays an error message to the user.
     *
     * @param message the error message to be displayed.
     */
    @Override
    public void showErrorMessage(String message) {
        System.out.println("ERROR: " + message);
    }

    /**
     * Displays the allocated appointments for a given patient.
     * Filters the appointments to include only pending, confirmed, or canceled appointments.
     *
     * @param patient the Patient object whose allocated appointments are to be displayed.
     */
    @Override
    public void display(Patient patient) {
        List<Appointment> appointments = appointmentService.viewScheduledAppointments();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
        boolean found = false;

        for (Appointment appointment : appointments) {
            if (appointment.getPatientId().equals(patient.getHospitalID()) &&
                    (appointment.getStatus() == enums.AppointmentStatus.PENDING ||
                            appointment.getStatus() == AppointmentStatus.CONFIRMED ||
                            appointment.getStatus() == AppointmentStatus.CANCELLED)) {
                found = true;
                System.out.println("Appointment ID: " + appointment.getAppointmentId());
                System.out.println("Doctor ID: " + appointment.getDoctorId());
                System.out.print("Date & Time: " + appointment.getAppointmentDateTime().format(formatter));
                System.out.println(" HRS");
                System.out.println("Status: " + appointment.getStatus());
                System.out.println("------------------------");
            }
        }

        if (!found) {
            System.out.println("No allocated appointments found for Patient ID: " + patient.getHospitalID());
        }
    }

    /**
     * Displays confirmed appointments for a given patient.
     * Includes additional information about the doctor's name.
     *
     * @param patient the Patient object whose confirmed appointments are to be displayed.
     */
    public void display2(Patient patient) {
        List<Appointment> appointments = appointmentService.viewScheduledAppointments();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm");
        boolean found = false;

        for (Appointment appointment : appointments) {
            UserService userService = new UserService();
            MedicalRecordService medicalRecordService = new MedicalRecordService();
            ScheduleService scheduleService = new services.ScheduleService();
            DoctorService doctorService = new services.DoctorService(userService, scheduleService, medicalRecordService, appointmentService);
            Doctor doctor = doctorService.getDoctorById(appointment.getDoctorId());

            if (appointment.getPatientId().equals(patient.getHospitalID()) &&
                    (appointment.getStatus() == AppointmentStatus.CONFIRMED)) {
                found = true;
                System.out.println("Appointment ID: " + appointment.getAppointmentId());
                System.out.println("Doctor ID: " + appointment.getDoctorId());
                System.out.println("Doctor Name: " + doctor.getName());
                System.out.print("Date & Time: " + appointment.getAppointmentDateTime().format(formatter));
                System.out.println(" HRS");
                System.out.println("Status: " + appointment.getStatus());
                System.out.println("------------------------");
            }
        }

        if (!found) {
            System.out.println("No confirmed appointments found for Patient ID: " + patient.getHospitalID());
        }
    }
}

