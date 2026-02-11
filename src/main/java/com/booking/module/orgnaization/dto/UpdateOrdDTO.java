package com.booking.module.orgnaization.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateOrdDTO {

  @NotBlank(message = "Name is required!")
  public String name;
}
