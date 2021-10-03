package com.tradiumapp.swingtradealerts.services.models;

import java.util.List;

public class ApeWisdomResponse {
    public List<Trending> results;

    public class Trending {
        public float rank;
        public String ticker;
        public String name;
        public float mentions;
        public float rank_24h_ago;
    }
}
