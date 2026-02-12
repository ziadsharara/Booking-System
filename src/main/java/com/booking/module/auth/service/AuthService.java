package com.booking.module.auth.service;

import com.booking.module.auth.dto.AuthResponseDTO;
import com.booking.module.auth.dto.LoginDTO;
import com.booking.module.auth.dto.RegisterDTO;
import com.booking.module.auth.util.JwtUtil;
import com.booking.module.user.entity.User;
import com.booking.module.user.entity.UserRole;
import com.booking.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtil jwtUtil;

  /**
   * Register a new user — saves directly to the users table.
   */
  @Transactional
  public AuthResponseDTO register(RegisterDTO dto) {

    // Check if email already exists
    if (userRepository.existsByEmail(dto.getEmail())) {
      throw new IllegalArgumentException("Email already registered");
    }

    // Build a User entity with the registration data
    User user = User.builder()
            .name(dto.getName())
            .email(dto.getEmail())
            .password(passwordEncoder.encode(dto.getPassword()))
            .role(UserRole.EMPLOYEE)  // Default role
            .organizationId(dto.getOrganizationId())
            .build();

    User savedUser = userRepository.save(user);

    // Generate JWT token
    String token = jwtUtil.generateToken(
            savedUser.getId(),
            savedUser.getEmail(),
            savedUser.getRole().name(),
            savedUser.getOrganizationId()
    );

    return AuthResponseDTO.builder()
            .id(savedUser.getId())
            .email(savedUser.getEmail())
            .token(token)
            .message("User registered successfully")
            .build();
  }

  /**
   * Login — authenticates against the users table.
   */
  public AuthResponseDTO login(LoginDTO dto) {

    // Find user by email
    User user = userRepository.findByEmail(dto.getEmail())
            .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

    // Verify password
    if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
      throw new IllegalArgumentException("Invalid email or password");
    }

    // Generate JWT token
    String token = jwtUtil.generateToken(
            user.getId(),
            user.getEmail(),
            user.getRole().name(),
            user.getOrganizationId()
    );

    return AuthResponseDTO.builder()
            .id(user.getId())
            .email(user.getEmail())
            .token(token)
            .message("Login successful")
            .build();
  }

  /**
   * Get user by ID from the users table.
   */
  public User getUserById(Long id) {
    return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
  }

  /**
   * Get user by email from the users table.
   */
  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("User not found"));
  }
}