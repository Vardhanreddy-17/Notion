package com.v1.Notion.Service;

import java.security.Principal;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.v1.Notion.DTO.UpdateProfile;
import com.v1.Notion.Model.Profile;
import com.v1.Notion.Model.User;
import com.v1.Notion.Repository.ProfileRepository;
import com.v1.Notion.Repository.UserRepository;
import com.v1.Notion.Utility.JwtUtility;
import com.v1.Notion.config.ApiResponse;

public class ProfileServiceImpl implements ProfileService{
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProfileRepository profileRepository;
	@Autowired
    private JwtUtility jwtUtility;
	@Override
	public ApiResponse updateProfile(UpdateProfile updateProfile,String token) {
		String email;
		try {
			email = jwtUtility.extractEmail(token);
	    } catch (Exception e) {
	        return new ApiResponse(false, "Invalid or expired token.", null);
	    }
		Optional<User> optionalUser = userRepository.findByEmail(email);
		if (!optionalUser.isPresent()) {
		    return new ApiResponse(false,"User not found");
		}
		User user = optionalUser.get();

		if (updateProfile.getFirstName() != null) user.setFirstName(updateProfile.getFirstName());
        if (updateProfile.getLastName() != null) user.setLastName(updateProfile.getLastName());
        userRepository.save(user);
        
        Optional<Profile> optionalProfile = profileRepository.findById(user.getAdditionalDetails().getId());
        if (!optionalProfile.isPresent()) {
		    return new ApiResponse(false,"Profile not found");
		}
        Profile profile = optionalProfile.get();
        if (updateProfile.getDateOfBirth() != null) profile.setDateOfBirth(updateProfile.getDateOfBirth());
        if (updateProfile.getAbout() != null) profile.setAbout(updateProfile.getAbout());
        if (updateProfile.getContactNumber() != null) {
            profile.setContactNumber(Long.parseLong(updateProfile.getContactNumber()));
        }
        if (updateProfile.getGender() != null) profile.setGender(updateProfile.getGender());
        profileRepository.save(profile);
        Optional<User> optionalUsers = userRepository.findById(user.getId());
        if (!optionalUsers.isPresent()) {
        	return new ApiResponse(false,"User not found");
        }
        User updatedUser = optionalUser.get();
        return new ApiResponse(true, "Profile updated successfully", updatedUser);
	}

}
