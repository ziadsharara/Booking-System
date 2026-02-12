package com.booking.module.user.controller;

import com.booking.module.auth.util.JwtUtil;
import com.booking.module.user.dto.CreateUserDTO;
import com.booking.module.user.dto.GetUserDTO;
import com.booking.module.user.dto.UpdateUserDTO;
import com.booking.module.user.entity.User;
import com.booking.module.user.mapper.UserMapper;
import com.booking.module.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final UserMapper userMapper;
  private final JwtUtil jwtUtil;

  // Extract organizationId from JWT token
  private Long getManagerOrganizationId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String token = (String) auth.getCredentials();
    Long organizationId = jwtUtil.getOrganizationIdFromToken(token);

    if (organizationId == null) {
      throw new IllegalArgumentException("Organization ID not found in token");
    }

    return organizationId;
  }

  /**
   * Create User - Only MANAGER can create
   * Must belong to their organization
   */
  @PreAuthorize("hasRole('MANAGER')")
  @PostMapping
  public ResponseEntity<GetUserDTO> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
    try {
      // Get manager's organization ID
      Long organizationId = getManagerOrganizationId();

      // Ensure user is created in manager's organization
      if (!organizationId.equals(createUserDTO.getOrganizationId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();  // Can't create users in other orgs
      }

      User user = userService.createUser(createUserDTO);
      GetUserDTO responseDTO = userMapper.toGetUserDTO(user);

      return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * Get User - Only MANAGER can view
   * Must be from their organization
   */
  @PreAuthorize("hasRole('MANAGER')")
  @GetMapping("/{id}")
  public ResponseEntity<GetUserDTO> getUser(@PathVariable Long id) {
    try {
      // Get manager's organization ID
      Long organizationId = getManagerOrganizationId();

      User user = userService.getUser(id);

      // Ensure user belongs to manager's organization
      if (!organizationId.equals(user.getOrganizationId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();  // Can't view users from other orgs
      }

      GetUserDTO responseDTO = userMapper.toGetUserDTO(user);
      return ResponseEntity.ok(responseDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Get All Users in Manager's Organization
   */
  @PreAuthorize("hasRole('MANAGER')")
  @GetMapping("/organization/my-org/users")
  public ResponseEntity<List<GetUserDTO>> getAllUsersOfMyOrganization() {
    try {
      // Get manager's organization ID
      Long organizationId = getManagerOrganizationId();

      // Get all users in that organization
      List<User> users = userService.getAllUsersOfOrg(organizationId);
      List<GetUserDTO> dtos = userMapper.toGetUserDTOList(users);

      return ResponseEntity.ok(dtos);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Get user by email - Only MANAGER can view
   */
  @PreAuthorize("hasRole('MANAGER')")
  @GetMapping("/email/{email}")
  public ResponseEntity<GetUserDTO> getUserByEmail(@PathVariable String email) {
    try {
      // Get manager's organization ID
      Long organizationId = getManagerOrganizationId();

      User user = userService.getUserByEmail(email);

      // Ensure user belongs to manager's organization
      if (!organizationId.equals(user.getOrganizationId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }

      GetUserDTO userDTO = userMapper.toGetUserDTO(user);
      return ResponseEntity.ok(userDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Update User - Only MANAGER can update
   * Must be from their organization
   */
  @PreAuthorize("hasRole('MANAGER')")
  @PutMapping("/{id}")
  public ResponseEntity<GetUserDTO> updateUser(
          @PathVariable Long id,
          @Valid @RequestBody UpdateUserDTO updateUserDTO) {
    try {
      // Get manager's organization ID
      Long organizationId = getManagerOrganizationId();

      User user = userService.getUser(id);

      // Ensure user belongs to manager's organization
      if (!organizationId.equals(user.getOrganizationId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();  // Can't update users from other orgs
      }

      User updatedUser = userService.updateUser(id, updateUserDTO);
      GetUserDTO userDTO = userMapper.toGetUserDTO(updatedUser);

      return ResponseEntity.ok(userDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Delete User - Only MANAGER can delete
   * Must be from their organization
   */
  @PreAuthorize("hasRole('MANAGER')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
    try {
      // Get manager's organization ID
      Long organizationId = getManagerOrganizationId();

      User user = userService.getUser(id);

      // Ensure user belongs to manager's organization
      if (!organizationId.equals(user.getOrganizationId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();  // Can't delete users from other orgs
      }

      userService.deleteUser(id);
      return ResponseEntity.noContent().build();
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }
}