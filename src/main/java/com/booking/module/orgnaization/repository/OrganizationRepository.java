package com.booking.module.orgnaization.repository;

import com.booking.module.orgnaization.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

  boolean existsByName(String name);

  Organization findByName(String name);

  List<Organization> findAll();


  Organization getOrganizationsById(Long id);
}
