package services;

import java.util.Map;

import models.Pharmacist;
import stores.AuthStore;
import stores.PharmacistDataStore;

/**
 * The {@link AuthPharmacistService} class extends {@link AuthService} and 
 * provides the login functionality for doctors.
 */
public class AuthPharmacistService extends AuthService {
	
	public AuthPharmacistService() {
		super();
	}
    @Override
    public boolean login(String hospitalID, String password) {
        Map<String, Pharmacist> pharmacistData = PharmacistDataStore.getPharmacistData();

        Pharmacist doc = pharmacistData.get(hospitalID);

        if (authenticate(doc, password) == false)
            return false;

        AuthStore.setCurrentUser(doc);
        return true;
    }

}
