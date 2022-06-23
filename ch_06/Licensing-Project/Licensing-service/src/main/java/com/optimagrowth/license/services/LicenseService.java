package com.optimagrowth.license.services;

import com.optimagrowth.license.configs.ServiceConfig;
import com.optimagrowth.license.models.License;
import com.optimagrowth.license.models.Organization;
import com.optimagrowth.license.repositories.LicenseRepository;
import com.optimagrowth.license.services.client.OrganizationDiscoveryClient;
import com.optimagrowth.license.services.client.OrganizationFeignClient;
import com.optimagrowth.license.services.client.OrganizationTemplateClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LicenseService {

  @Autowired
  private MessageSource messageSource;
  @Autowired
  private LicenseRepository licenseRepository;
  @Autowired
  ServiceConfig config;

  @Autowired
  OrganizationFeignClient organizationFeignClient;

  @Autowired
  OrganizationTemplateClient organizationTemplateClient;

  @Autowired
  OrganizationDiscoveryClient organizationDiscoveryClient;

  public License getLicense(String licenseId, String organizationId, String clientType) {
    License license = this.licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
    if (license == null) {
      throw new IllegalArgumentException(String.format(messageSource.getMessage("license.search.error.message", null, null), licenseId, organizationId));
    }

    Organization organization = retrieveOrganizationInfo(organizationId, clientType);
    if (organization != null) {
      license.setOrganizationName(organization.getName());
      license.setContactName(organization.getContactName());
      license.setContactEmail(organization.getContactEmail());
      license.setContactPhone(organization.getContactPhone());
    }

    return license.withComment(config.getProperty());
  }

  private Organization retrieveOrganizationInfo(String organizationId, String clientType) {
    Organization organization = null;

    switch (clientType) {
      case "feign":
        System.out.println("I am using the feign client");
        organization = this.organizationFeignClient.getOrganization(organizationId);
        break;
      case "rest":
        System.out.println("I am using the rest client");
        organization = this.organizationTemplateClient.getOrganization(organizationId);
        break;
      case "discovery":
        System.out.println("I am using the discovery client");
        organization = this.organizationDiscoveryClient.getOrganization(organizationId);
        break;
      default:
        organization = this.organizationTemplateClient.getOrganization(organizationId);
        break;
    }

    return organization;
  }

  public License createLicense(License license) {
    license.setLicenseId(UUID.randomUUID().toString());
    this.licenseRepository.save(license);

    return license.withComment(config.getProperty());
  }

  public License updateLicense(License license) {
    this.licenseRepository.save(license);

    return license.withComment(config.getProperty());
  }

  public String deleteLicense(String licenseId) {
    String responseMessage = null;
    License license = new License();
    license.setLicenseId(licenseId);
    this.licenseRepository.delete(license);
    responseMessage = String.format(messageSource.getMessage("license.delete.message", null, null), licenseId);

    return responseMessage;
  }

  public List<License> getLicensesByOrganization(String organizationId) {
    return this.licenseRepository.findByOrganizationId(organizationId);
  }
}
