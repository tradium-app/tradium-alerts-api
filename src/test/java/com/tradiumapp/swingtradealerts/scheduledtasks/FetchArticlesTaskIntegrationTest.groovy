package com.tradiumapp.swingtradealerts.scheduledtasks

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests
import org.testng.annotations.Test

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FetchArticlesTaskIntegrationTest extends AbstractTestNGSpringContextTests {
    @Autowired
    private FetchArticlesTask task

    @Test(groups = "integration")
    void fetchSaTopNews() {
        task.fetchArticles()
    }
}
