package com.tradiumapp.swingtradealerts.services;


import com.tradiumapp.swingtradealerts.models.Article;

import java.util.List;

public interface ArticleService {

    List<Article> findAllUserArticles(List<String> articleIds);
}
