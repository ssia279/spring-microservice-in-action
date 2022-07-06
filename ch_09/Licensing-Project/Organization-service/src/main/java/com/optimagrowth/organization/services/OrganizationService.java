package com.optimagrowth.organization.services;

import com.optimagrowth.organization.models.Organization;
import com.optimagrowth.organization.repositories.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class OrganizationService {

  @Autowired
  private OrganizationRepository repository;

  public Organization findById(String organizationId) {
    Optional<Organization> opt = this.repository.findById(organizationId);

    return (opt.isPresent()) ? opt.get() : null;
  }

  public Organization create(Organization organization) {
    organization.setId(UUID.randomUUID().toString());
    organization = this.repository.save(organization);

    return organization;
  }

  public void update(Organization organization) {
    this.repository.save(organization);
  }

  public void delete(Organization organization) {
    this.repository.deleteById(organization.getId());
  }
}
