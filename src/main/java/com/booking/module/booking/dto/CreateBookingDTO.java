package com.booking.module.booking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBookingDTO {

  @NotNull(message = "User ID is required")
  @Positive(message = "User ID must be positive")
  private Long userId;

  @NotNull(message = "Resource ID is required")
  @Positive(message = "Resource ID must be positive")
  private Long resourceId;

  @NotNull(message = "Organization ID is required")
  @Positive(message = "Organization ID must be positive")
  private Long organizationId;

  @NotNull(message = "Start time is required")
  private LocalDateTime startTime;

  @NotNull(message = "End time is required")
  private LocalDateTime endTime;
}
