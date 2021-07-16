package com.tradiumapp.swingtradealerts.repositories;

import com.tradiumapp.swingtradealerts.models.Stock;
import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface StockRepository extends PagingAndSortingRepository<Stock, ObjectId> {
}
