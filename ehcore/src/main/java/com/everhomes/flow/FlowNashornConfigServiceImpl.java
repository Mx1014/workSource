package com.everhomes.flow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FlowNashornConfigServiceImpl implements FlowModuleNashornConfigService {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlowNashornConfigServiceImpl.class);

    @Override
    public String name() {
        return "flowService";
    }

    @Override
    public String ggetConfigByKeyet() {
        LOGGER.debug("this is test api call");
        return "111111111111111111";
    }
}