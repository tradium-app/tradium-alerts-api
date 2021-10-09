package com.tradiumapp.swingtradealerts.repositories;

import com.tradiumapp.swingtradealerts.models.Article;
import org.bson.types.ObjectId;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ArticleRepository extends PagingAndSortingRepository<Article, ObjectId> {
}
