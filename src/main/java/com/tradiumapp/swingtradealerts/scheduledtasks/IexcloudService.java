package com.tradiumapp.swingtradealerts.scheduledtasks;

import com.tradiumapp.swingtradealerts.models.Stock;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface IexcloudService {
    @GET("/beta/ref-data/symbols")
    Call<List<Stock>> listStocks(@Query("token") String apiToken);
}
