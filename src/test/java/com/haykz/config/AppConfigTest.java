package com.haykz.config;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Import(AppConfig.class)
public class AppConfigTest {

    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void contextLoads() {
        assertNotNull(modelMapper, "ModelMapper bean should not be null");
    }
}
