package com.booking.module.user.dto;

import com.booking.module.user.entity.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDTO {

  @NotBlank(message = "Name field is required")
  @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters")
  private String name;

  @NotNull(message = "Role field is required")
  private UserRole role;
}