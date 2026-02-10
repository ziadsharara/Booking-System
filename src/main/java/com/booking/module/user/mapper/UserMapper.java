package com.booking.module.user.mapper;

import com.booking.module.user.dto.CreateUserDTO;
import com.booking.module.user.dto.GetUserDTO;
import com.booking.module.user.dto.UpdateUserDTO;
import com.booking.module.user.entity.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserMapper {

  // Convert CreateUserDTO to User entity (for CREATE operation)
  public User toEntity(CreateUserDTO dto) {
    return User.builder()
            .name(dto.getName())
            .email(dto.getEmail())
            .password(dto.getPassword())
            .role(dto.getRole())
            .organizationId(dto.getOrganizationId())
            .build();
  }

  // Convert UpdateUserDTO to User entity (for UPDATE operation)
  public User toEntity(UpdateUserDTO dto) {
    return User.builder()
            .name(dto.getName())
            .role(dto.getRole())
            .build();
  }

  // Convert User entity to GetUserDTO (for READ operation)
  public GetUserDTO toGetUserDTO(User user) {
    return GetUserDTO.builder()
            .id(user.getId())
            .name(user.getName())
            .email(user.getEmail())
            .role(user.getRole())
            .organizationId(user.getOrganizationId())
            .createdAt(user.getCreatedAt())
            .updatedAt(user.getUpdatedAt())
            .build();
  }

  // Convert list of User entities to list of GetUserDTOs
  public List<GetUserDTO> toGetUserDTOList(List<User> users) {
    List<GetUserDTO> dtos = new ArrayList<>();
    for (User user : users) {
      dtos.add(toGetUserDTO(user));
    }
    return dtos;
  }

}