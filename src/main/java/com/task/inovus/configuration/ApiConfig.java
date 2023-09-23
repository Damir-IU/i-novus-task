package com.task.inovus.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Random;

@Configuration
@EnableJpaAuditing
public class ApiConfig {
    @Bean
    public Random random() {
        return new Random();
    }
}