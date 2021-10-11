package com.tradiumapp.swingtradealerts.scheduledtasks.helpers

import com.tradiumapp.swingtradealerts.models.Alert
import com.tradiumapp.swingtradealerts.models.Condition
import com.tradiumapp.swingtradealerts.models.IndicatorType
import com.tradiumapp.swingtradealerts.models.User
import org.mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.Assert
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test

import static org.mockito.ArgumentMatchers.any
import static org.mockito.Mockito.verify

@DataMongoTest
class AlertEmailSender_SimpleBuy_Test extends AbstractTestNGSpringContextTests {
    @Mock
    SendGridEmailSender sendGridEmailSender;

    @Autowired
    @InjectMocks
    private AlertEmailSender alertEmailSender;

    @Captor
    ArgumentCaptor<String> subjectCaptor;

    @BeforeClass
    public void setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test(groups = "unit")
    public void testSendBuyAlertEmail() {
        User user = new User()
        List<Alert> alerts = Arrays.asList(createATestAlert())

        alertEmailSender.sendEmail(user, alerts)

        verify(sendGridEmailSender).sendEmail(any(), subjectCaptor.capture(), any())
        Assert.assertEquals(subjectCaptor.getValue().trim(), "Buy TEST1..")
    }

    private Alert createATestAlert() {
        Alert testAlert = new Alert()
        testAlert.title = "test alert"
        testAlert.symbol = "TEST1"
        testAlert.signal = Alert.AlertSignal.Buy
        def config = new Condition.ValueConfig()
        config.value = 20;
        def condition = new Condition(IndicatorType.week52high, "20_below_week52high", config)
        testAlert.conditions = Arrays.asList(condition)

        return testAlert
    }
}
