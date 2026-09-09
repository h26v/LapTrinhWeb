package vn.iotstar.dao;

import java.util.Date;

import vn.iotstar.entity.User;

public interface IUserDao {
    User findById(int id);

    User findByUsername(String username);

    User findByEmail(String email);

    User login(String username, String password);

    void insert(User user);

    void update(User user);

    void updatePassword(int id, String passwordHash);

    void activateUser(int id);

    void saveActivationOtp(int id, String otpHash, Date expiresAt);

    void saveResetOtp(int id, String otpHash, Date expiresAt);

    void clearActivationOtp(int id);

    void clearResetOtp(int id);

    /**
     * Updates only the profile fields supplied by the authenticated user.
     * The returned entity is detached after the DAO closes its EntityManager.
     */
    User updateProfile(int id, String fullName, String phone, String avatar);
}
