package com.v1.Notion.Middlewares;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.v1.Notion.Utility.JwtUtility;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtility jwtUtility;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestPath = request.getRequestURI();
        // Exclude certain paths from authentication
        if (requestPath.contains("/signup") || requestPath.contains("/login") || requestPath.contains("/verifyOtp")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Get Authorization header
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Missing or invalid Authorization header");
            return;
        }

        // Extract token from header
        String token = authHeader.substring(7); // Remove "Bearer " prefix

        try {
            String email = jwtUtility.extractEmail(token);

            // Validate token
            if (!jwtUtility.validateToken(token, email)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired token");
                return;
            }

            // Token is valid, proceed with the request
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: " + ex.getMessage());
        }
    }
    
}
