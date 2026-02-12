package com.booking.module.auth.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String jwtSecret;

  @Value("${jwt.expiration}")  // 24 hours in milliseconds
  private long jwtExpiration;

   // Generate JWT token for user
  public String generateToken(Long userId, String email, String role, Long organizationId) {
    SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

    return Jwts.builder()
            .setSubject(email)
            .claim("userId", userId)
            .claim("role", role)
            .claim("organizationId", organizationId)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
  }

  // Validate JWT token
  public boolean validateToken(String token) {
    try {
      SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
      Jwts.parserBuilder()
              .setSigningKey(key)
              .build()
              .parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  // Get email from token
  public String getEmailFromToken(String token) {
    SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getSubject();
  }

  // Get user id from token
  public Long getUserIdFromToken(String token) {
    SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("userId", Long.class);
  }

  // Get role form token
  public String getRoleFromToken(String token) {
    SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("role", String.class);
  }

  // Get organization ID from token
  public Long getOrganizationIdFromToken(String token) {
    SecretKey key = Keys.hmacShaKeyFor(jwtSecret.getBytes());
    return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody()
            .get("organizationId", Long.class);
  }
}