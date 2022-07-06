package com.optimagrowth.license.services;

import com.optimagrowth.license.configs.ServiceConfig;
import com.optimagrowth.license.models.License;
import com.optimagrowth.license.models.Organization;
import com.optimagrowth.license.repositories.LicenseRepository;
import com.optimagrowth.license.services.client.OrganizationDiscoveryClient;
import com.optimagrowth.license.services.client.OrganizationFeignClient;
import com.optimagrowth.license.services.client.OrganizationTemplateClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

@Service
public class LicenseService {

  private static final Logger logger = LoggerFactory.getLogger(LicenseService.class);

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

  @CircuitBreaker(name = "licenseService",
    fallbackMethod = "buildFallbackLicenseList")
  public List<License> getLicensesByOrganization(String organizationId) throws TimeoutException {
    randomlyRunLong();
    return this.licenseRepository.findByOrganizationId(organizationId);
  }

  private List<License> buildFallbackLicenseList(String organizationId, Throwable t) {
    List<License> fallbackList = new ArrayList<>();
    License license = new License();
    license.setLicenseId("0000000-00-00000");
    license.setOrganizationId(organizationId);
    license.setProductName("Sorry no licensing information currently available");
    fallbackList.add(license);
    return fallbackList;
  }

  private void randomlyRunLong() throws TimeoutException{
    Random rand = new Random();
    int randomNum = rand.nextInt(3) + 1;
    if (randomNum == 3) sleep();
  }

  private void sleep() throws TimeoutException {
    try {
      System.out.println("Sleep");
      Thread.sleep(5000);
      throw new java.util.concurrent.TimeoutException();
    }
    catch(InterruptedException e) {
      logger.error(e.getMessage());
    }
  }
}
