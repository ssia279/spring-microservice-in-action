package com.optimagrowth.organization.controllers;

import com.optimagrowth.organization.models.Organization;
import com.optimagrowth.organization.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;

@Controller
@RequestMapping(value = "v1/organization")
public class OrganizationController {

  @Autowired
  private OrganizationService service;

  @RolesAllowed({ "ADMIN", "USER" })
  @RequestMapping(value = "/{organizationId}", method = RequestMethod.GET)
  public ResponseEntity<Organization> getOrganization(@PathVariable("organizationId") String organizationId) {
    return ResponseEntity.ok(this.service.findById(organizationId));
  }

  @RolesAllowed({ "ADMIN", "USER" })
  @RequestMapping(value = "/{organizationId}", method = RequestMethod.PUT)
  public void updateOrganization(@PathVariable("organizationId") String organizationId, @RequestBody Organization organization) {
    this.service.update(organization);
  }

  @RolesAllowed({ "ADMIN", "USER" })
  @PostMapping
  public ResponseEntity<Organization> saveOrganization(@RequestBody Organization organization) {
    return ResponseEntity.ok(this.service.create(organization));
  }

  @RolesAllowed({ "ADMIN" })
  @RequestMapping(value = "/{organizationId}", method = RequestMethod.DELETE)
  public void deleteOrganization(@PathVariable("organizationId") String organizationId, @RequestBody Organization organization) {
    this.service.delete(organization);
  }
}
