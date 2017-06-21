package com.everhomes.flow.action;

import com.everhomes.flow.FlowTimeout;
import com.everhomes.flow.FlowTimeoutProvider;
import com.everhomes.flow.FlowTimeoutService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * Created by xq.tian on 2017/5/31.
 */
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FlowTimeoutJob extends QuartzJobBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowTimeoutJob.class);

    @Autowired
    private
    FlowTimeoutProvider flowTimeoutProvider;

    @Autowired
    private
    FlowTimeoutService flowTimeoutService;

    // private static class FlowVersionVO {Integer flowVersion;}// vo class

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        try {
            JobDataMap jobMap = context.getJobDetail().getJobDataMap();

            Long ftId = (Long)jobMap.get("flowTimeoutId");
            // FlowCaseState ctx = (FlowCaseState)jobMap.get("ctx");

            if (flowTimeoutProvider.deleteIfValid(ftId)) {
                FlowTimeout ft = flowTimeoutProvider.getFlowTimeoutById(ftId);

                // FlowVersionVO flowVersionVO = (FlowVersionVO) StringHelper.fromJsonString(ft.getJson(), FlowVersionVO.class);
                // Integer ftFlowVersion = flowVersionVO.flowVersion;
                // Integer currFlowVersion = ctx.getFlowCase().getFlowVersion();

                // 当前的flowVersion和ft的flowVersion一致时才执行
                // if (currFlowVersion.equals(ftFlowVersion)) {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("FlowTimeoutAction.run success ft = {}", ft);
                    }

                    //delete ok, means we take it's owner
                    flowTimeoutService.processTimeout(ft);
                // }
            } else {
                LOGGER.warn("FlowTimeoutAction.run failure timeoutId = {}", ftId);
            }
        } catch (Exception e) {
            LOGGER.error("FlowTimeoutJob error", e);
        }
    }
}
