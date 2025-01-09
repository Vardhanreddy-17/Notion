package com.v1.Notion.Service;

import com.v1.Notion.Model.User;
import com.v1.Notion.config.ApiResponse;
import com.v1.Notion.DTO.*;
public interface UserService {
	ApiResponse signUp(SignUpRequest signUpRequest);
	public ApiResponse verifyOTP(String email,String enteredOtp);
}
