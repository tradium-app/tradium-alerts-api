package com.tradiumapp.swingtradealerts.jobscheduler;

import com.tradiumapp.swingtradealerts.scheduledtasks.CalculateMetricsTask;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class JobScheduler {
    private static final Logger logger = LoggerFactory.getLogger(JobScheduler.class);
    private static final SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private SchedulerService schedulerService;

    @PostConstruct
    public void scheuduleAllJobs() throws SchedulerException {
        String day = dayFormat.format(new Date());

        schedulerService.scheduleJob(CalculateMetricsTask.class, "CalculateMetricsTask", "0 * * ? * *");
    }
}
