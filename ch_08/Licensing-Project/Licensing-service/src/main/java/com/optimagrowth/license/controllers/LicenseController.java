package com.optimagrowth.license.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.optimagrowth.license.models.License;
import com.optimagrowth.license.services.LicenseService;
import com.optimagrowth.license.utils.UserContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping(value = "v1/organization/{organizationId}/license")
public class LicenseController {

  private static final Logger logger = LoggerFactory.getLogger(LicenseController.class);

  @Autowired
  private LicenseService licenseService;

  @GetMapping(value = "/{licenseId}")
  public ResponseEntity<License> getLicense(
      @PathVariable("organizationId") String organizationId,
      @PathVariable("licenseId") String licenseId
  ) {

    License license = this.licenseService.getLicense(licenseId, organizationId, "");
    license.add(linkTo(methodOn(LicenseController.class)
        .getLicense(organizationId, license.getLicenseId()))
        .withSelfRel(),
        linkTo(methodOn(LicenseController.class).createLicense(license))
            .withRel("createLicense"),
    linkTo(methodOn(LicenseController.class).updateLicense(license))
        .withRel("updateLicense"),
    linkTo(methodOn(LicenseController.class).deleteLicense(license.getLicenseId()))
        .withRel("deleteLicense"));

    return ResponseEntity.ok(license);
  }

  @RequestMapping(value = "/{licenseId}/{clientType}", method = RequestMethod.GET)
  public License getLicensesWithClient(@PathVariable("organizationId") String organizationId,
                       @PathVariable("licenseId") String licenseId,
                       @PathVariable("clientType") String clientType) {
    return this.licenseService.getLicense(organizationId, licenseId, clientType);

  }

  @PutMapping
  public ResponseEntity<License> updateLicense(
      @RequestBody License license)
  {
    return ResponseEntity.ok(this.licenseService.updateLicense(license));
  }

  @PostMapping
  public ResponseEntity<License> createLicense(
      @RequestBody License license)
  {

    return ResponseEntity.ok(this.licenseService.createLicense(license));
  }

  @DeleteMapping(value = "/{licenseId}")
  public ResponseEntity<String> deleteLicense(
      @PathVariable("licenseId") String licenseId
  ) {
    return ResponseEntity.ok(this.licenseService.deleteLicense(licenseId));
  }

  @RequestMapping(value = "/", method = RequestMethod.GET)
  public List<License> getLicenses(@PathVariable("organizationId") String organizationId) throws TimeoutException {
    logger.debug("LicenseServiceController Correlation id: {}", UserContextHolder.getContext().getCorrelationId());

    return this.licenseService.getLicensesByOrganization(organizationId);
  }
}
