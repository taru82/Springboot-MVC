package com.sample.data.user.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.opencsv.bean.CsvBindByName;
import com.sample.data.util.CsvBindByNameOrder;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@CsvBindByNameOrder("FIRST_NAME, LAST_NAME, COUNTRY, STREET, CITY, STATE, ZIP_CODE")
public class UserUpdate {
    @CsvBindByName(column = "FIRST_NAME")
    private String firstName;

    @CsvBindByName(column = "LAST_NAME")
    private String lastName;

    @CsvBindByName(column = "COUNTRY")
    private String country;

    @CsvBindByName(column = "STREET")
    private String street;

    @CsvBindByName(column = "CITY")
    private String city;

    @CsvBindByName(column = "STATE")
    private String state;

    @CsvBindByName(column = "ZIP_CODE")
    private String zipcode;
}