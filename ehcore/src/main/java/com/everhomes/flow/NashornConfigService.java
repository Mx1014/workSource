package com.everhomes.flow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NashornConfigService {

    @Autowired
    private static FlowScriptConfigProvider flowScriptConfigProvider;

    public static String getConfigByKey(Long flowMainId, Integer flowVersion, String configName) {
        return flowScriptConfigProvider.getConfig(flowMainId, flowVersion, configName);
    }

    @Autowired
    public void setApiServices(FlowScriptConfigProvider flowScriptConfigProvider) {
        NashornConfigService.flowScriptConfigProvider = flowScriptConfigProvider;
    }
}
