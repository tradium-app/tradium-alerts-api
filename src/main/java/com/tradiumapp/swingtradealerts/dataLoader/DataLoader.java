package com.tradiumapp.swingtradealerts.dataLoader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.tradiumapp.swingtradealerts.models.User;
import com.tradiumapp.swingtradealerts.repositories.ArticleRepository;
import com.tradiumapp.swingtradealerts.repositories.UserRepository;

@Component
public class DataLoader {
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;

    @Autowired
    public DataLoader(UserRepository userRepository, ArticleRepository articleRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
    }

    @PostConstruct
    private void generateData() {
        List<User> users = new ArrayList<>();

        User user1 = new User();
        user1.name = "jj";
        user1.createdAt = new Date();
        user1.age = 22;

        User user2 = new User();
        user2.name = "jj";
        user2.createdAt = new Date();
        user2.age = 22;

        users.add(user1);
        users.add(user2);

        userRepository.saveAll(users);
    }
}
