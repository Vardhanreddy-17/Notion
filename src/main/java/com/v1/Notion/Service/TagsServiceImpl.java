package com.v1.Notion.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.v1.Notion.DTO.TagsRequest;
import com.v1.Notion.Model.Tags;
import com.v1.Notion.Repository.TagsRepository;
import com.v1.Notion.config.ApiResponse;

@Service
public class TagsServiceImpl implements TagsService{

	@Autowired 
	private TagsRepository tagsRepository;
	@Override
	public ApiResponse createTag(TagsRequest tagsRequest) {
		String name = tagsRequest.getName();
		String description = tagsRequest.getDescription();
		if(name == null || description == null) {
			return new ApiResponse(false, "All fields are required",null);
		}
		Tags tag = new Tags();
		tag.setName(name);
		tag.setDescription(description);
		
		tagsRepository.save(tag);
		return new ApiResponse(true,"Tags created successfully",true);
	}
	

}
