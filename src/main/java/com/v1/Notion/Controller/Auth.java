package com.v1.Notion.Controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.v1.Notion.Service.OTPService;
import com.v1.Notion.Service.UserService;

@RestController
@RequestMapping("/api/auth")
public class Auth {
	@Autowired
	private OTPService otpService;
	@Autowired
	private UserService userService;
	@GetMapping("/sendOTP")
	public ResponseEntity<?> sendOTP(@RequestBody Map<String,String> requestBody){
		try {
			String email = requestBody.get("email");
			if(email==null){
				return ResponseEntity.badRequest().body("Email is required");
			}
			String otp = otpService.sendOTP(email);
			return ResponseEntity.ok(Map.of("Success",true,"message","OTP sent successfully","data",otp));
		}catch(IllegalArgumentException ex) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("success", false, "message", ex.getMessage()));
		}catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "message", ex.getMessage()));
        }
	}
	
}
