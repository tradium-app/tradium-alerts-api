package com.tradiumapp.swingtradealerts.repositories;

import com.tradiumapp.swingtradealerts.models.Stock;
import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface StockRepository extends PagingAndSortingRepository<Stock, ObjectId> {
    Stock findBySymbol(String symbol);
    List<Stock> findBySymbolIn(List<String> symbols);
}
