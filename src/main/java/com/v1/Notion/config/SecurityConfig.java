//package com.v1.Notion.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.crypto.password.PasswordEncoder;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new PasswordEncoder() {
//            @Override
//            public String encode(CharSequence rawPassword) {
//                // Implement custom password encoding logic
//                return rawPassword.toString();  // Example: No encoding, just return the password as is
//            }
//
//            @Override
//            public boolean matches(CharSequence rawPassword, String encodedPassword) {
//                // Implement custom password matching logic
//                return rawPassword.toString().equals(encodedPassword);  // Example: Check if raw password matches the encoded password
//            }
//        };
//    }
//}
