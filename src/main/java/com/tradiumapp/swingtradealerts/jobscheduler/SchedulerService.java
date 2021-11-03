package com.tradiumapp.swingtradealerts.jobscheduler;

import org.quartz.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import static org.quartz.CronScheduleBuilder.cronSchedule;

@Service
public class SchedulerService implements InitializingBean {
    private final SchedulerFactoryBean schedulerFactory;

    @Autowired
    public SchedulerService(SchedulerFactoryBean schedulerFactory) {
        this.schedulerFactory = schedulerFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    public void scheduleJob(Class<? extends Job> jobClass, String jobName, String cronExpression)
            throws SchedulerException {

        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobName, jobName)
                .storeDurably()
                .requestRecovery()
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .startNow()
                .withSchedule(cronSchedule(cronExpression)
                .withMisfireHandlingInstructionFireAndProceed())
                .build();

        Scheduler scheduler = schedulerFactory.getScheduler();
        JobKey jobKey = JobKey.jobKey(jobName);

        if (!scheduler.checkExists(jobKey)) {
            scheduler.scheduleJob(jobDetail, trigger);
        }
    }
}
