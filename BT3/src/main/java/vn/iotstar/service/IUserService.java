package vn.iotstar.service;

import vn.iotstar.entity.User;

public interface IUserService {
    User findById(int id);

    User findByUsername(String username);

    User findByEmail(String email);

    boolean usernameExists(String username);

    boolean emailExists(String email);

    User register(String username, String email, String fullName, String phone, String password) throws Exception;

    void resendActivationOtp(int userId) throws Exception;

    boolean verifyActivationOtp(int userId, String otp);

    User login(String username, String password);

    void requestPasswordReset(String email) throws Exception;

    boolean resetPassword(int userId, String otp, String newPassword);

    User updateProfile(int id, String fullName, String phone, String avatar);
}
