package com.tradiumapp.swingtradealerts.services.implementation;

import com.tradiumapp.swingtradealerts.models.Article;
import com.tradiumapp.swingtradealerts.services.ArticleService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ArticleServiceImpl implements ArticleService {
    @Override
    public List<Article> findAllUserArticles(
            List<String> articleIds) {
        return null;  //TODO: Implement
    }
}
