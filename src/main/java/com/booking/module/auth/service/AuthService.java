package com.booking.module.auth.service;

import com.booking.module.auth.dto.AuthResponseDTO;
import com.booking.module.auth.dto.LoginDTO;
import com.booking.module.auth.dto.RegisterDTO;
import com.booking.module.auth.entity.AuthUser;
import com.booking.module.user.entity.UserRole;
import com.booking.module.auth.repository.AuthUserRepository;
import com.booking.module.auth.util.JwtUtil;
import com.booking.module.user.entity.User;
import com.booking.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final AuthUserRepository authUserRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;
  private final UserService userService;

  /**
   * Register new user
   */
  @Transactional
  public AuthResponseDTO register(RegisterDTO dto) {

    // Check if email already exists
    if (authUserRepository.existsByEmail(dto.getEmail())) {
      throw new IllegalArgumentException("Email already registered");
    }

    // Create new auth user with USER role
    AuthUser authUser = AuthUser.builder()
            .email(dto.getEmail())
            .password(passwordEncoder.encode(dto.getPassword()))
            .role(UserRole.EMPLOYEE)  // Default role
            .build();

    AuthUser savedUser = authUserRepository.save(authUser);

    // Fetch user from User table to get organizationId
    User bookingUser = userService.getUserByEmail(dto.getEmail());

    // Generate JWT token with organizationId
    String token = jwtUtil.generateToken(
            savedUser.getId(),
            savedUser.getEmail(),
            savedUser.getRole().name(),
            bookingUser.getOrganizationId()
    );

    return AuthResponseDTO.builder()
            .id(savedUser.getId())
            .email(savedUser.getEmail())
            .token(token)
            .message("User registered successfully")
            .build();
  }


  public AuthResponseDTO login(LoginDTO dto) {

    // Find auth user by email
    AuthUser authUser = authUserRepository.findByEmail(dto.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));


    // Verify password
    if (!passwordEncoder.matches(dto.getPassword(), authUser.getPassword())) {
      throw new IllegalArgumentException("Invalid email or password");
    }

    // ✅ FIX: Fetch user from User table to get organizationId
    User bookingUser = userService.getUserByEmail(dto.getEmail());

    // Generate JWT token with organizationId
    String token = jwtUtil.generateToken(
            authUser.getId(),
            authUser.getEmail(),
            authUser.getRole().name(),
            bookingUser.getOrganizationId()
    );

    return AuthResponseDTO.builder()
            .id(authUser.getId())
            .email(authUser.getEmail())
            .token(token)
            .message("Login successful")
            .build();
  }


  public AuthUser getUserById(Long id) {
    return authUserRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
  }


  public AuthUser getUserByEmail(String email) {
    return authUserRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
  }
}