package com.tradiumapp.swingtradealerts.services;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class TipranksServiceConfig {

    @Bean
    public TipranksService tipranksService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.tipranks.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(TipranksService.class);
    }
}
