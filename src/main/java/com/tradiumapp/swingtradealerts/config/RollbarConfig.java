package com.tradiumapp.swingtradealerts.config;

import com.rollbar.notifier.Rollbar;
import com.rollbar.notifier.config.Config;
import com.rollbar.spring.webmvc.RollbarSpringConfigBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration
@EnableWebMvc
public class RollbarConfig {

    @Value("${ROLLBAR_ACCESS_TOKEN:dummy_token}")
    private String ROLLBAR_ACCESS_TOKEN;
    /**
     * Register a Rollbar bean to configure App with Rollbar.
     */
    @Bean
    public Rollbar rollbar() {
        return new Rollbar(getRollbarConfigs(ROLLBAR_ACCESS_TOKEN));
    }

    private Config getRollbarConfigs(String accessToken) {
        return RollbarSpringConfigBuilder.withAccessToken(accessToken)
                .environment("development")
                .build();
    }
}