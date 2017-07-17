package com.everhomes.pmtask;

import com.everhomes.configuration.ConfigurationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by ying.xiong on 2017/7/17.
 */
@Component(ZJGKHandoverTaskHandler.HANDOVER_VENDOR_PREFIX + HandoverTaskHandler.ZJGK)
public class ZJGKHandoverTaskHandler implements HandoverTaskHandler {
    @Autowired
    private ConfigurationProvider configProvider;

    String url = configProvider.getValue("pmtask.zjgk.url", "");
    String HANDOVER_TASK = "";

    @Override
    public void handoverTaskToTrd(PmTask task) {

    }
}
