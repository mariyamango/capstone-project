package com.example.backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    public static final String FORWARD_INDEX_HTML = "forward:/index.html";

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/car/{id}").setViewName(FORWARD_INDEX_HTML);
        registry.addViewController("/about").setViewName(FORWARD_INDEX_HTML);
        registry.addViewController("/chuck").setViewName(FORWARD_INDEX_HTML);
        registry.addViewController("/").setViewName(FORWARD_INDEX_HTML);
    }
}
