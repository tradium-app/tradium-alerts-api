package com.tradiumapp.swingtradealerts.services.models;

import com.tradiumapp.swingtradealerts.models.Stock;
import com.tradiumapp.swingtradealerts.models.StockHistory;

import java.util.List;

public class PolygonQuoteResponse {
    public Boolean adjusted;
    public Integer resultsCount;
    public List<StockHistory.StockPrice> results;
}

