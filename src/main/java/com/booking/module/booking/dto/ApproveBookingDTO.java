package com.booking.module.booking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApproveBookingDTO {

  @NotNull(message = "Approved by user ID is required")
  @Positive(message = "User ID must be positive")
  private Long approvedByUserId;
}
