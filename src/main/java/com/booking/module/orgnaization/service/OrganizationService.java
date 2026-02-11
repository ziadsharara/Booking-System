package com.booking.module.orgnaization.service;

import com.booking.module.orgnaization.dto.CreateOrgDTO;
import com.booking.module.orgnaization.dto.UpdateOrdDTO;
import com.booking.module.orgnaization.entity.Organization;
import com.booking.module.orgnaization.mapper.OrganizationMapper;
import com.booking.module.orgnaization.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationService {

  private final OrganizationRepository organizationRepository;
  private final OrganizationMapper organizationMapper;

  // Create Organization
  @Transactional
  public Organization createOrganization(CreateOrgDTO createOrgDTO) {
    if (organizationRepository.existsByName(createOrgDTO.getName())) {
      throw new IllegalArgumentException("Name already exist!");
    }

    Organization organization = organizationMapper.toEntity(createOrgDTO);

    return organizationRepository.save(organization);
  }

  public Organization getOrganizationById(Long id) {
    return organizationRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("There's no Organization with this ID"));
  }

  public List<Organization> getAllOrganization() {
    return organizationRepository.findAll();
  }

  public Organization getOrganizationByName(String name) {
    if (!organizationRepository.existsByName(name))
      throw new IllegalArgumentException("There's no Organization with this name!");

    return organizationRepository.findByName(name);
  }

  public Organization updateOrganization(Long id, UpdateOrdDTO updateOrdDTO) {
    Organization organization = organizationRepository.getOrganizationsById(id);

    Organization updates = organizationMapper.toEntity(updateOrdDTO);

    if (updates.getName() != null && !updates.getName().isBlank() && !organization.getName().equals(updates.getName())) {
      organization.setName(updates.getName());
    }

    return organizationRepository.save(organization);
  }

  public void deleteOrganization(Long id) {
    if (!organizationRepository.existsById(id))
      throw new IllegalArgumentException("There's no organization with this ID");
    organizationRepository.deleteById(id);
  }
}
