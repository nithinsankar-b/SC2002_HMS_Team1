package stores;

import models.Schedule;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ScheduleDataStore {

    private String scheduleFilePath;
    private List<Schedule> schedules;

    public ScheduleDataStore(String scheduleFilePath) {
        this.scheduleFilePath = scheduleFilePath;
        this.schedules = loadSchedules();
    }

    private List<Schedule> loadSchedules() {
        List<Schedule> loadedSchedules = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(scheduleFilePath))) {
            String header = br.readLine(); // Skip header
            String line;
            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length == 9) {
                    String doctorId = fields[0].trim();
                    String timeSlot = fields[1].trim();
                    String monday = fields[2].trim();
                    String tuesday = fields[3].trim();
                    String wednesday = fields[4].trim();
                    String thursday = fields[5].trim();
                    String friday = fields[6].trim();
                    String saturday = fields[7].trim();
                    String sunday = fields[8].trim();

                    Schedule schedule = new Schedule(doctorId, timeSlot, monday, tuesday, wednesday,
                            thursday, friday, saturday, sunday);
                    loadedSchedules.add(schedule);
                } else {
                    System.err.println("Invalid schedule entry: " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading schedules: " + e.getMessage());
        }
        return loadedSchedules;
    }

    public List<Schedule> getSchedulesByDoctorId(String doctorId) {
        return schedules.stream()
                .filter(schedule -> schedule.getDoctorId().equalsIgnoreCase(doctorId))
                .collect(Collectors.toList());
    }

    public void updateSchedule(Schedule updatedSchedule) {
        for (int i = 0; i < schedules.size(); i++) {
            Schedule schedule = schedules.get(i);
            if (schedule.getDoctorId().equalsIgnoreCase(updatedSchedule.getDoctorId()) &&
                schedule.getTimeSlot().equalsIgnoreCase(updatedSchedule.getTimeSlot())) {
                schedules.set(i, updatedSchedule);
                saveSchedules();
                return;
            }
        }
        System.out.println("Schedule not found for update.");
    }

    private void saveSchedules() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(scheduleFilePath))) {
            // Write header
            bw.write("DoctorID,Time Slot,Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday");
            bw.newLine();
            for (Schedule schedule : schedules) {
                String line = String.join(",",
                        schedule.getDoctorId(),
                        schedule.getTimeSlot(),
                        schedule.getMonday(),
                        schedule.getTuesday(),
                        schedule.getWednesday(),
                        schedule.getThursday(),
                        schedule.getFriday(),
                        schedule.getSaturday(),
                        schedule.getSunday());
                bw.write(line);
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving schedules: " + e.getMessage());
        }
    }
}
