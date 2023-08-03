package com.group3.persobudgetmanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI usersMicroserviceOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("PeroBudgetManager")
                        .description("PeroBudgetManager est une API crée par les apprenant de ODK3(Orange Digital Center) qui sont.\nL'objectif était de leur permettre d'ameliorer leur competence en création d'API avec Spring Boot.")
                        .version("1.0"));
    }
}
