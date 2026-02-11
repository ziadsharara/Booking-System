package com.booking.module.resource.service;

import com.booking.module.orgnaization.entity.Organization;
import com.booking.module.resource.dto.CreateResourceDTO;
import com.booking.module.resource.dto.UpdateResourceDTO;
import com.booking.module.resource.entity.Resource;
import com.booking.module.resource.entity.ResourceStatus;
import com.booking.module.resource.mapper.ResourceMapper;
import com.booking.module.resource.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceService {

  private final ResourceRepository resourceRepository;
  private final ResourceMapper resourceMapper;

  @Transactional
  public Resource createResource(CreateResourceDTO dto) {
    if (resourceRepository.existsByNameAndOrganizationId(dto.getName(), dto.getOrganizationId())) {
      throw new IllegalArgumentException("Resource is already exist in this organization");
    }

    Resource resource = resourceMapper.toEntity(dto);

    return resourceRepository.save(resource);
  }

  @Transactional
  public Resource updateResource(Long id, UpdateResourceDTO dto) {
    if (resourceRepository.existsByName(dto.getName())) {
      throw new IllegalArgumentException("The name of resource already exist");
    }

    Resource resource = resourceRepository.getResourcesById(id);

    if (dto.getName() != null && !dto.getName().isBlank()) {
      resource.setName(dto.getName());
    }

    if (dto.getDescription() != null) {
      resource.setDescription(dto.getDescription());
    }

    if (dto.getStatus() != null) {
      resource.setStatus(dto.getStatus());
    }


    return resourceRepository.save(resource);
  }

  public Resource getResourceById(Long id) {
   return resourceRepository.findById(id)
           .orElseThrow(() -> new IllegalArgumentException("there's no resource with this id"));
  }

  public List<Resource> getAllResourcesByOrganizationId(Long id) {
    return resourceRepository.findAllByOrganizationId(id);
  }

  public Resource getResourceByName(String name) {
    return resourceRepository.findByName(name)
            .orElseThrow(() -> new IllegalArgumentException("there's no resource with this name"));
  }

  @Transactional
  public void deleteResource(Long id) {
    if (!resourceRepository.existsById(id))
      throw new IllegalArgumentException("There's no resource wit this ID");

    resourceRepository.deleteById(id);
  }

  @Transactional
  public void deleteAllResourcesByOrganizationId(Long orgId) {
    List<Resource> resources = resourceRepository.findAllByOrganizationId(orgId);
     resourceRepository.deleteAll(resources);
  }

  // Enable/Disable status
  @Transactional
  public Resource toggleResourceStatus(Long id) {
    Resource resource = getResourceById(id);

    if (resource.getStatus() == ResourceStatus.ENABLED) {
      resource.setStatus(ResourceStatus.DISABLED);
    } else {
      resource.setStatus(ResourceStatus.ENABLED);
    }

    return resourceRepository.save(resource);
  }

  // Check if resource is available
  public boolean isResourceAvailable(Long id) {
    Resource resource = resourceRepository.getResourcesById(id);
    return resource.getStatus() == ResourceStatus.ENABLED;
  }
}
