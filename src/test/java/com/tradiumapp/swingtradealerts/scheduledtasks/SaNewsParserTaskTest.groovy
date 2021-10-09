package com.tradiumapp.swingtradealerts.scheduledtasks

import org.mockito.InjectMocks
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SaNewsParserTaskTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private SaNewsParserTask task

    @Test(groups = "integration")
    void fetchSaTopNews() {
        task.fetchSaTopNews()
    }
}
