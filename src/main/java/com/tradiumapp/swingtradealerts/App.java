package com.tradiumapp.swingtradealerts;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@EnableConfigurationProperties
@EnableMongoAuditing
@ComponentScan(basePackages = "com.tradiumapp.swingtradealerts")
@SpringBootApplication(exclude = EmbeddedMongoAutoConfiguration.class)
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}
}
