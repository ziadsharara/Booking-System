package com.booking.module.orgnaization.repository;

import com.booking.module.orgnaization.entity.Organization;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

  boolean existsByName(String name);

  Optional<Organization> findByName(String name);

  Page<Organization> findAll(Pageable pageable);
}
