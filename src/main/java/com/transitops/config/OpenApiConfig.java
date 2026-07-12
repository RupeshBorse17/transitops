package com.transitops.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI transitOpsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("TransitOps API")
                        .version("1.0.0")
                        .description("Smart Transport Operations Platform backend APIs"));
    }
}
