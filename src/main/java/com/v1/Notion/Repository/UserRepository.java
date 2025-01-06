package com.v1.Notion.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.v1.Notion.Model.User;

public interface UserRepository extends JpaRepository<User, Long>{
	Optional<User> findByEmail(String email);
}
