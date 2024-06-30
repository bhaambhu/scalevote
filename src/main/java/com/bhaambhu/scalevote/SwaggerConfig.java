package com.bhaambhu.scalevote;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
// import io.swagger.v3.oas.models.OpenAPI;
// import io.swagger.v3.oas.models.info.Info;
// import io.swagger.v3.oas.models.info.License;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                .title("ScaleVote API")
                .version("1.0")
                .description("API documentation for the ScaleVote project - A minimal election conducting system put up in Spring Boot for experimenting with scalability and real-time processing capabilities of Apache Kafka.")
                .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}
