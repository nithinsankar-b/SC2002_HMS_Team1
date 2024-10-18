package boundary;

import interfaces.IDoctorView;
import models.Doctor;
import models.Schedule;
import services.DoctorScheduleManagement;
import stores.ScheduleDataStore;

import java.util.List;
import java.util.Scanner;

public class ViewPersonalSchedule implements IDoctorView {

    private DoctorScheduleManagement scheduleManagement;
    private ScheduleDataStore scheduleDataStore;

    public ViewPersonalSchedule(DoctorScheduleManagement scheduleManagement,
                                ScheduleDataStore scheduleDataStore) {
        this.scheduleManagement = scheduleManagement;
        this.scheduleDataStore = scheduleDataStore;
    }

    @Override
    public void displayDoctorView(Object obj) {
        if (!(obj instanceof Doctor)) {
            System.out.println("Invalid object passed to ViewPersonalSchedule.");
            return;
        }

        Doctor doctor = (Doctor) obj;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Personal Schedule ===");
            List<Schedule> schedules = scheduleDataStore.getSchedulesByDoctorId(doctor.getHospitalID());

            for (Schedule schedule : schedules) {
                System.out.println(schedule);
            }

            System.out.println("\nOptions:");
            System.out.println("1. Set Time Slot Available");
            System.out.println("2. Set Time Slot Unavailable");
            System.out.println("3. Return to Doctor Menu");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    setAvailability(doctor, scanner, "Available");
                    break;
                case "2":
                    setAvailability(doctor, scanner, "Blocked");
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void setAvailability(Doctor doctor, Scanner scanner, String status) {
        System.out.print("Enter Time Slot to " + status.toLowerCase() + " (e.g., 09:00-10:00): ");
        String timeSlot = scanner.nextLine().trim();
        System.out.print("Enter Day to " + status.toLowerCase() + " (Monday, Tuesday, ...): ");
        String day = scanner.nextLine().trim();

        scheduleManagement.setAvailable(doctor.getHospitalID(), timeSlot, day);
    }
}
