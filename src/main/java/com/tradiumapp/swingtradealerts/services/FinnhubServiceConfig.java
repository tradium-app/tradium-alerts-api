package com.tradiumapp.swingtradealerts.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class FinnhubServiceConfig {

    @Bean
    public FinnhubService finnhubService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://finnhub.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(FinnhubService.class);
    }
}