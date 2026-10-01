package vn.iotstar.service.impl;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import vn.iotstar.service.IEmailService_24162046;
import vn.iotstar.util.Constant_24162046;

import java.util.Properties;

public class EmailService_24162046 implements IEmailService_24162046 {

    @Override
    public boolean sendOtp(String toEmail, String otp) {
        // In ra console de van test duoc khi chua cau hinh Gmail
        System.out.println("[OTP] " + toEmail + " -> " + otp);

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Constant_24162046.MAIL_FROM, Constant_24162046.MAIL_APP_PASSWORD);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Constant_24162046.MAIL_FROM));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("BookStore - Mã OTP kích hoạt tài khoản");
            message.setContent("<h2>Mã OTP kích hoạt tài khoản của bạn:</h2>"
                    + "<h1 style='color:#0d6efd'>" + otp + "</h1>"
                    + "<p>Mã có hiệu lực trong " + Constant_24162046.OTP_EXPIRE_MINUTES + " phút.</p>",
                    "text/html; charset=utf-8");
            Transport.send(message);
            return true;
        } catch (MessagingException e) {
            System.err.println("[OTP] Gui mail that bai: " + e.getMessage());
            return false;
        }
    }
}
