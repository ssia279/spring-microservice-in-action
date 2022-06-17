package com.optimagrowth.license.services;

import com.optimagrowth.license.configs.ServiceConfig;
import com.optimagrowth.license.models.License;
import com.optimagrowth.license.repositories.LicenseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LicenseService {

  @Autowired
  private MessageSource messageSource;
  @Autowired
  private LicenseRepository licenseRepository;
  @Autowired
  ServiceConfig config;

  public License getLicense(String licenseId, String organizationId) {
    License license = this.licenseRepository.findByOrganizationIdAndLicenseId(organizationId, licenseId);
    if (license == null) {
      throw new IllegalArgumentException(String.format(messageSource.getMessage("license.search.error.message", null, null), licenseId, organizationId));
    }

    return license.withComment(config.getProperty());
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
}
