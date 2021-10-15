package com.tradiumapp.swingtradealerts.services;

import com.tradiumapp.swingtradealerts.services.models.TipranksDataResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TipranksService {

    @GET("api/stocks/getData?benchmark=1&period=3")
    Call<TipranksDataResponse> getData(@Query("name") String symbol, @Query("break") String epochTime);
}
