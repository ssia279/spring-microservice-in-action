package com.optimagrowth.license.controllers;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import com.optimagrowth.license.models.License;
import com.optimagrowth.license.services.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

@RestController
@RequestMapping(value = "v1/organization/{organizationId}/license")
public class LicenseController {

  @Autowired
  private LicenseService licenseService;

  @GetMapping(value = "/{licenseId}")
  public ResponseEntity<License> getLicense(
      @PathVariable("organizationId") String organizationId,
      @PathVariable("licenseId") String licenseId
  ) {

    License license = this.licenseService.getLicense(licenseId, organizationId);
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
}
