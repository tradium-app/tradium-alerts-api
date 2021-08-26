package com.tradiumapp.swingtradealerts.services;

import com.tradiumapp.swingtradealerts.models.Stock;
import com.tradiumapp.swingtradealerts.scheduledtasks.PolygonQuoteResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.HashMap;
import java.util.List;

public interface PolygonService {

    @GET("/v2/aggs/grouped/locale/us/market/stocks/{day}?adjusted=true")
    Call<PolygonQuoteResponse> getQuotes(@Path("day") String day, @Query("apiKey") String apiKey);
}
