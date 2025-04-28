package com.sample.data;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Date;

@Configuration
@PropertySource("classpath:springdoc-default.properties")
public class SpringdocSwaggerConfig implements WebMvcConfigurer {


    private static final String BEARER_AUTH_SCHEME_NAME = "BearerAuth";
    @Autowired
    ApplicationContext context;

    @Bean
    public OpenApiCustomizer storeOpenApiCustomizer() {
        return swagger -> {
            // Add information about the API
            swagger.info(getApiInfo());

            // Add security requirements
            swagger.addSecurityItem(new SecurityRequirement()
                    .addList(BEARER_AUTH_SCHEME_NAME));

            swagger.getComponents()
                    .addSecuritySchemes(BEARER_AUTH_SCHEME_NAME,
                            new SecurityScheme()
                                    .type(SecurityScheme.Type.HTTP)
                                    .scheme("bearer")
                                    .type(SecurityScheme.Type.APIKEY)
                                    .in(SecurityScheme.In.HEADER)
                                    .name("Authorization")
                                    .description("OAuth Header")
                    );
        };
    }

    private Info getApiInfo() {
        return new Info()
                .title(context.getApplicationName())
                .version(new Date(context.getStartupDate()).toString());
    }
}

