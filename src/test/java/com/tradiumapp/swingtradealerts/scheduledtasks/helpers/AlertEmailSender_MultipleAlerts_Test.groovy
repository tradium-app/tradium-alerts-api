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
class AlertEmailSender_MultipleAlerts_Test extends AbstractTestNGSpringContextTests {
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
    public void testSendBuySellAlertEmail() {
        User user = new User()
        List<Alert> alerts = new ArrayList<>(Arrays.asList(createATestAlert()))
        Alert sellAlert = createATestAlert()
        sellAlert.signal = Alert.AlertSignal.Sell
        sellAlert.symbol = "TEST2"
        alerts.add(sellAlert)

        alertEmailSender.sendEmail(user, alerts)

        verify(sendGridEmailSender).sendEmail(any(), subjectCaptor.capture(), any())
        Assert.assertEquals(subjectCaptor.getValue(), "Buy TEST1.. Sell TEST2..")
    }

    private Alert createATestAlert() {
        Alert testAlert = new Alert()
        testAlert.title = "test alert"
        testAlert.symbol = "TEST1"
        testAlert.signal = Alert.AlertSignal.Buy
        def config = new Condition.Config()
        config.value = 20;
        def condition = new Condition(IndicatorType.week52High, "20_below_week52high", config)
        testAlert.conditions = Arrays.asList(condition)

        return testAlert
    }
}
