package com.booking.module.user.dto;

import com.booking.module.user.entity.UserRole;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {

  @NotBlank(message = "Name field is required")
  @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters")
  private String name;

  @NotBlank(message = "Email field is required")
  @Email(message = "Invalid email format")
  private String email;

  @NotBlank(message = "Password field is required")
  @Pattern(
          regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{6,100}$",
          message = "Password must contain at least 1 uppercase character, 1 lowercase character, 1 digit, 1 special character, and be at least 6 characters long"
  )
  private String password;

  @NotNull(message = "Role field is required")
  private UserRole role;

  @NotNull(message = "Organization ID field is required")
  @Positive(message = "Organization ID must be a positive number")
  private Long organizationId;
}