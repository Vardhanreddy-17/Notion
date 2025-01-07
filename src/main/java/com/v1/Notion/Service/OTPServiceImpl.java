package com.v1.Notion.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.v1.Notion.Model.OTP;
import com.v1.Notion.Repository.OTPRepository;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class OTPServiceImpl implements OTPService{
	@Autowired
	private OTPRepository otpRepository;
	
	 @Value("${spring.mail.host}")
	    private String mailHost;
	    
	    @Value("${spring.mail.port}")
	    private int mailPort;
	    
	    @Value("${spring.mail.username}")
	    private String mailUsername;
	    
	    @Value("${spring.mail.password}")
	    private String mailPassword;
	    
	    
	public String otpGenerate() {
		return String.valueOf(new Random().nextInt(900000) + 100000);
	}
	@Override
	public String sendOTP(String email) {
		String otp = otpGenerate();
		if(otpRepository.findEmail(email).isPresent()) {
			throw new IllegalArgumentException("User already registered");
		}
		while(otpRepository.findOTP(otp).isPresent()) {
			otp  = otpGenerate();
		}
	    OTP otpEntity = new OTP();
        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setCreatedAt(LocalDateTime.now());
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otpRepository.save(otpEntity);
        try {
            sendWelcomeEmail(email,otp);
        } catch (Exception e) {
            return email.toString();
        }
        return otp;
	}
	private void sendWelcomeEmail(String toEmail,String otp) throws MessagingException, IOException {
        // Read the email template from the file system
        String emailContent = new String(Files.readAllBytes(Paths.get("")));

        // Replace placeholders with dynamic data
        emailContent = emailContent.replace("{{email}}", toEmail)
                                   .replace("{{otp}}", otp);

        // Email properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", mailHost);
        properties.put("mail.smtp.port", mailPort);

        // Create a session with authentication
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailUsername, mailPassword);
            }
        });

        // Construct the email message
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(mailUsername));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
        message.setSubject("Your OTP for Study Notion Verification");

        // Set the content of the message as HTML
        message.setContent(emailContent, "text/html");

        // Send the email
        Transport.send(message);
    }
}
