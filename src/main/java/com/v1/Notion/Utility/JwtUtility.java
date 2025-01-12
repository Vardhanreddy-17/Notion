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
    public String GenerateToken(String email) {
        // Use secretKey or generate a secure key
        // Key key = new SecretKeySpec(secretKey.getBytes(), SignatureAlgorithm.HS512.getJcaName());  // Uncomment if using custom secret
        
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS512);  // Generate secure key
        
        return Jwts.builder()
               .setSubject(email)
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
    
    // Extract Email from Token
    public String extractEmail(String token) {
        return Jwts.parser()
                .setSigningKey(Keys.secretKeyFor(SignatureAlgorithm.HS512))  // Use the generated secure key here
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
    
    // Check if Token is Expired
    public boolean isTokenExpired(String token) {
        Date expiration = Jwts.parser()
                .setSigningKey(Keys.secretKeyFor(SignatureAlgorithm.HS512))  // Use the generated secure key here
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expiration.before(new Date());
    }
}
