package com.tradiumapp.swingtradealerts.scheduledtasks

import com.tradiumapp.swingtradealerts.models.Alert
import com.tradiumapp.swingtradealerts.models.Condition
import com.tradiumapp.swingtradealerts.models.IndicatorType
import com.tradiumapp.swingtradealerts.models.Stock
import com.tradiumapp.swingtradealerts.models.StockHistory
import com.tradiumapp.swingtradealerts.repositories.AlertRepository
import com.tradiumapp.swingtradealerts.repositories.StockRepository
import com.tradiumapp.swingtradealerts.repositories.UserRepository
import com.tradiumapp.swingtradealerts.scheduledtasks.helpers.SendGridEmailSender
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

import java.time.Instant

import static org.mockito.ArgumentMatchers.any
import static org.mockito.ArgumentMatchers.eq
import static org.mockito.Mockito.when

class SendAlertTaskTest extends AbstractTestNGSpringContextTests {
    @Mock
    private AlertRepository alertRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StockRepository stockRepository;

    @Mock
    MongoTemplate mongoTemplate;

    @Mock
    SendGridEmailSender emailSender;

    @InjectMocks
    private SendAlertTask task

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this)

        Alert testAlert = new Alert()
        testAlert.title = "test alert"
        testAlert.symbol = "TEST"

        def condition = new Condition(IndicatorType.week52high, "20_below_week52high", new Condition.ValueConfig("11"))
        testAlert.conditions = Arrays.asList(condition)

        List<Alert> alerts = Arrays.asList(testAlert)

        when(alertRepository.findByStatusNot(Alert.AlertStatus.Disabled)).thenReturn(alerts)

        StockHistory stockHistory = new StockHistory()
        stockHistory.symbol = testAlert.symbol;
        stockHistory.daily_priceHistory = new ArrayList<>()

        def price = new StockHistory.StockPrice()
        price.close = 75;
        price.time = Instant.now().toEpochMilli()

        stockHistory.daily_priceHistory.add(price)
        when(mongoTemplate.findOne(any(), eq(StockHistory.class))).thenReturn(stockHistory)

        Stock stock = new Stock()
        stock.week52High = 100;
        when(stockRepository.findBySymbol(testAlert.symbol)).thenReturn(stock)
    }

    @Test(groups = "unit")
    void testSendAlerts() {
        task.sendAlerts();
    }
}
