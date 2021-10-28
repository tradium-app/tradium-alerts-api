package com.tradiumapp.swingtradealerts.jobscheduler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Properties;

@Configuration
public class QuartzConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    /**
     * Here we integrate quartz with Spring and let Spring manage initializing
     * quartz as a spring bean.
     *
     * @return an instance of {@link SchedulerFactoryBean} which will be managed
     *         by spring.
     */
    @Bean
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean scheduler = new SchedulerFactoryBean();
        scheduler.setApplicationContextSchedulerContextKey("applicationContext");

        Properties properties = new Properties();
        properties.setProperty("org.quartz.jobStore.class", "com.novemberain.quartz.mongodb.MongoDBJobStore");
        properties.setProperty("org.quartz.jobStore.mongoUri", mongoUri);
        properties.setProperty("org.quartz.jobStore.collectionPrefix", "quartz_");
        properties.setProperty("org.quartz.threadPool.threadCount", "1");
        properties.setProperty("org.quartz.jobStore.isClustered", "true");
        properties.setProperty("org.quartz.scheduler.instanceId", "AUTO");
        properties.setProperty("org.quartz.scheduler.instanceName", "quartzMongoInstance");

        scheduler.setQuartzProperties(properties);
        scheduler.setWaitForJobsToCompleteOnShutdown(true);
        return scheduler;
    }

}