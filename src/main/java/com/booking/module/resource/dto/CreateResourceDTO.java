package com.booking.module.resource.dto;

import com.booking.module.resource.entity.ResourceStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateResourceDTO {

  @NotBlank(message = "Name field is required")
  @Size(min = 2, max = 100, message = "Name must be between 2 and 100 Characters")
  private String name;

  @Size(min = 3, max = 300, message = "Description must be between 3 and 300 characters")
  private String description;

  @NotNull(message = "Organization Id field is required")
  @Positive(message = "Organization Id must be positive")
  private Long organizationId;

  private ResourceStatus status;
}
