package com.tradiumapp.swingtradealerts.scheduledtasks;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Configuration
public class IexCloudServiceConfig {

    @Value("${IEX_API_ENV}")
    private String iexEnv;

    @Bean
    public IexCloudService iexcloudService() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://" + iexEnv + ".iexapis.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(IexCloudService.class);
    }
}
