package com.haykz.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
     * @return a new instance of ModelMapper
     */
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
