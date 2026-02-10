package com.booking.module.resource.repository;

import com.booking.module.resource.entity.Resource;
import com.booking.module.resource.entity.ResourceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

  boolean existsByNameAndOrganizationId(String name, Long organizationId);

  // "Select * from resources where organization_id = ?"
  Page<Resource> findAllByOrganizationId(Long organizationId, Pageable pageable);

  // "Select * from resources where organization_id = ? AND status = ?"
  Page<Resource> findAllByOrganizationIdAndStatus(Long organizationId, ResourceStatus status, Pageable pageable);

}
