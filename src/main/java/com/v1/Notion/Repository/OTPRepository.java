package com.v1.Notion.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.v1.Notion.Model.OTP;

public interface OTPRepository extends JpaRepository<OTP, Long>{
	@Query("Select o from OTP o where o.otp = :otp")
	Optional<OTP> findOTP(@Param("otp") String otp);
	@Query("Select o from OTP o where o.email = :email")
	Optional<OTP> findEmail(@Param("email") String email);
}
