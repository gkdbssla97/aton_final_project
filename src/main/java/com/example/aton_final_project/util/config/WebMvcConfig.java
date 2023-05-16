package com.example.aton_final_project.util.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);
        registry
                .addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "classpath:/templates/"
//                        , "classpath:/uploaded_files/"
//                        , "file:///C:/homework/aton_final_project/src/main/resources/uploaded_files/"
                        , "file:src/main/resources/uploaded_files/"
                );
    }
}
