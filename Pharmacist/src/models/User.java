package models;

import enums.UserRole;

public class User {
	
	private String hospitalID;
	private String password;
	private String name;
	private UserRole role;

	public User(String hospitalID,String name, String password) {
		this.hospitalID = hospitalID;
		this.name = name;
		this.password = password;
	}

	public String getHospitalID() {
		return this.hospitalID;
	}

	public String getPassword() {
		return this.password;
	}
	
	public String getName() {
		return this.name;
	}

	public UserRole getRole() {
		return this.role;
	}


	public Boolean setRole(UserRole role) {
		this.role = role;
		return true;
	}

	public Boolean setPassword(String oldPassword, String newPassword) {
		if (!oldPassword.equals(this.password))
			return false;
		this.password = newPassword;
		return true;
	}
}