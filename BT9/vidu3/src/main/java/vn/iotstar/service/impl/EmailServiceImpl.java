package vn.iotstar.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import vn.iotstar.service.EmailService;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendOtp(String email, String otp, String subject) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(subject);
        message.setText("""
                Xin chao,

                Ma OTP cua ban la: %s

                OTP co hieu luc trong 5 phut va chi su dung mot lan.
                Khong chia se ma nay cho nguoi khac.
                """.formatted(otp));
        mailSender.send(message);
    }
}
