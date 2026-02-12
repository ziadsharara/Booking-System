package com.booking.module.resource.controller;

import com.booking.module.auth.util.JwtUtil;
import com.booking.module.resource.dto.ChangeResourceStatusDTO;
import com.booking.module.resource.dto.CreateResourceDTO;
import com.booking.module.resource.dto.GetResourceDTO;
import com.booking.module.resource.dto.UpdateResourceDTO;
import com.booking.module.resource.entity.Resource;
import com.booking.module.resource.mapper.ResourceMapper;
import com.booking.module.resource.service.ResourceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
@RequiredArgsConstructor
public class ResourceController {

  private final ResourceMapper resourceMapper;
  private final ResourceService resourceService;
  private final JwtUtil jwtUtil;

  //  Extract organizationId from JWT token
  private Long getManagerOrganizationId() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String token = (String) auth.getCredentials();
    Long organizationId = jwtUtil.getOrganizationIdFromToken(token);

    if (organizationId == null) {
      throw new IllegalArgumentException("Organization ID not found in token");
    }

    return organizationId;
  }

  /**
   * Create Resource - Only MANAGER can create
   * Must belong to their organization
   */
  @PreAuthorize("hasRole('MANAGER')")
  @PostMapping
  public ResponseEntity<GetResourceDTO> createResource(@Valid @RequestBody CreateResourceDTO dto) {
    try {
      // Get manager's organization ID
      Long organizationId = getManagerOrganizationId();

      // Ensure resource is created in manager's organization
      if (!organizationId.equals(dto.getOrganizationId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();  // Can't create resources in other orgs
      }

      Resource resource = resourceService.createResource(dto);
      GetResourceDTO resourceDTO = resourceMapper.toGetResourceDTO(resource);

      return ResponseEntity.status(HttpStatus.CREATED).body(resourceDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  /**
   * Update Resource - Only MANAGER can update
   * Must be from their organization
   */
  @PreAuthorize("hasRole('MANAGER')")
  @PutMapping("/{id}")
  public ResponseEntity<GetResourceDTO> updateResource(
          @PathVariable Long id,
          @Valid @RequestBody UpdateResourceDTO dto) {
    try {
      // Get manager's organization ID
      Long organizationId = getManagerOrganizationId();

      Resource resource = resourceService.getResourceById(id);

      // Ensure resource belongs to manager's organization
      if (!organizationId.equals(resource.getOrganizationId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();  // Can't update resources from other orgs
      }

      Resource updatedResource = resourceService.updateResource(id, dto);
      GetResourceDTO responseDTO = resourceMapper.toGetResourceDTO(updatedResource);

      return ResponseEntity.ok(responseDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Get Resource by ID - MANAGER and EMPLOYEE can view
   * MANAGER: can view resources from their org
   * EMPLOYEE: can view resources from their org
   */
  @PreAuthorize("hasAnyRole('MANAGER', 'EMPLOYEE')")
  @GetMapping("/{id}")
  public ResponseEntity<GetResourceDTO> getResourceById(@PathVariable Long id) {
    try {
      // Get user's organization ID
      Long organizationId = getManagerOrganizationId();

      Resource resource = resourceService.getResourceById(id);

      // Ensure resource belongs to user's organization
      if (!organizationId.equals(resource.getOrganizationId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();  // Can't view resources from other orgs
      }

      GetResourceDTO responseDTO = resourceMapper.toGetResourceDTO(resource);
      return ResponseEntity.ok(responseDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Get All Resources in Organization - MANAGER and EMPLOYEE can view
   * MANAGER: can see all resources in their org
   * EMPLOYEE: can see all resources in their org
   */
  @PreAuthorize("hasAnyRole('MANAGER', 'EMPLOYEE')")
  @GetMapping("/organization/my-org/resources")
  public ResponseEntity<List<GetResourceDTO>> getAllResourcesOfMyOrganization() {
    try {
      // Get user's organization ID
      Long organizationId = getManagerOrganizationId();

      List<Resource> resources = resourceService.getAllResourcesByOrganizationId(organizationId);
      List<GetResourceDTO> responseDTOS = resourceMapper.getGetResourceList(resources);

      return ResponseEntity.ok(responseDTOS);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Get Resource by Name - MANAGER and EMPLOYEE can view
   * Must be from their organization
   */
  @PreAuthorize("hasAnyRole('MANAGER', 'EMPLOYEE')")
  @GetMapping("/name/{name}")
  public ResponseEntity<GetResourceDTO> getResourceByName(@PathVariable String name) {
    try {
      // Get user's organization ID
      Long organizationId = getManagerOrganizationId();

      Resource resource = resourceService.getResourceByName(name);

      // Ensure resource belongs to user's organization
      if (!organizationId.equals(resource.getOrganizationId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }

      GetResourceDTO responseDTO = resourceMapper.toGetResourceDTO(resource);
      return ResponseEntity.ok(responseDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Delete Resource - Only MANAGER can delete
   * Must be from their organization
   */
  @PreAuthorize("hasRole('MANAGER')")
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteResource(@PathVariable Long id) {
    try {
      // Get manager's organization ID
      Long organizationId = getManagerOrganizationId();

      Resource resource = resourceService.getResourceById(id);

      // Ensure resource belongs to manager's organization
      if (!organizationId.equals(resource.getOrganizationId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();  // Can't delete resources from other orgs
      }

      resourceService.deleteResource(id);
      return ResponseEntity.ok("Resource deleted successfully");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Delete All Resources in Organization - Only MANAGER can delete
   */
  @PreAuthorize("hasRole('MANAGER')")
  @DeleteMapping("/organization/my-org/delete-all")
  public ResponseEntity<String> deleteAllResourcesOfMyOrganization() {
    try {
      // Get manager's organization ID
      Long organizationId = getManagerOrganizationId();

      resourceService.deleteAllResourcesByOrganizationId(organizationId);
      return ResponseEntity.ok("All resources of your organization deleted successfully");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  /**
   * Toggle Resource Status - Only MANAGER can toggle
   * Must be from their organization
   */
  @PreAuthorize("hasRole('MANAGER')")
  @PutMapping("/{id}/toggle-status")
  public ResponseEntity<ChangeResourceStatusDTO> toggleResourceStatus(@PathVariable Long id) {
    try {
      // Get manager's organization ID
      Long organizationId = getManagerOrganizationId();

      Resource resource = resourceService.getResourceById(id);

      // Ensure resource belongs to manager's organization
      if (!organizationId.equals(resource.getOrganizationId())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }

      Resource toggledResource = resourceService.toggleResourceStatus(id);
      ChangeResourceStatusDTO responseDTO = resourceMapper.toGetResourceStatusDTO(toggledResource);
      return ResponseEntity.ok(responseDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }
}