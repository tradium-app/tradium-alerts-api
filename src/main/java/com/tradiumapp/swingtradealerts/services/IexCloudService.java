package com.tradiumapp.swingtradealerts.services;

import com.tradiumapp.swingtradealerts.models.Stock;
import com.tradiumapp.swingtradealerts.services.models.IexcloudEarningsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface IexCloudService {
    @GET("/beta/ref-data/symbols")
    Call<List<Stock>> listStocks(@Query("token") String apiToken);

    @GET("/beta/stock/market/upcoming-earnings")
    Call<List<IexcloudEarningsResponse>> fetchUpcomingEarnings(@Query("from") String from, @Query("to") String to,
                                                               @Query("token") String apiToken);
}
