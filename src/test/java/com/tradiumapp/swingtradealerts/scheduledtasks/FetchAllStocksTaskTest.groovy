package com.tradiumapp.swingtradealerts.scheduledtasks

import com.tradiumapp.swingtradealerts.models.Stock
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit4.SpringRunner
import retrofit2.Call
import retrofit2.Response

import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FetchAllStocksTask.class)
public class FetchAllStocksTaskTest {

    @MockBean
    private IexCloudService iexService

    @Autowired
    private FetchAllStocksTask task

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this)

        def stocks = new ArrayList<Stock>()
        stocks.add(new Stock(symbol: "ss"))

        def call = mock(Call.class);
        when(call.execute()).thenReturn(Response.success(stocks))
        when(iexService.listStocks(any())).thenReturn(call)
    }

    @Test
    void testFetchAllStocks() {
        task.fetchAllStocks();
    }
}
