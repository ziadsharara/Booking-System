package com.booking.module.resource.repository;

import com.booking.module.resource.entity.Resource;
import com.booking.module.resource.entity.ResourceStatus;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

  boolean existsByNameAndOrganizationId(String name, Long organizationId);

  boolean existsByName(String name);

  Optional<Resource> findByName(String name);

  List<Resource> findAllByStatus(ResourceStatus status);

  List<Resource> findAllByOrganizationId(Long organizationId);

  Resource getResourcesById(Long id);
}
