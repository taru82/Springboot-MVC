package com.sample.data.user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private String firstName;
    private String lastName;
    private String country;
    private String street;
    private String city;
    private String state;
    private String zipcode;
    private String createdBy;
    private String modifiedBy;
}