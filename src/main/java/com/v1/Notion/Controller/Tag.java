package com.v1.Notion.Controller;

import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.v1.Notion.DTO.TagsRequest;
import com.v1.Notion.Service.TagsService;
import com.v1.Notion.config.ApiResponse;

@RestController
@RequestMapping("/api/Tag")
public class Tag {
	@Autowired
	private TagsService tagsService;
	
	@PostMapping("/Createtag")
	public ResponseEntity<?> createTag(@RequestBody TagsRequest tagsRequest){
		try {
			ApiResponse response = tagsService.createTag(tagsRequest);
			return ResponseEntity.ok(response);
		}catch(IllegalArgumentException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, ex.getMessage()));
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Tag cannot be created successfully, Please try again later"));
		}

	}
	@GetMapping("/getAllTags")
	public ResponseEntity<?> getAllTags(){
		try {
			ApiResponse response = tagsService.showAllCategories();
			return ResponseEntity.ok(response);
		}catch(IllegalArgumentException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ApiResponse(false, ex.getMessage()));
		}catch(Exception ex) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse(false, "Tags cannot be fetched right now!, Please try again later"));
		}
	}
	
}
