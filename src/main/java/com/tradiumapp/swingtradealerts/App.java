package com.tradiumapp.swingtradealerts;

import com.rollbar.notifier.Rollbar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableConfigurationProperties
@ComponentScan(basePackages = "com.tradiumapp.swingtradealerts")
public class App {

//	@Autowired
//	private static Rollbar rollbar;

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);

//		sampleClass.doSomething();
	}

}
