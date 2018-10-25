package com.everhomes.core.sdk.print;

import com.everhomes.core.sdk.NsDispatcher;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.print.MfpLogNotificationCommand;
import org.springframework.stereotype.Service;

@Service
public class SdkPrintService extends NsDispatcher {

    public RestResponse mfpLogNotification(Integer namespaceId, String jobData) {
        MfpLogNotificationCommand cmd = new MfpLogNotificationCommand();
        cmd.setJobData(jobData);
        return dispatcher(namespaceId, sdkClient -> {
            return sdkClient.restCall("post", "/evh/siyinprint/mfpLogNotification", cmd, RestResponse.class);
        });
    }
}