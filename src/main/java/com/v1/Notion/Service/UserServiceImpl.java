package com.v1.Notion.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.v1.Notion.Model.AccountType;
import com.v1.Notion.Model.OTP;
import com.v1.Notion.Model.Profile;
import com.v1.Notion.Model.User;
import com.v1.Notion.Repository.OTPRepository;
import com.v1.Notion.Repository.ProfileRepository;
import com.v1.Notion.Repository.UserRepository;
import com.v1.Notion.DTO.LoginRequest;
import com.v1.Notion.DTO.SignUpRequest;
import com.v1.Notion.DTO.UserResponseDTO;
import com.v1.Notion.config.ApiResponse;
import com.v1.Notion.Utility.*;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private OTPRepository otpRepository;
    
    @Autowired
    private ProfileRepository profileRepository;
    
    @Autowired
    private JwtUtility jwtUtility;
    
    // @Autowired
    // private PasswordEncoder passwordEncoder;

    @Override
    public ApiResponse signUp(SignUpRequest signUpRequest) {
        String firstName = signUpRequest.getFirstName();
        String lastName = signUpRequest.getLastName();
        String password = signUpRequest.getPassword();
        String confirmpassword = signUpRequest.getConfirmPassword();
        String email = signUpRequest.getEmail();
        String accountTypeString = signUpRequest.getAccountType();
        String contactnumber = signUpRequest.getContactNumber();
        
        // Validate required fields
        if(firstName == null || lastName == null || password == null || confirmpassword == null ||
           email == null || contactnumber == null) {
            return new ApiResponse(false, "All fields are required.", null);
        }
        
        // Check if passwords match
        if(!password.equals(confirmpassword)) {
            return new ApiResponse(false, "Password and Confirm Password do not match.", null);
        }
        
        // Check if user already exists
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            User user = existingUser.get();

            // If user exists but is not approved, delete them and allow re-registration
            if (!user.getApproved()) {
                userRepository.delete(user);
            } else {
                // If user is approved, block registration
                return new ApiResponse(false, "User already exists. Please sign in to continue.", null);
            }
        }
        

        // Set Account Type
        AccountType accountType;
        try {
            accountType = AccountType.valueOf(accountTypeString.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ApiResponse(false, "Invalid account type.", null);
        }
        
        // Set approval status based on account type
        boolean active = accountType != AccountType.INSTRUCTOR;
        
        // Create a profile
        Profile profile = new Profile();
        profile.setGender(null);
        profile.setAbout(null);
        profile.setContactNumber(Long.valueOf(contactnumber));
        profile.setDateOfBirth(null);
        profileRepository.save(profile);
        
        String jwtToken = jwtUtility.GenerateToken(email);
        // Create a new user
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password); // You can add password encoding here
        user.setAccountType(accountType);
        user.setApproved(false);
        user.setAdditionalDetails(profile);
        user.setImage("https://api.dicebear.com/6.x/initials/svg?seed=" + firstName + "%20" + lastName);
        user.setActive(active);
        user.setToken(jwtToken);
        userRepository.save(user);
        
        // Prepare the response DTO
        UserResponseDTO userResponseDTO = new UserResponseDTO(
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getAccountType().toString(),
            user.getApproved(),
            user.getImage(),
            user.getToken()
        );
        return new ApiResponse(true, "User registered successfully", userResponseDTO);
    }
    
    @Override
    public ApiResponse verifyOTP(String email, String enteredOtp) {
        // Fetch the most recent OTP record for the given OTP value
        Optional<OTP> latestOtp = otpRepository.findTopByEmailOrderByCreatedAtDesc(email);
        		
        if (latestOtp.isEmpty()) {
            return new ApiResponse(false, "Invalid OTP. Please try again.", null);
        }

        OTP otpEntity = latestOtp.get();

        // Extract email from OTP entity
        String storedEmail = otpEntity.getEmail();  // This is the email associated with the OTP

        // Check if OTP has expired
        if (otpEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            return new ApiResponse(false, "OTP has expired. Please request a new OTP.", null);
        }

        // Ensure the provided email matches the one associated with the OTP
        if (!storedEmail.equals(email)) {
            return new ApiResponse(false, "Email does not match the OTP record.", null);
        }

        // OTP is valid, update the user's approval status
        Optional<User> userOptional = userRepository.findByEmail(storedEmail);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setApproved(true);  // Mark the user as approved
            userRepository.save(user); // Save the updated user details

            // Generate JWT token
            String jwtToken = jwtUtility.GenerateToken(user.getEmail());

            // Prepare response with the token
            return new ApiResponse(true, "OTP verified successfully. Your account is now approved.", jwtToken);
        } else {
            return new ApiResponse(false, "User not found.", null);
        }
    }

	@Override
	public ApiResponse login(LoginRequest loginRequest) {
		String email = loginRequest.getEmail();
		String password = loginRequest.getPassword();
		if(email==null  || password == null) {
			return new ApiResponse(false,"All fields required",null);
		}
		Optional<User> exisistingUser = userRepository.findByEmail(email);
		if(!exisistingUser.isPresent()) {
			return new ApiResponse(false,"Please register before login",null);
		}
		User user = exisistingUser.get();
		if(!user.getPassword().equals(password)) {
			return new ApiResponse(false,"Please enter correct password",null);
		}
		if (!user.getApproved()) {
	        return new ApiResponse(false, "Account not approved. Please verify your email.", null);
	    }
		String jwtToken = jwtUtility.GenerateToken(user.getEmail());
		user.setToken(jwtToken);
		userRepository.save(user); 
		
		UserResponseDTO userResponseDTO = new UserResponseDTO(
		        user.getFirstName(),
		        user.getLastName(),
		        user.getEmail(),
		        user.getAccountType().toString(),
		        user.getApproved(),
		        user.getImage(),
		        jwtToken
		    );
		return new ApiResponse(true, "Login successful.", userResponseDTO);
	}

}
