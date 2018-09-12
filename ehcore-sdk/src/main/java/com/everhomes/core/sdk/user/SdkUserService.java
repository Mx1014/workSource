package com.everhomes.core.sdk.user;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.user.sdk.LogonInfoCommand;
import com.everhomes.rest.user.sdk.UserLogonInfo;
import com.everhomes.core.sdk.SdkRestClient;
import org.springframework.stereotype.Service;

@Service
public class SdkUserService {

    private final SdkRestClient sdkRestClient = SdkRestClient.getInstance();

    public UserLogonInfo logonInfo() {
        LogonInfoCommand cmd = new LogonInfoCommand();
        RestResponse response = sdkRestClient.restCall("post", "/evh/user/logonInfo", cmd, RestResponse.class);
        return new UserLogonInfo();
    }
}