package com.tradiumapp.swingtradealerts.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class PolygonServiceConfig {

    @Bean
    public PolygonService polygonService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.polygon.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(PolygonService.class);
    }
}
