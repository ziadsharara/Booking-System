package com.booking.module.user.controller;

import com.booking.module.user.dto.CreateUserDTO;
import com.booking.module.user.dto.GetUserDTO;
import com.booking.module.user.dto.UpdateUserDTO;
import com.booking.module.user.entity.User;
import com.booking.module.user.mapper.UserMapper;
import com.booking.module.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

  public final UserService userService;
  public final UserMapper userMapper;

  // Create User
  @PostMapping
  public ResponseEntity<GetUserDTO> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
    try {
      User user = userService.createUser(createUserDTO);
      GetUserDTO responseDTO = userMapper.toGetUserDTO(user);

      return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  // Get User
  @GetMapping(path = "/{id}")
  public ResponseEntity<GetUserDTO> getUser(@PathVariable Long id) {
    try {
      User user = userService.getUser(id);
      GetUserDTO responseDTO = userMapper.toGetUserDTO(user);

      return ResponseEntity.ok(responseDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

   // Get all users
  @GetMapping
  public ResponseEntity<List<GetUserDTO>> getAllUsers() {
    List<User> users = userService.getAllUsers();
    List<GetUserDTO> dtos = userMapper.toGetUserDTOList(users);

    return ResponseEntity.ok(dtos);
  }

  // Get all users in specific organization
  @GetMapping("/organization/{orgId}")
  public ResponseEntity<List<GetUserDTO>> getAllUsersOfOrganization(@PathVariable Long orgId) {
    List<User> users = userService.getAllUsersOfOrg(orgId);
    List<GetUserDTO> dtos = userMapper.toGetUserDTOList(users);

    return ResponseEntity.ok(dtos);
  }

  // Get user by email
  @GetMapping("/email/{email}")
  public ResponseEntity<GetUserDTO> getUserByEmail(@PathVariable String email) {
    User user = userService.getUserByEmail(email);
    GetUserDTO userDTO = userMapper.toGetUserDTO(user);

    return ResponseEntity.ok(userDTO);
  }

  // Update user by id
  @PutMapping("/{id}")
  public ResponseEntity<GetUserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UpdateUserDTO updateUserDTO) {
    User user = userService.updateUser(id, updateUserDTO);
    GetUserDTO userDTO = userMapper.toGetUserDTO(user);

    return ResponseEntity.ok(userDTO);
  }

  @DeleteMapping("/{id}")
  public void deleteUser(@PathVariable Long id) {
    userService.deleteUser(id);
  }

}
