package com.booking.module.resource.mapper;

import com.booking.module.resource.dto.ChangeResourceStatusDTO;
import com.booking.module.resource.dto.CreateResourceDTO;
import com.booking.module.resource.dto.GetResourceDTO;
import com.booking.module.resource.dto.UpdateResourceDTO;
import com.booking.module.resource.entity.Resource;
import com.booking.module.resource.entity.ResourceStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ResourceMapper {

  public Resource toEntity(CreateResourceDTO dto) {
    return Resource.builder()
            .name(dto.getName())
            .description(dto.getDescription())
            .organizationId(dto.getOrganizationId())
            .status(dto.getStatus() != null ? dto.getStatus() : ResourceStatus.ENABLED)  // Default to ENABLED
            .build();
  }

  public Resource toEntity(UpdateResourceDTO dto) {
    return Resource.builder()
            .name(dto.getName())
            .description(dto.getDescription())
            .status(dto.getStatus())
            .build();
  }

  public Resource toEntity(ChangeResourceStatusDTO dto) {
    return Resource.builder()
            .status(dto.status)
            .build();
  }

  public GetResourceDTO toGetResourceDTO (Resource resource) {
    return GetResourceDTO.builder()
            .id(resource.getId())
            .name(resource.getName())
            .description(resource.getDescription())
            .organizationId(resource.getOrganizationId())
            .status(resource.getStatus())
            .createdAt(resource.getCreatedAt())
            .updatedAt(resource.getUpdatedAt())
            .build();
  }

  public ChangeResourceStatusDTO toGetResourceStatusDTO (Resource resource) {
    return ChangeResourceStatusDTO.builder()
            .status(resource.getStatus())
            .build();
  }

  public List<GetResourceDTO> getGetResourceList(List<Resource> resources) {
    List<GetResourceDTO> dtos = new ArrayList<>();
    for (Resource resource : resources) {
      dtos.add(toGetResourceDTO(resource));
    }
    return dtos;
  }
}
