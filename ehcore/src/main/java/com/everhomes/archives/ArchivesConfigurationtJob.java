package com.everhomes.archives;

import com.everhomes.flow.FlowServiceImpl;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class ArchivesConfigurationtJob extends QuartzJobBean {

    @Autowired
    ArchivesService archivesService;

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowServiceImpl.class);

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            archivesService.executeArchivesConfiguration();
            LOGGER.info("ArchivesConfigurationtJob success!");
        } catch (Exception e) {
            LOGGER.error("ArchivesConfigurationtJob failed!", e);
        }
    }

}
