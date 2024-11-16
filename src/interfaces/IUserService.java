package interfaces;

import enums.UserRole;

/**
 * Interface for user-related services, including login, password management,
 * and retrieving user roles.
 */
public interface IUserService {

    /**
     * Authenticates a user based on their hospital ID and password.
     *
     * @param hospitalID The unique hospital ID of the user.
     * @param password   The user's password.
     * @return {@code true} if the login is successful; {@code false} otherwise.
     */
    boolean login(String hospitalID, String password);

    /**
     * Changes the password for a user.
     *
     * @param hospitalID   The unique hospital ID of the user.
     * @param oldPassword  The user's current password.
     * @param newPassword  The user's new password.
     * @return {@code true} if the password is successfully changed; {@code false} otherwise.
     */
    boolean changePassword(String hospitalID, String oldPassword, String newPassword);

    /**
     * Retrieves the role of a user based on their hospital ID.
     *
     * @param hospitalID The unique hospital ID of the user.
     * @return The {@link UserRole} of the user (e.g., PATIENT, DOCTOR, PHARMACIST, ADMINISTRATOR).
     */
    UserRole getUserRole(String hospitalID);
}
