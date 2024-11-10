package com.haykz.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

/**
 * This is the configuration class for setting up beans in the application context.
 * It contains the configuration for the ModelMapper bean, which is used for object mapping.
 *
 * ModelMapper simplifies the process of mapping one object to another, typically used for
 * converting between DTOs and entities in the application.
 */
@Configuration
public class AppConfig {

    /**
     * Provides the ModelMapper bean to the Spring context.
     * The ModelMapper is used to map objects from one type to another, which is particularly useful
     * for mapping between entities and DTOs.
     *
     * Example usage:
     * - Mapping from an Entity to a DTO: `modelMapper.map(entity, MyDto.class)`
     * - Mapping from a DTO to an Entity: `modelMapper.map(dto, MyEntity.class)`
     *
     * @return a new instance of ModelMapper, which can be used for object mapping
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    /**
     * Provides a RequestMatcher bean to match specific paths for security-related purposes.
     * This matcher is typically used to define custom patterns for paths that need special security configurations.
     * In this case, the request matcher is configured to match the "/user/clothes/**" pattern, which could be used
     * to bypass security or apply specific security rules for these endpoints.
     *
     * Example usage:
     * - Used for custom request matching in Spring Security configuration to determine which requests need authentication.
     *
     * @return a RequestMatcher that matches the "/user/clothes/**" path
     */
    @Bean
    public RequestMatcher ignoredPaths() {
        return new AntPathRequestMatcher("/user/clothes/**");
    }
}
