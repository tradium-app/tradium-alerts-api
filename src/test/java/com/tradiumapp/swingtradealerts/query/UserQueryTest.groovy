package com.tradiumapp.swingtradealerts.query

import com.tradiumapp.swingtradealerts.models.Stock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserQueryTest extends AbstractTestNGSpringContextTests {

    @Autowired
    private UserQuery userQuery

    @Test(groups = "integration")
    public void testSearchStocks() {
        String searchTerm = "TSLA"
        List<Stock> stocks = userQuery.searchStocks(searchTerm)

        assertTrue(stocks.size() > 1)
    }
}