package com.everhomes.flow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlowNashornConfigService {

    private static FlowScriptConfigProvider flowScriptConfigProvider;

    public static String getConfigByKey(String ownerType, Long ownerId, String configName) {
        return flowScriptConfigProvider.getConfig(ownerType, ownerId, configName);
    }

    @Autowired
    public void setApiServices(FlowScriptConfigProvider flowScriptConfigProvider) {
        FlowNashornConfigService.flowScriptConfigProvider = flowScriptConfigProvider;
    }
}
