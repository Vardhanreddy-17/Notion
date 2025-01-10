package com.v1.Notion.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties.UiService;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.v1.Notion.Model.*;
import com.v1.Notion.Repository.OTPRepository;
import com.v1.Notion.Repository.UserRepository;

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
	
	@Autowired
	private UserRepository userRepository;
	
//	 @Value("${spring.mail.host}")
//	    private String mailHost;
//	    
//	    @Value("${spring.mail.port}")
//	    private int mailPort;
//	    
//	    @Value("${spring.mail.username}")
//	    private String mailUsername;
//	    
//	    @Value("${spring.mail.password}")
//	    private String mailPassword;
	    
	    @Autowired
	    private JavaMailSender javaMailSender;
	    
	public String otpGenerate() {
		return String.valueOf(new Random().nextInt(900000) + 100000);
	}
	@Override
	public String sendOTP(String email) {
		String otp = otpGenerate();
		Optional<com.v1.Notion.Model.User> existingUser = userRepository.findByEmail(email);
		if(existingUser.isPresent()) {
			com.v1.Notion.Model.User user = existingUser.get();
			if(user.getApproved()==false){
				otp = otpGenerate();
			}else {
				throw new IllegalArgumentException("User already registered");
			}
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
            return e.toString();
        }
        return otp;
	}
	public void sendWelcomeEmail(String toEmail, String otp) throws MessagingException, IOException, URISyntaxException {
        // Read the HTML template from the file system
		String emailContent = new String(Files.readAllBytes(Paths.get(getClass().getClassLoader().getResource("templates/Verificationmail.html").toURI())));


        // Replace placeholders with dynamic data
        emailContent = emailContent.replace("{{email}}", toEmail)
                                   .replace("{{otp}}", otp);

        // Create a MimeMessage
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        // Set email properties
        helper.setTo(toEmail);
        helper.setSubject("Your OTP for Study Notion Verification");
        helper.setText(emailContent, true); // Mark the content as HTML

        // Send the email
        javaMailSender.send(mimeMessage);
    }
}
