/*package interfaces;

import java.util.Map;
import models.Pharmacist;

public interface IPharmacistDataService {
	Map<String, Pharmacist> importDoctorData(String doctorFilePath);
	boolean exportDoctorData(String doctorFilePath, Map<String, Pharmacist> doctorMap);
}*/
package interfaces;

import models.Pharmacist;
import java.util.List;

public interface IPharmacistService {
    List<Pharmacist> loadPharmacistsFromCsv(String filePath);
    void savePharmacistsToCsv(List<Pharmacist> pharmacists, String filePath);
}

