package com.tradiumapp.swingtradealerts.repositories;

import com.tradiumapp.swingtradealerts.models.Stock;
import com.tradiumapp.swingtradealerts.models.StockHistory;
import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StockHistoryRepository extends PagingAndSortingRepository<StockHistory, ObjectId> {
    Stock findBySymbol(String symbol);
}
