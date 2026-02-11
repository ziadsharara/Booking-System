package com.booking.module.orgnaization.mapper;

import com.booking.module.orgnaization.dto.CreateOrgDTO;
import com.booking.module.orgnaization.dto.GetOrgDTO;
import com.booking.module.orgnaization.dto.UpdateOrdDTO;
import com.booking.module.orgnaization.entity.Organization;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrganizationMapper {

  public Organization toEntity(CreateOrgDTO dto) {
    return Organization.builder()
            .name(dto.getName())
            .build();
  }

  public Organization toEntity(UpdateOrdDTO dto) {
    return Organization.builder()
            .name(dto.getName())
            .build();
  }

  public GetOrgDTO toGetOrgDTO(Organization organization) {
    return GetOrgDTO.builder()
            .id(organization.getId())
            .name(organization.getName())
            .createdAt(organization.getCreatedAt())
            .updatedAt(organization.getUpdatedAt())
            .build();
  }

  public List<GetOrgDTO> toGetOrgsDTOList(List<Organization> organizations) {
    List<GetOrgDTO> dtos = new ArrayList<>();
    for (Organization organization : organizations) {
      dtos.add(toGetOrgDTO(organization));
    }
    return dtos;
  }
}
