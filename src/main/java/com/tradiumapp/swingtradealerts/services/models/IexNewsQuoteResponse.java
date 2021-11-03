package com.tradiumapp.swingtradealerts.services.models;

import java.util.List;

public class IexNewsQuoteResponse {
    public Quote quote;
    public List<News> news;

    public class Quote {
        public String companyName;
        public float latestPrice;
        public float peRatio;
        public float week52High;
        public float week52Low;
    }

    public class News {
        public float datetime;
        public String headline;
        public String url;
        public String summary;
        public String image;
    }
}
