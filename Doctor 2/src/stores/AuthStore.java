package stores;

import models.User;
//keeps tracks of the user that is logged in
public class AuthStore {
	
	private static User currentUser;
	//current authenticated user

	private AuthStore() {
	}

	public static void setCurrentUser(User currentUser) {
		AuthStore.currentUser = currentUser;
	}
	//Sets the current user in the AuthStore

	public static boolean isLoggedIn() {
		return currentUser != null;
	}
	//checks if user is logged in

	public static User getCurrentUser() {
		return AuthStore.currentUser;
	}
}
//since the method is static, cannot use this. operator