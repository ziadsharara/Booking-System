package com.booking.module.resource.dto;

import com.booking.module.resource.entity.ResourceStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChangeResourceStatusDTO {

  @NotNull(message = "The status field is null!")
  public ResourceStatus status;
}
