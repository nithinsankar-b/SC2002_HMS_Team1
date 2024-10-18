package interfaces;

import models.Doctor;
import java.util.List;

public interface IDoctorService {
    List<Doctor> loadDoctorsFromCsv(String filePath);
    void saveDoctorsToCsv(List<Doctor> doctors, String filePath);
}

/*package interfaces;

import java.util.Map;
import models.Doctor;

public interface IDoctorDataService {
	Map<String, Doctor> importDoctorData(String doctorFilePath);
	boolean exportDoctorData(String doctorFilePath, Map<String, Doctor> doctorMap);
}*/
