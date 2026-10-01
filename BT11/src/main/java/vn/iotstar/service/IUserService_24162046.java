package vn.iotstar.service;

import vn.iotstar.entity.User_24162046;

public interface IUserService_24162046 {

    /** Tra ve user neu dung email + mat khau (va cap nhat last_login), nguoc lai null. */
    User_24162046 login(String email, String rawPassword);

    boolean isEmailRegistered(String email);

    /** Tao user tam (chua luu DB) de cho xac thuc OTP. */
    User_24162046 buildPendingUser(String email, String fullname, Integer phone, String rawPassword);

    /** OTP dung -> luu user vao DB (kich hoat tai khoan). */
    void activate(User_24162046 pendingUser);

    String generateOtp();

    long count();
}
