package vn.iotstar.service.impl;

import vn.iotstar.entity.User_24162046;
import vn.iotstar.repository.IUserRepository_24162046;
import vn.iotstar.repository.impl.UserRepository_24162046;
import vn.iotstar.service.IUserService_24162046;
import vn.iotstar.util.PasswordUtil_24162046;

import java.security.SecureRandom;
import java.time.LocalDateTime;

public class UserService_24162046 implements IUserService_24162046 {

    private static final SecureRandom RANDOM = new SecureRandom();

    private final IUserRepository_24162046 userRepository = new UserRepository_24162046();

    @Override
    public User_24162046 login(String email, String rawPassword) {
        if (email == null || rawPassword == null) {
            return null;
        }
        User_24162046 user = userRepository.findByEmail(email.trim());
        if (user == null || !PasswordUtil_24162046.matches(rawPassword, user.getPasswd())) {
            return null;
        }
        user.setLastLogin(LocalDateTime.now());
        userRepository.update(user);
        return user;
    }

    @Override
    public boolean isEmailRegistered(String email) {
        return userRepository.findByEmail(email) != null;
    }

    @Override
    public User_24162046 buildPendingUser(String email, String fullname, Integer phone, String rawPassword) {
        User_24162046 user = new User_24162046();
        user.setEmail(email);
        user.setFullname(fullname);
        user.setPhone(phone);
        user.setPasswd(PasswordUtil_24162046.hash(rawPassword));
        user.setIsAdmin(false);
        return user;
    }

    @Override
    public void activate(User_24162046 pendingUser) {
        if (isEmailRegistered(pendingUser.getEmail())) {
            throw new IllegalStateException("Email đã được đăng ký");
        }
        pendingUser.setSignupDate(LocalDateTime.now());
        userRepository.insert(pendingUser);
    }

    @Override
    public String generateOtp() {
        return String.valueOf(100000 + RANDOM.nextInt(900000));
    }

    @Override
    public long count() {
        return userRepository.count();
    }
}
