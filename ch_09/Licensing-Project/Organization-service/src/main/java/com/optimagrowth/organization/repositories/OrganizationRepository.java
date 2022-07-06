package com.optimagrowth.organization.repositories;

import com.optimagrowth.organization.models.Organization;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrganizationRepository extends CrudRepository<Organization, String> {

  public Optional<Organization> findById(String organizationId);
}
