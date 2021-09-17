package com.tradiumapp.swingtradealerts.mutations;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;
import com.tradiumapp.swingtradealerts.models.Response;
import com.tradiumapp.swingtradealerts.scheduledtasks.FetchAllStocksTask;
import com.tradiumapp.swingtradealerts.scheduledtasks.FetchQuotesTask;
import com.tradiumapp.swingtradealerts.scheduledtasks.SendAlertTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JobRunnerMutation implements GraphQLMutationResolver {
    Logger logger = LoggerFactory.getLogger(AlertMutation.class);

    @Autowired
    private FetchAllStocksTask fetchAllStocksTask;

    @Autowired
    private FetchQuotesTask fetchQuotesTask;

    @Autowired
    private SendAlertTask sendAlertTask;

    public Response runJob(final float jobId) throws IOException {
        if(jobId == 1001) fetchAllStocksTask.fetchAllStocks();
        else if(jobId == 1002) fetchQuotesTask.fetchQuotes();
        else if(jobId == 1003) sendAlertTask.sendAlerts();

        logger.info("Job {} ran successfully.", jobId);

        return new Response(true, String.format("Job %s ran successfully.",jobId));
    }
}
