package com.v1.Notion.Controller;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.v1.Notion.DTO.UpdateProfile;
import com.v1.Notion.Service.ProfileService;
import com.v1.Notion.config.ApiResponse;

@RestController
@RequestMapping("/api/profile")
public class Profile {
	private ProfileService profileService;
	@PutMapping("/updateProfile")
	public ResponseEntity<?> updateProdile(@RequestBody UpdateProfile updateProfile,Principal principal) {
		try {
			ApiResponse response = profileService.updateProfile(updateProfile,principal.getName());
			return ResponseEntity.ok(response);
		}catch(IllegalArgumentException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, ex.getMessage()));
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Profile cannot be Updated, Please try again later"));
		}
	}
}
