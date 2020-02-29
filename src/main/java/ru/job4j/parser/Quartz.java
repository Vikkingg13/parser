package ru.job4j.parser;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Run job by CronTrigger
 * @author Viktor
 * @version 1.0
 */

public class Quartz {
    /**
     * Start application
     */
    public void start() {
        try {
            Config config = new Config("app.properties");
            config.init();
            SchedulerFactory factory = new StdSchedulerFactory();
            Scheduler scheduler = factory.getScheduler();
            JobDataMap data = new JobDataMap();
            data.put("config", config);
            JobDetail detail = JobBuilder.newJob(ParserJob.class)
                    .withIdentity("parserJob", "firstGroup")
                    .usingJobData(data)
                    .build();
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity("trigger", "firstGroup")
                    .withSchedule(CronScheduleBuilder.cronSchedule(config.get("cron-time")))
                    .forJob("parserJob", "firstGroup")
                    .build();
            scheduler.scheduleJob(detail, trigger);
            scheduler.start();
        } catch (SchedulerException ex) {
            System.out.println(ex.getMessage());
        }
    }
    public static void main(String[] args) {
        new Quartz().start();
    }
}

