package com.tradiumapp.swingtradealerts.jobscheduler.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

import java.util.Properties;

@Configuration
public class QuartzConfiguration {

    @Autowired
    ApplicationContext applicationContext;

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
        scheduler.setJobFactory(jobFactory());

        Properties properties = new Properties();
        properties.setProperty("org.quartz.jobStore.class", "com.novemberain.quartz.mongodb.MongoDBJobStore");
        properties.setProperty("org.quartz.jobStore.mongoUri", mongoUri);
        properties.setProperty("org.quartz.jobStore.collectionPrefix", "quartz_");
        properties.setProperty("org.quartz.threadPool.threadCount", "2");
        properties.setProperty("org.quartz.jobStore.isClustered", "true");
        properties.setProperty("org.quartz.scheduler.instanceId", "AUTO");
        properties.setProperty("org.quartz.scheduler.instanceName", "quartzMongoInstance");
        properties.setProperty("org.quartz.jobStore.misfireThreshold", "1800000");

        scheduler.setQuartzProperties(properties);
        scheduler.setWaitForJobsToCompleteOnShutdown(true);
        return scheduler;
    }

    @Bean
    public SpringBeanJobFactory jobFactory() {
        ContextAwareSpringBeanJobFactory jobFactory = new ContextAwareSpringBeanJobFactory();
        jobFactory.setApplicationContext(applicationContext);
        return jobFactory;
    }
}