package services;

import interfaces.IAuthService;
import models.User;
import stores.AuthStore;

//It provides basic authentication functionalities for user login and logout.

public abstract class AuthService implements IAuthService {
    public abstract boolean login(String userID, String password);


    public AuthService() {
	}
   
    @Override
    public boolean logout() {
        AuthStore.setCurrentUser(null);
        return true;
    };
    // Upon logout, current user is set to null

    protected boolean authenticate(User user, String password) {
        if (user == null)
            return false;
        if (!user.getPassword().equals(password))
            return false;
        return true;
    }
    

 
}
