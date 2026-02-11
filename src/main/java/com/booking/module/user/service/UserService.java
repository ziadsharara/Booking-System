package com.booking.module.user.service;

import com.booking.module.orgnaization.repository.OrganizationRepository;
import com.booking.module.user.dto.CreateUserDTO;
import com.booking.module.user.dto.GetUserDTO;
import com.booking.module.user.dto.UpdateUserDTO;
import com.booking.module.user.entity.User;
import com.booking.module.user.mapper.UserMapper;
import com.booking.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  // Create User
  @Transactional
  public User createUser(CreateUserDTO createUserDTO) {
    if (userRepository.existsByEmail(createUserDTO.getEmail())) {
      throw new IllegalArgumentException("Email already exist!");
    }

    User user = userMapper.toEntity(createUserDTO);

    user.setPassword(passwordEncoder.encode(user.getPassword()));

    return userRepository.save(user);
  }

  // Get User by id
  public User getUser(Long id) {
    return userRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("There's no user with this ID!"));
  }

  // Get All Users (paginated)
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  // Get All Users of an Organization
  public List<User> getAllUsersOfOrg(Long organizationId) {
    return userRepository.findAllByOrganizationId(organizationId);
  }

  public User getUserByEmail(String email) {
    return userRepository.findByEmail(email)
            .orElseThrow(() -> new IllegalArgumentException("There's no user with this email"));
  }

  // Update User
  @Transactional
  public User updateUser(Long id, UpdateUserDTO updateUserDTO) {
    User user = userRepository.getUserById(id);

    User updates = userMapper.toEntity(updateUserDTO);

    if (updates.getName() != null && !updates.getName().isBlank())
      user.setName(updates.getName());

    if (updates.getRole() != null)
      user.setRole(updates.getRole());

    return userRepository.save(user);
  }

  // Delete User
  @Transactional
  public void deleteUser(Long id) {
    if (!userRepository.existsById(id)) {
      throw new IllegalArgumentException("There's no user with this ID");
    }
    userRepository.deleteById(id);
  }
}
