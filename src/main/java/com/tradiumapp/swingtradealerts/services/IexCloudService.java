package com.tradiumapp.swingtradealerts.services;

import com.tradiumapp.swingtradealerts.models.Stock;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.HashMap;
import java.util.List;

public interface IexCloudService {
    @GET("/beta/ref-data/symbols")
    Call<List<Stock>> listStocks(@Query("token") String apiToken);

//    @GET("/beta/stock/market/batch?types=quote&chartByDay=true&last=1")
//    Call<HashMap<String,HashMap<String,Stock>>> getQuotes(@Query("symbols") String symbols, @Query("token") String apiToken);
}
