package services;

import java.util.Map;


import models.Doctor;
import stores.AuthStore;
import stores.DoctorDataStore;

/**
 * The {@link AuthDoctorService} class extends {@link AuthService} and 
 * provides the login functionality for doctors.
 */
public class AuthDoctorService extends AuthService {
	
	public AuthDoctorService() {
		super();
	}
    @Override
    public boolean login(String hospitalID, String password) {
        Map<String, Doctor> doctorData = DoctorDataStore.getDoctorsData();

        Doctor doc = doctorData.get(hospitalID);

        if (authenticate(doc, password) == false)
            return false;

        AuthStore.setCurrentUser(doc);
        return true;
    }
    //NO NEED TO GET THE HOSPITALID AS PARAMETER 
    //This is because actually the object doc in this case is being retrieved from the map via the hospitalID
    //so if it does not exist--> object = null therefore false will be returned.If it exists but password wrong then false again.
    //so this way both hospitalID and password is verified 
}
