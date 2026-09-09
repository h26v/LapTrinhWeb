package vn.iotstar.service.impl;

import java.util.Date;

import vn.iotstar.dao.IUserDao;
import vn.iotstar.dao.impl.UserDao;
import vn.iotstar.entity.User;
import vn.iotstar.service.IUserService;
import vn.iotstar.service.MailService;
import vn.iotstar.util.OtpUtil;
import vn.iotstar.util.PasswordUtil;

public class UserServiceImpl implements IUserService {
    private final IUserDao userDao = new UserDao();
    private final MailService mailService = new SmtpMailService();

    @Override
    public User findById(int id) {
        return userDao.findById(id);
    }

    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    @Override
    public User findByEmail(String email) {
        if (isBlank(email)) {
            return null;
        }
        return userDao.findByEmail(email.trim());
    }

    @Override
    public boolean usernameExists(String username) {
        return !isBlank(username) && userDao.findByUsername(username.trim()) != null;
    }

    @Override
    public boolean emailExists(String email) {
        return !isBlank(email) && userDao.findByEmail(email.trim()) != null;
    }

    @Override
    public User register(String username, String email, String fullName, String phone, String password)
            throws Exception {
        validateRegister(username, email, password);
        if (usernameExists(username)) {
            throw new IllegalArgumentException("Ten dang nhap da ton tai.");
        }
        if (emailExists(email)) {
            throw new IllegalArgumentException("Email da duoc su dung.");
        }

        String otp = OtpUtil.generateOtp();
        String trimmedEmail = email.trim();
        mailService.sendOtp(trimmedEmail, "Ma OTP kich hoat tai khoan BT3", otp);

        User user = new User();
        user.setUserName(username.trim());
        user.setEmail(trimmedEmail);
        user.setFullName(trim(fullName));
        user.setPhone(trim(phone));
        user.setPassWord(PasswordUtil.hash(password));
        user.setRoleid(2);
        user.setCreatedDate(new Date());
        user.setActive(0);
        user.setActivationOtpHash(PasswordUtil.hash(otp));
        user.setActivationOtpExpiresAt(OtpUtil.expiresAt());
        userDao.insert(user);
        return user;
    }

    @Override
    public void resendActivationOtp(int userId) throws Exception {
        User user = userDao.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("Khong tim thay tai khoan.");
        }
        if (user.getActive() == 1) {
            return;
        }
        String otp = OtpUtil.generateOtp();
        userDao.saveActivationOtp(userId, PasswordUtil.hash(otp), OtpUtil.expiresAt());
        mailService.sendOtp(user.getEmail(), "Ma OTP kich hoat tai khoan BT3", otp);
    }

    @Override
    public boolean verifyActivationOtp(int userId, String otp) {
        User user = userDao.findById(userId);
        if (user == null || user.getActive() == 1 || isBlank(otp)) {
            return false;
        }
        if (OtpUtil.isExpired(user.getActivationOtpExpiresAt())) {
            userDao.clearActivationOtp(userId);
            return false;
        }
        if (!PasswordUtil.matches(otp.trim(), user.getActivationOtpHash())) {
            return false;
        }
        userDao.activateUser(userId);
        return true;
    }

    @Override
    public User login(String username, String password) {
        if (isBlank(username) || password == null) {
            return null;
        }
        User user = userDao.login(username.trim(), password);
        if (user == null || user.getActive() != 1) {
            return null;
        }
        if (!PasswordUtil.matches(password, user.getPassWord())) {
            return null;
        }
        if (!PasswordUtil.isBCrypt(user.getPassWord())) {
            userDao.updatePassword(user.getId(), PasswordUtil.hash(password));
            user = userDao.findById(user.getId());
        }
        return user;
    }

    @Override
    public void requestPasswordReset(String email) throws Exception {
        User user = findByEmail(email);
        if (user == null || user.getActive() != 1) {
            return;
        }
        String otp = OtpUtil.generateOtp();
        userDao.saveResetOtp(user.getId(), PasswordUtil.hash(otp), OtpUtil.expiresAt());
        try {
            mailService.sendOtp(user.getEmail(), "Ma OTP dat lai mat khau BT3", otp);
        } catch (Exception e) {
            userDao.clearResetOtp(user.getId());
            throw e;
        }
    }

    @Override
    public boolean resetPassword(int userId, String otp, String newPassword) {
        if (isBlank(otp) || isBlank(newPassword) || newPassword.length() < 6) {
            return false;
        }
        User user = userDao.findById(userId);
        if (user == null || user.getActive() != 1) {
            return false;
        }
        if (OtpUtil.isExpired(user.getResetOtpExpiresAt())) {
            userDao.clearResetOtp(userId);
            return false;
        }
        if (!PasswordUtil.matches(otp.trim(), user.getResetOtpHash())) {
            return false;
        }
        userDao.updatePassword(userId, PasswordUtil.hash(newPassword));
        userDao.clearResetOtp(userId);
        return true;
    }

    @Override
    public User updateProfile(int id, String fullName, String phone, String avatar) {
        return userDao.updateProfile(id, fullName, phone, avatar);
    }

    private void validateRegister(String username, String email, String password) {
        if (isBlank(username) || username.trim().length() > 100) {
            throw new IllegalArgumentException("Ten dang nhap phai co tu 1 den 100 ky tu.");
        }
        if (isBlank(email) || email.trim().length() > 255
                || !email.trim().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) {
            throw new IllegalArgumentException("Email khong hop le.");
        }
        if (isBlank(password) || password.length() < 6) {
            throw new IllegalArgumentException("Mat khau phai co it nhat 6 ky tu.");
        }
    }

    private String trim(String value) {
        return value == null ? null : value.trim();
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
