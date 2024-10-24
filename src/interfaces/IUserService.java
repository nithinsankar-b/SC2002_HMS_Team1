package interfaces;

import enums.UserRole;

public interface IUserService {
    boolean login(String hospitalID, String password);
    boolean changePassword(String hospitalID, String oldPassword, String newPassword);
    UserRole getUserRole(String hospitalID);
}