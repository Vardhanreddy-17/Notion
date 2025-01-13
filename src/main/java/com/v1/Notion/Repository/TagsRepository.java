package com.v1.Notion.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.v1.Notion.Model.Tags;

@Repository
public interface TagsRepository extends JpaRepository<Tags, Long>{
	
}
