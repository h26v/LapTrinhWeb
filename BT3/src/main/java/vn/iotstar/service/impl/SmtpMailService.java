package vn.iotstar.service.impl;

import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import vn.iotstar.service.MailService;
import vn.iotstar.util.OtpUtil;

public class SmtpMailService implements MailService {

    @Override
    public void sendOtp(String to, String subject, String otp) throws Exception {
        String host = config("BT3_SMTP_HOST", "bt3.smtp.host");
        String port = config("BT3_SMTP_PORT", "bt3.smtp.port", "587");
        String username = config("BT3_SMTP_USERNAME", "bt3.smtp.username");
        String password = config("BT3_SMTP_PASSWORD", "bt3.smtp.password");
        String from = config("BT3_SMTP_FROM", "bt3.smtp.from", username);
        String starttls = config("BT3_SMTP_STARTTLS", "bt3.smtp.starttls", "true");

        if (isBlank(host) || isBlank(username) || isBlank(password) || isBlank(from)) {
            throw new IllegalStateException("Chua cau hinh SMTP de gui email OTP.");
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", starttls);
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText("Ma OTP cua ban la: " + otp + "\nMa co hieu luc trong "
                    + OtpUtil.OTP_MINUTES + " phut. Khong chia se ma nay cho nguoi khac.");
            Transport.send(message);
        } catch (MessagingException e) {
            throw new Exception("Khong the gui email OTP. Vui long kiem tra cau hinh SMTP.", e);
        }
    }

    private String config(String envName, String propertyName) {
        return config(envName, propertyName, null);
    }

    private String config(String envName, String propertyName, String defaultValue) {
        String value = System.getenv(envName);
        if (!isBlank(value)) {
            return value.trim();
        }
        value = System.getProperty(propertyName);
        if (!isBlank(value)) {
            return value.trim();
        }
        return defaultValue;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
