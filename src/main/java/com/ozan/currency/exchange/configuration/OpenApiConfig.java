package com.ozan.currency.exchange.configuration;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Currency Exchange microservice REST API Documentation",
                description = "Ozan's Currency Exchange microservice REST API Documentation",
                version = "v1",
                contact = @Contact(
                        name = "Ozan Kilinc",
                        email = "ozan.kilinc@example.com",
                        url = "http://localhost:8080"
                ),
                license = @License(
                        name = "Apache 2.0"
                )
        ),
        externalDocs = @ExternalDocumentation(
                description =  "Ozan's Currency Exchange microservice REST API Documentation"
        )
)
@Configuration
public class OpenApiConfig {
}
