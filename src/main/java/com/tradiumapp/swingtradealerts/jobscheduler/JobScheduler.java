package com.tradiumapp.swingtradealerts.jobscheduler;

import com.tradiumapp.swingtradealerts.scheduledtasks.CalculateMetricsTask;
import com.tradiumapp.swingtradealerts.scheduledtasks.UpdateYesterdaysPriceTask;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class JobScheduler {

    @Autowired
    private SchedulerService schedulerService;

    @PostConstruct
    public void scheuduleAllJobs() throws SchedulerException {
        schedulerService.scheduleJob(CalculateMetricsTask.class, "CalculateMetricsTask", "0 0 */4 ? * *");
        schedulerService.scheduleJob(UpdateYesterdaysPriceTask.class, "UpdateYesterdaysPriceTask", "0 1 0 ? * *");
    }
}
