package com.booking.module.orgnaization.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetOrgDTO {

  public Long id;

  public String name;

  public LocalDateTime createdAt;

  public LocalDateTime updatedAt;
}
