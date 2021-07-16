package com.tradiumapp.swingtradealerts.scheduledtasks

import com.tradiumapp.swingtradealerts.models.Stock
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import retrofit2.Call
import retrofit2.Response

import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

public class FetchAllStocksTaskTest extends AbstractTestNGSpringContextTests {

    @Mock
    private IexCloudService iexService

    @InjectMocks
    private FetchAllStocksTask task

    @BeforeClass
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
