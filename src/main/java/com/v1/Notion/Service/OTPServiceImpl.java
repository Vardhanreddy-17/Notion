package com.v1.Notion.Service;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.v1.Notion.Model.OTP;
import com.v1.Notion.Repository.OTPRepository;

@Service
public class OTPServiceImpl implements OTPService{
	@Autowired
	private OTPRepository otpRepository;
	
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
		OTP newotp = new OTP();
	    OTP otpEntity = new OTP();
        otpEntity.setEmail(email);
        otpEntity.setOtp(otp);
        otpEntity.setCreatedAt(LocalDateTime.now());
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otpRepository.save(otpEntity);
        return otp;
	}
	
}
