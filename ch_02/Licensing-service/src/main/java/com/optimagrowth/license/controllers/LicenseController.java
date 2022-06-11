package com.optimagrowth.license.controllers;

import com.optimagrowth.license.models.License;
import com.optimagrowth.license.services.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    return ResponseEntity.ok(license);
  }

  @PutMapping
  public ResponseEntity<String> updateLicense(
      @PathVariable("organizationId") String organizationId,
      @RequestBody License license)
  {
    return ResponseEntity.ok(this.licenseService.updateLicense(license, organizationId));
  }

  @PostMapping
  public ResponseEntity<String> createLicense(
    @PathVariable("organizationId") String organizationId,
    @RequestBody License license)
  {

    return ResponseEntity.ok(this.licenseService.createLicense(license, organizationId));
  }

  @DeleteMapping(value = "/{licenseId}")
  public ResponseEntity<String> deleteLicense(
      @PathVariable("organizationId") String organizationId,
      @PathVariable("licenseId") String licenseId
  ) {
    return ResponseEntity.ok(this.licenseService.deleteLicense(licenseId, organizationId));
  }
}
