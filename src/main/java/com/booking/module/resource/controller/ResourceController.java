package com.booking.module.resource.controller;

import com.booking.module.orgnaization.service.OrganizationService;
import com.booking.module.resource.dto.ChangeResourceStatusDTO;
import com.booking.module.resource.dto.CreateResourceDTO;
import com.booking.module.resource.dto.GetResourceDTO;
import com.booking.module.resource.dto.UpdateResourceDTO;
import com.booking.module.resource.entity.Resource;
import com.booking.module.resource.mapper.ResourceMapper;
import com.booking.module.resource.repository.ResourceRepository;
import com.booking.module.resource.service.ResourceService;
import com.booking.module.user.mapper.UserMapper;
import com.booking.module.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
public class ResourceController {

  private final ResourceMapper resourceMapper;
  private final ResourceService resourceService;
  private final ResourceRepository resourceRepository;

  @PostMapping
  public ResponseEntity<GetResourceDTO> createResource(@Valid @RequestBody CreateResourceDTO dto) {
    try {
      Resource resource = resourceService.createResource(dto);
      GetResourceDTO resourceDTO = resourceMapper.toGetResourceDTO(resource);

      return ResponseEntity.status(HttpStatus.CREATED).body(resourceDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<GetResourceDTO> updateResource(@PathVariable Long id, @Valid @RequestBody UpdateResourceDTO dto) {
    try {
      Resource resource = resourceService.updateResource(id, dto);

      if (dto.getName() != null && !dto.getName().isBlank() && !dto.getName().equals(resource.getName())) {
        if (resourceRepository.existsByName(dto.getName())) {
          throw new IllegalArgumentException("The name of resource already exist");
        }
      }
      GetResourceDTO responseDTO = resourceMapper.toGetResourceDTO(resource);

      return ResponseEntity.ok(responseDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<GetResourceDTO> getResourceById(@PathVariable Long id) {
    try {
      Resource resource = resourceService.getResourceById(id);
      GetResourceDTO responseDTO = resourceMapper.toGetResourceDTO(resource);

      return ResponseEntity.ok(responseDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/org/{orgId}")
  public ResponseEntity<List<GetResourceDTO>> getAllResourcesByOrganizationId(@PathVariable Long orgId) {
    try {
      List<Resource> resources = resourceService.getAllResourcesByOrganizationId(orgId);
      List<GetResourceDTO> responseDTOS = resourceMapper.getGetResourceList(resources);

      return ResponseEntity.ok(responseDTOS);
    } catch (IllegalArgumentException e) {
      return  ResponseEntity.notFound().build();
    }
  }

  @GetMapping("/name/{name}")
  public ResponseEntity<GetResourceDTO> getResourceByName(@PathVariable String name) {
    try {
      Resource resource = resourceService.getResourceByName(name);
      GetResourceDTO responseDTO = resourceMapper.toGetResourceDTO(resource);

      return ResponseEntity.ok(responseDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity deleteResource(@PathVariable Long id) {
    resourceService.deleteResource(id);

    return ResponseEntity.ok().body("Resource deleted successfully");
  }

  @DeleteMapping("/delete/org/{orgId}")
  public ResponseEntity<String> deleteAllResourcesByOrganizationId(@PathVariable Long orgId) {
    try {
      resourceService.deleteAllResourcesByOrganizationId(orgId);
      return ResponseEntity.ok("All resources deleted successfully");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}/toggle-status")
  public ResponseEntity<ChangeResourceStatusDTO> toggleResourceStatus(@PathVariable Long id) {
    try {
      Resource resource = resourceService.toggleResourceStatus(id);
      ChangeResourceStatusDTO responseDTO = resourceMapper.toGetResourceStatusDTO(resource);
      return ResponseEntity.ok(responseDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }


}
