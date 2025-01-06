package com.v1.Notion.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.v1.Notion.Model.AccountType;
import com.v1.Notion.Model.OTP;
import com.v1.Notion.Model.Profile;
import com.v1.Notion.Model.User;
import com.v1.Notion.Repository.OTPRepository;
import com.v1.Notion.Repository.ProfileRepository;
import com.v1.Notion.Repository.UserRepository;
import com.v1.Notion.DTO.*;
import com.v1.Notion.config.*;
@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OTPRepository otpRepository;
	
	@Autowired
	private ProfileRepository profileRepository;
//	
//	@Autowired
//	private PasswordEncoder passwordEncoder;

	@Override
	public String signUp(SignUpRequest signUpRequest) {
		String firstName = signUpRequest.getFirstName();
		String lastName = signUpRequest.getLastName();
		String password = signUpRequest.getPassword();
		String confirmpassword = signUpRequest.getConfirmPassword();
		String email = signUpRequest.getEmail();
		String accountTypeString = signUpRequest.getAccountType();
		String contactnumber = signUpRequest.getContactNumber();
		String otp = signUpRequest.getOtp();
		
		if(firstname == null || lastname==null || password == null || confirmpassword == null ||
		   email == null || contactnumber == null || otp == null) {
			throw new IllegalArgumentException("All fields are required.");
		}
		
		if(!password.equals(confirmpassword)) {
			throw new IllegalArgumentException("Password and Confirm Password do not match.");
		}
		
		if(userRepository.findByEmail(email).isPresent()) {
			throw new IllegalArgumentException("User already exists. Please sign in to continue.");
		}
		
		Optional<OTP> latestotp = otpRepository.findTopByEmailOrderByCreatedAtDesc(email);
		if(latestotp.isEmpty() || !latestotp.get().getOtp().equals(otp)) {
			throw new IllegalArgumentException("Invalid or Expired OTP");
		}
		
		AccountType accountType;
        try {
            accountType = AccountType.valueOf(accountTypeString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid account type.");
        }
		
        boolean approved = accountType != AccountType.INSTRUCTOR;
        
		Profile profile = new Profile();
		profile.setGender(null);
		profile.setAbout(null);
		profile.setContactNumber(Long.valueOf(contactnumber));
		profile.setDateOfBirth(null);
		profileRepository.save(profile);
		
		User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setAccountType(accountType);
        user.setApproved(approved);
        user.setAdditionalDetails(profile);
        user.setImage("https://api.dicebear.com/6.x/initials/svg?seed=" + firstName + "%20" + lastName);
        
        userRepository.save(user);
		return "User registered successfully";
	}

}
