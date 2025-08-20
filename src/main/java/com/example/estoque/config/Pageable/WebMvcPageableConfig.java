package com.example.estoque.config.Pageable;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcPageableConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        SanitizingPageableResolver resolver = new SanitizingPageableResolver();
        resolver.setMaxPageSize(100);
        resolver.setFallbackPageable(PageRequest.of(0, 20, Sort.by("createdAt").descending()));
        resolvers.add(resolver);
    }
}
