package com.johnsonwang.microservice;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HelloRequest {
  private String firstName;
  private String lastName;
}
