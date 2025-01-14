package com.v1.Notion.Service;

import java.security.Principal;

import com.v1.Notion.DTO.UpdateProfile;
import com.v1.Notion.config.ApiResponse;

public interface ProfileService {
	public ApiResponse updateProfile(UpdateProfile updateProfile,Principal principal);
}
