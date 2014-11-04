package com.bytearrays.onecthings.job;

import com.bytearrays.onecthings.bean.TrelloBean;
import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.trello4j.Trello;

/**
 * Created by dobrescu on 11/3/14.
 */
@Component
public class FetchTrelloJob extends QuartzJobBean {


    private Logger LOG = Logger.getLogger(FetchTrelloJob.class);

    @Autowired
    TrelloBean trelloBean;


    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        LOG.info("Executing a new fetch Trello job");
        trelloBean.fetchFirstFresh();
    }
}
