package com.booking.module.auth.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterDTO {

  @NotBlank(message = "Name is required")
  @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters")
  private String name;

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  private String email;

  @NotBlank(message = "Password is required")
  @Pattern(
          regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).{6,100}$",
          message = "Password must contain uppercase, lowercase, digit, special char, min 6 chars"
  )
  private String password;

  @NotNull(message = "Organization ID is required")
  @Positive(message = "Organization ID must be a positive number")
  private Long organizationId;
}