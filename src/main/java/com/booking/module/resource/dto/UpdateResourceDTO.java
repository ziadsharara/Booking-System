package com.booking.module.resource.dto;

import com.booking.module.resource.entity.ResourceStatus;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateResourceDTO {

  @Size(min = 2, max = 100, message = "Name must be between 2 and 100 Characters")
  private String name;

  @Size(min = 3, max = 300, message = "Description must be between 3 and 300 characters")
  private String description;

  private ResourceStatus status;
}
