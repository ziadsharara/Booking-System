package com.booking.module.orgnaization.controller;

import com.booking.module.orgnaization.dto.CreateOrgDTO;
import com.booking.module.orgnaization.dto.GetOrgDTO;
import com.booking.module.orgnaization.dto.UpdateOrdDTO;
import com.booking.module.orgnaization.entity.Organization;
import com.booking.module.orgnaization.mapper.OrganizationMapper;
import com.booking.module.orgnaization.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/organization")
@RequiredArgsConstructor
public class OrganizationController {

  private final OrganizationService organizationService;
  private final OrganizationMapper organizationMapper;

  // Create Organization
  @PostMapping
  public ResponseEntity<GetOrgDTO> createOrganization(@Valid @RequestBody CreateOrgDTO createOrgDTO) {
    try {
      Organization organization = organizationService.createOrganization(createOrgDTO);
      GetOrgDTO getOrgDTO = organizationMapper.toGetOrgDTO(organization);

      return ResponseEntity.status(HttpStatus.CREATED).body(getOrgDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  // Get Organization by id
  @GetMapping("/{id}")
  public ResponseEntity<GetOrgDTO> getorgByID(@PathVariable Long id) {
    try {
      Organization organization = organizationService.getOrganizationById(id);
      GetOrgDTO getOrgDTO = organizationMapper.toGetOrgDTO(organization);

      return ResponseEntity.ok(getOrgDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  // Get all organizations
  @GetMapping
  public ResponseEntity<List<GetOrgDTO>> getAllOrgs() {
    List<Organization> organizations = organizationService.getAllOrganization();
    List<GetOrgDTO> orgDTOS = organizationMapper.toGetOrgsDTOList(organizations);

    return ResponseEntity.ok(orgDTOS);
  }

  @GetMapping("/name/{name}")
  public ResponseEntity<GetOrgDTO> getOrgByName(@PathVariable String name) {
    try {
      Organization organization = organizationService.getOrganizationByName(name);
      GetOrgDTO getOrgDTO = organizationMapper.toGetOrgDTO(organization);

      return ResponseEntity.ok(getOrgDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<GetOrgDTO> updateOrg(@PathVariable Long id, @Valid @RequestBody UpdateOrdDTO updateOrdDTO) {
    try {
      Organization organization = organizationService.updateOrganization(id, updateOrdDTO);
      GetOrgDTO getOrgDTO = organizationMapper.toGetOrgDTO(organization);

      return ResponseEntity.ok(getOrgDTO);
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<GetOrgDTO> deleteOrg(@PathVariable Long id) {
    try {
      organizationService.deleteOrganization(id);
      return ResponseEntity.noContent().build();  // 204 No Content
    } catch (IllegalArgumentException e) {
      return ResponseEntity.notFound().build();   // 404 Not Found
    }
  }
}
