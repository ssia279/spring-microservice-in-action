package com.optimagrowth.license.services.client;

import com.optimagrowth.license.models.Organization;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrganizationTemplateClient {

  @Autowired
  private KeycloakRestTemplate restTemplate;

  public Organization getOrganization(String organizationId){
    ResponseEntity<Organization> restExchange =
        restTemplate.exchange(
            "http://gateway:8072/organization/v1/organization/{organizationId}",
            HttpMethod.GET,
            null, Organization.class, organizationId);

    return restExchange.getBody();
  }
}
