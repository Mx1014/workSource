package com.everhomes.flow.action;

import com.everhomes.flow.FlowTimeout;
import com.everhomes.flow.FlowTimeoutProvider;
import com.everhomes.flow.FlowTimeoutService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * 使用{@link FlowTimeoutJob}代替
 */
@Deprecated
@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FlowTimeoutAction implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlowTimeoutAction.class);

    @Autowired
    FlowTimeoutProvider flowTimeoutProvider;

    @Autowired
    FlowTimeoutService flowTimeoutService;

    private Long timeoutId;

    @Override
    public void run() {
        if (flowTimeoutProvider.deleteIfValid(timeoutId)) {
            FlowTimeout ft = flowTimeoutProvider.getFlowTimeoutById(timeoutId);

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("FlowTimeoutAction.run success ft = {}", ft);
            }

            //delete ok, means we take it's owner
            flowTimeoutService.processTimeout(ft, ctx);
        } else {
            LOGGER.warn("FlowTimeoutAction.run failure timeoutId = {}", timeoutId);
        }
    }

    public FlowTimeoutAction(final String timeoutId) {
        this.timeoutId = Long.parseLong(timeoutId);
    }
}
