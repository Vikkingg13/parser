package ru.job4j.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;

import java.util.Calendar;

/**
 * Job.
 * @author Viktor
 * @version 1.0
 */
public class ParserJob implements Job {
    private static final Logger LOG = LogManager.getLogger(ParserJob.class.getName());

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        LOG.info("Execute job");
        Config config = (Config) jobExecutionContext.getJobDetail().getJobDataMap().get("config");
        LOG.info("Start date: {}", config.get("start-date"));
        ParserHTML parser = new ParserHTML(
                config.get("address"),
                config.get("regular"),
                config.get("start-date")
        );
        StoreSQL sql = new StoreSQL();
        sql.init(config);
        sql.save(parser.parse());
        String date = DateUtil.format(Calendar.getInstance().getTime());
        config.set("start-date", date);
        LOG.info("Start date: {}", config.get("start-date"));
    }
}
