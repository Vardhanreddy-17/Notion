package com.v1.Notion.Service;

import org.springframework.stereotype.Service;

import com.v1.Notion.Model.AccountType;
import com.v1.Notion.Model.Profile;
import com.v1.Notion.Model.User;
import com.v1.Notion.DTO.*;
@Service
public class UserServiceImpl implements UserService{

	@Override
	public String signUp(SignUpRequest signUpRequest) {
		String firstname = signUpRequest.getFirstName();
		String lastname = signUpRequest.getLastName();
		String password = signUpRequest.getPassword();
		String confirmpassword = signUpRequest.getConfirmPassword();
		String email = signUpRequest.getEmail();
		AccountType accounttype = signUpRequest.getAccountType();
		String otp = signUpRequest.getOtp();
		
		
		Profile profile = new Profile();
		profile.setGender(null);
		profile.setAbout(null);
		profile.setContactNumber(null);
		profile.setDateOfBirth(null);
		
		return "";
	}

}
