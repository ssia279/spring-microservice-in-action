package com.optimagrowth.license.models.utils;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseWrapper {

  private Object data;
  private Object metadata;
  private List<ErrorMessage> errors;

  public ResponseWrapper(Object data, Object metadata, List<ErrorMessage> errors) {
    super();
    this.data = data;
    this.metadata = metadata;
    this.errors = errors;
  }

  public Object getData() {
    return this.data;
  }

  public void setData(Object data) {
    this.data = data;
  }

  public Object getMetadata() {
    return this.metadata;
  }

  public void setMetadata(Object metadata) {
    this.metadata = metadata;
  }

  public List<ErrorMessage> getErrors() {
    return this.errors;
  }

  public void setErrors(List<ErrorMessage> errors) {
    this.errors = errors;
  }
}
