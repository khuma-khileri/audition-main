package com.audition.config;

import com.audition.common.logging.AuditionLogger;
import com.audition.configuration.LoggingInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestConfig {

    @Bean
    public AuditionLogger auditionLogger() {
        return new AuditionLogger();
    }

    @Bean
    public LoggingInterceptor loggingInterceptor() {
        return new LoggingInterceptor();
    }
}
