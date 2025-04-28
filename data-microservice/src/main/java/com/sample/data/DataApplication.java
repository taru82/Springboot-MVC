package com.sample.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@Slf4j
@ComponentScan(basePackages = {"com.sample"})
public class DataApplication {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(DataApplication.class);
        app.setAdditionalProfiles("local");
        app.run(args);
    }

}