package com.v1.Notion.Service;

import org.springframework.http.ResponseEntity;

import com.v1.Notion.DTO.TagsRequest;
import com.v1.Notion.Model.Tags;
import com.v1.Notion.Repository.TagsRepository;
import com.v1.Notion.config.ApiResponse;

public interface TagsService {
	public ApiResponse createTag(TagsRequest tagsRequest);
	public ApiResponse showAllCategories();
}
