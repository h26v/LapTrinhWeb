package vn.iotstar.service;

public interface MailService {
    void sendOtp(String to, String subject, String otp) throws Exception;
}
