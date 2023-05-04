package com.example.aton_final_project.util;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(title = "개발자 페이지 API 명세서",
                description = "로그인 서비스 API 명세서",
                version = "v1"))
@RequiredArgsConstructor
@Configuration
public class Swagger2Config {

    @Bean
    public GroupedOpenApi chatOpenApi() {
        String[] paths = {"/**"};

        return GroupedOpenApi.builder()
                .group("로그인 서비스 API v1")
                .pathsToMatch(paths)
                .build();
    }

}
