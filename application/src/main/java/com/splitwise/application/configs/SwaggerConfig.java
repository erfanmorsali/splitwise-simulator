package com.splitwise.application.configs;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi allApi() {
        return GroupedOpenApi.builder()
                .group("All APIs")
                .pathsToMatch("/**")
                .addOpenApiCustomizer(openApi -> openApi.info(new Info()
                        .title("swagger")
                        .description("list of all APIs"))
                ).build();
    }
}