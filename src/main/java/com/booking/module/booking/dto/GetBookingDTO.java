package com.booking.module.booking.dto;

import com.booking.module.booking.entity.BookingStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetBookingDTO {

  private Long id;

  private Long userId;

  private Long resourceId;

  private Long organizationId;

  private LocalDateTime startTime;

  private LocalDateTime endTime;

  private BookingStatus status;

  private Long approvedBy;

  private LocalDateTime approvedAt;

  private LocalDateTime completedAt;

  private LocalDateTime cancelledAt;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
