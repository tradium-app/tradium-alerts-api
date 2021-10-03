package com.tradiumapp.swingtradealerts.services;

import com.tradiumapp.swingtradealerts.services.models.ApeWisdomResponse;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

public interface ApeWisdomService {
    @GET("/api/v1.0/filter/all-stocks/page/1")
    Call<ApeWisdomResponse> getTopTrendingStocks();
}
