package com.booking.module.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateBookingDTO {

  private LocalDateTime startTime;

  private LocalDateTime endTime;
}