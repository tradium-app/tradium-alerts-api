package com.tradiumapp.swingtradealerts.scheduledtasks.utilities;

import com.tradiumapp.swingtradealerts.models.Stock;
import com.tradiumapp.swingtradealerts.models.User;
import com.tradiumapp.swingtradealerts.repositories.StockRepository;
import com.tradiumapp.swingtradealerts.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class StockUtility {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockRepository stockRepository;

    public List<Stock> fetchStocksInWatchList() {
        List<User> users = (List<User>) userRepository.findAll();
        Set<String> symbols = new HashSet<>();

        for (User user : users) {
            if (user.watchList != null) symbols.addAll(user.watchList);
        }

        return stockRepository.findBySymbolIn(new ArrayList<>(symbols));
    }
}
