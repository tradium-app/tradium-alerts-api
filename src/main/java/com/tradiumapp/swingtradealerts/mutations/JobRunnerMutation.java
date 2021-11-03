package com.tradiumapp.swingtradealerts.mutations;

import com.tradiumapp.swingtradealerts.models.Response;
import com.tradiumapp.swingtradealerts.scheduledtasks.*;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.quartz.JobExecutionException;
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
    private FetchDailyQuotesTask fetchDailyQuotesTask;

    @Autowired
    private SendAlertTask sendAlertTask;

    @Autowired
    private CalculateMetricsTask calculateMetricsTask;

    @Autowired
    private FetchStockMetricsTask fetchStockMetricsTask;

    @Autowired
    private FetchArticlesTask fetchArticlesTask;

    @Autowired
    private TipranksMetricsTask tipranksMetricsTask;

    @Autowired
    private FetchEarningsTask fetchEarningsTask;

    @Autowired
    private FetchLiveQuotesTask fetchLiveQuotesTask;

    @Autowired
    private UpdateYesterdaysPriceTask updateYesterdaysPriceTask;

    public Response runJob(final float jobId) throws IOException, JobExecutionException {
        if (jobId == 1001) fetchAllStocksTask.fetchAllStocks();
        else if (jobId == 1002) fetchDailyQuotesTask.fetchQuotes();
        else if (jobId == 1003) sendAlertTask.sendAlerts();
        else if (jobId == 1004) calculateMetricsTask.execute(null);
        else if (jobId == 1005) fetchArticlesTask.fetchArticles();
        else if (jobId == 1006) tipranksMetricsTask.getData();
        else if (jobId == 1007) fetchEarningsTask.fetchUpcomingEarnings();
        else if (jobId == 1008) fetchStockMetricsTask.fetchStockMetrics();
        else if (jobId == 1009) fetchLiveQuotesTask.fetchLiveQuotesTask();
        else if (jobId == 1010) updateYesterdaysPriceTask.execute(null);

        logger.info("Job {} ran successfully.", jobId);

        return new Response(true, String.format("Job %s ran successfully.", jobId));
    }
}
