//package com.v1.Notion.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig extends WebSecurityConfiguration {
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//            .authorizeRequests()
//            .antMatchers("/api/auth/sendOTP", "/api/auth/signup").permitAll() // Allow unauthenticated access to these endpoints
//            .anyRequest().authenticated() // Require authentication for all other requests
//            .and()
//            .addFilter(new JwtAuthenticationFilter()) // Add the JWT filter for subsequent requests
//            .csrf().disable(); // Disable CSRF for stateless authentication
//    }
//}
