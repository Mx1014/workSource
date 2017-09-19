package com.everhomes.archives;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class ArchivesEventJob extends QuartzJobBean {

    @Autowired
    ArchivesService archivesService;

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        archivesService.executeArchivesConfiguration();
    }


}
