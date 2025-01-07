package com.v1.Notion.Service;

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
import com.v1.Notion.DTO.SignUpRequest;
import com.v1.Notion.DTO.UserResponseDTO;
import com.v1.Notion.config.ApiResponse;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private OTPRepository otpRepository;
    
    @Autowired
    private ProfileRepository profileRepository;
    
//    @Autowired
//    private JavaMailSender mailsender;
    
    @Value("${spring.mail.host}")
    private String mailHost;
    
    @Value("${spring.mail.port}")
    private int port;
    
    @Value("${spring.mail.username}")
    private String mailUsername;
    
    @Value("${spring.mail.password}")
    private String mailPassword;
    
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
        String otp = signUpRequest.getOtp();
        
        // Validate required fields
        if(firstName == null || lastName == null || password == null || confirmpassword == null ||
           email == null || contactnumber == null || otp == null) {
            return new ApiResponse(false, "All fields are required.", null);
        }
        
        // Check if passwords match
        if(!password.equals(confirmpassword)) {
            return new ApiResponse(false, "Password and Confirm Password do not match.", null);
        }
        
        // Check if user already exists
        if(userRepository.findByEmail(email).isPresent()) {
            return new ApiResponse(false, "User already exists. Please sign in to continue.", null);
        }
        
        // Verify OTP
        Optional<OTP> latestotp = otpRepository.findTopByEmailOrderByCreatedAtDesc(email);
        if(latestotp.isEmpty() || !latestotp.get().getOtp().equals(otp)) {
            return new ApiResponse(false, "Invalid or Expired OTP", null);
        }
        
        // Set Account Type
        AccountType accountType;
        try {
            accountType = AccountType.valueOf(accountTypeString.toUpperCase());
        } catch (IllegalArgumentException e) {
            return new ApiResponse(false, "Invalid account type.", null);
        }
        
        // Set approval status based on account type
        boolean approved = accountType != AccountType.INSTRUCTOR;
        
        // Create a profile
        Profile profile = new Profile();
        profile.setGender(null);
        profile.setAbout(null);
        profile.setContactNumber(Long.valueOf(contactnumber));
        profile.setDateOfBirth(null);
        profileRepository.save(profile);
        
        // Create a new user
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password); // You can add password encoding here
        user.setAccountType(accountType);
        user.setApproved(approved);
        user.setAdditionalDetails(profile);
        user.setImage("https://api.dicebear.com/6.x/initials/svg?seed=" + firstName + "%20" + lastName);
        
        userRepository.save(user);
        
        // Prepare the response DTO
        UserResponseDTO userResponseDTO = new UserResponseDTO(
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getAccountType().toString(),
            user.getApproved(),
            user.getImage()
        );
        return new ApiResponse(true, "User registered successfully", userResponseDTO);
    }
}
