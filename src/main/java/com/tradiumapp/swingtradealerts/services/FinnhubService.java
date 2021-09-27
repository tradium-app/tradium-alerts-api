package com.tradiumapp.swingtradealerts.services;

import com.tradiumapp.swingtradealerts.models.Stock;
import com.tradiumapp.swingtradealerts.services.models.FinnhubMetricResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FinnhubService {

    @GET("/api/v1//stock/metric?metric=all")
    Call<FinnhubMetricResponse> getStockMetrics(@Query("symbol") String symbol, @Query("token") String token);
}
