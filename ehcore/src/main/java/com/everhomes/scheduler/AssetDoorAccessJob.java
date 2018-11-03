package com.everhomes.scheduler;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import com.everhomes.asset.AssetService;

/**
 * Created by djm  2018/10/17
 */
@Component
public class AssetDoorAccessJob extends QuartzJobBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(AssetDoorAccessJob.class);

    @Autowired
	private AssetService assetService;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("start AssetDoorAccess auto Reading task......");
        assetService.meterAutoReading(true);
        LOGGER.info("finish  AssetDoorAccess auto Reading task......");

    }
}
