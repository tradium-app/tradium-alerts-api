package com.tradiumapp.swingtradealerts.scheduledtasks;

import com.tradiumapp.swingtradealerts.models.Stock;

import java.util.List;

public class PolygonQuoteResponse {
    public Boolean adjusted;
    public Integer resultsCount;
    public List<Stock.StockPrice> results;
}

