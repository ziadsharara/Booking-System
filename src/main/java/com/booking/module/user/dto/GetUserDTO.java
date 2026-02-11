package com.booking.module.user.dto;

import com.booking.module.user.entity.UserRole;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetUserDTO {

  private Long id;

  private String name;

  private String email;

  private UserRole role;

  private Long organizationId;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}