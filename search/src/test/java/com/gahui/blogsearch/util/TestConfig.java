package com.gahui.blogsearch.util;

import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Properties;

@TestConfiguration
public class TestConfig {
    @Bean
    BuildProperties buildProperties() {
        return new BuildProperties(new Properties());
    }
}
