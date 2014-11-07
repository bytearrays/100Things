package com.bytearrays.onecthings.job;

import com.bytearrays.onecthings.bean.MailService;
import com.bytearrays.onecthings.bean.TrelloService;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by dobrescu on 11/3/14.
 */
@Component
public class FetchMailJob extends QuartzJobBean {


    private Logger LOG = Logger.getLogger(FetchMailJob.class);

    @Autowired
    MailService mailService;


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LOG.info("Executing a new fetch Mail job");
//        mailService.searchForPendingMails();
    }
}
