package com.v1.Notion.Utility;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key; 
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtUtility {
    
    @Value("${jwt.secret}")
    private String secretKey;  // This can be removed if you want to use a generated key
    
    @Value("${jwt.expiration}")
    private long jwtExpirationInMs;
    
    // Generate Token
 // Updated GenerateToken Method
    public String GenerateToken(String email, String role) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);  // Generate secure key

        return Jwts.builder()
               .setSubject(email)
               .claim("role", role) // Add role claim
               .setIssuedAt(new Date())
               .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
               .signWith(key)  // Use the generated secure key here
               .compact();
    }

    // Validate Token
    public boolean validateToken(String token, String email) {
        String extractedEmail = extractEmail(token);
        return (extractedEmail.equals(email) && !isTokenExpired(token));
    }
    
    // Extract Email 
    public String extractEmail(String token) {
        return Jwts.parser()
                .setSigningKey(Keys.secretKeyFor(SignatureAlgorithm.HS512))  // Use the generated secure key here
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    public String extractRole(String token) {
        return Jwts.parser()
                .setSigningKey(Keys.secretKeyFor(SignatureAlgorithm.HS512))  // Use the generated secure key here
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }

    
    // Check if Token Expired
    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(Keys.secretKeyFor(SignatureAlgorithm.HS512))  // Use the generated secure key here
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}
