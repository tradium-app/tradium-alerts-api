package com.tradiumapp.swingtradealerts.services;

import com.tradiumapp.swingtradealerts.services.models.PolygonQuoteResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface PolygonService {

    @GET("/v2/aggs/grouped/locale/us/market/stocks/{day}?adjusted=true")
    Call<PolygonQuoteResponse> getQuotes(@Path("day") String day, @Query("apiKey") String apiKey);
}
