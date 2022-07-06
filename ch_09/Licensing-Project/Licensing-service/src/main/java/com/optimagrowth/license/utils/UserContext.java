package com.optimagrowth.license.utils;

import org.springframework.stereotype.Component;

@Component
public class UserContext {
  public static final String CORRELATION_ID = "tmx-correlation-id";
  public static final String AUTH_TOKEN = "tmx-auth-token";
  public static final String USER_ID = "tmx-user-id";
  public static final String ORGANIZATION_ID = "tmx-organization-id";

  private String correctionId = new String();
  private String authToken = new String();
  private String userId = new String();
  private String organizationId = new String();

  public String getCorrelationId() { return this.correctionId; }
  public void setCorrelationId(String correctionId) {
    this.correctionId = correctionId;
  }

  public String getAuthToken() {
    return this.authToken;
  }

  public void setAuthToken(String authToken) {
    this.authToken = authToken;
  }

  public String getUserId() {
    return this.userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getOrganizationId() {
    return this.organizationId;
  }

  public void setOrganizationId(String organizationId) {
    this.organizationId = organizationId;
  }

}
