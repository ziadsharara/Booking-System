package com.booking.module.resource.dto;

import com.booking.module.resource.entity.ResourceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetResourceDTO {

  private Long id;

  private String name;

  private String description;

  private Long organizationId;

  private ResourceStatus status;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
