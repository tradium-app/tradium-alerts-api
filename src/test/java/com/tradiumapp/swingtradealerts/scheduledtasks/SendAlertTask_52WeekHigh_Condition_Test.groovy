package com.tradiumapp.swingtradealerts.scheduledtasks

import com.tradiumapp.swingtradealerts.models.*
import com.tradiumapp.swingtradealerts.repositories.AlertRepository
import com.tradiumapp.swingtradealerts.repositories.StockHistoryRepository
import com.tradiumapp.swingtradealerts.repositories.StockRepository
import com.tradiumapp.swingtradealerts.repositories.UserRepository
import com.tradiumapp.swingtradealerts.scheduledtasks.helpers.AlertEmailSender
import org.mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.Assert
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

import java.time.Instant

import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.verify

@DataMongoTest
class SendAlertTask_52WeekHigh_Condition_Test extends AbstractTestNGSpringContextTests {
    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private StockHistoryRepository stockHistoryRepository;

    @Mock
    AlertEmailSender alertEmailSender;

    @Autowired
    @InjectMocks
    private SendAlertTask task

    User testUser = new User();

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this)

        testUser.email = "test@test.com"
        userRepository.save(testUser);

        Alert testAlert = new Alert()
        testAlert.title = "test alert"
        testAlert.symbol = "TEST"
        testAlert.userId = testUser.id
        testAlert.status = Alert.AlertStatus.Off
        def config = new Condition.ValueConfig()
        config.value = 20;
        def condition = new Condition(IndicatorType.week52high, "20_below_week52high", config)
        testAlert.conditions = Arrays.asList(condition)
        alertRepository.save(testAlert);

        StockHistory stockHistory = new StockHistory()
        stockHistory.symbol = testAlert.symbol;
        stockHistory.daily_priceHistory = new ArrayList<>()
        def price = new StockHistory.StockPrice()
        price.close = 75;
        price.time = Instant.now().toEpochMilli()
        stockHistory.daily_priceHistory.add(price)
        stockHistoryRepository.save(stockHistory)

        Stock stock = new Stock()
        stock.symbol = testAlert.symbol;
        stock.week52High = 100;
        stockRepository.save(stock)
    }

    @Captor
    ArgumentCaptor<User> userCaptor;

    @Test(groups = "unit")
    void testSendAlerts() {
        task.sendAlerts();
        verify(alertEmailSender).sendEmail(userCaptor.capture(), any())
        Assert.assertEquals(userCaptor.getValue().email, testUser.email)

        Alert alert = alertRepository.findAll().asList().first()
        Assert.assertEquals(alert.status, Alert.AlertStatus.On)
    }
}
