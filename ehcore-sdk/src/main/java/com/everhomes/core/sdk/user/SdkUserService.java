package com.everhomes.core.sdk.user;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.user.ListBorderAndContentResponse;
import com.everhomes.rest.user.SetUserCurrentCommunityCommand;
import com.everhomes.rest.user.SetUserDefaultCommunityCommand;
import com.everhomes.rest.user.sdk.LogonInfoCommand;
import com.everhomes.rest.user.sdk.UserLogonInfo;
import com.everhomes.core.sdk.SdkRestClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SdkUserService {

    private final SdkRestClient sdkRestClient = SdkRestClient.getInstance();

    public UserLogonInfo logonInfo() {
        LogonInfoCommand cmd = new LogonInfoCommand();
        RestResponse response = sdkRestClient.restCall("post", "/evh/user/logonInfo", cmd, RestResponse.class);
        return new UserLogonInfo();
    }

    public void setDefaultCommunity(Long userId, Integer namecpaceId) {
        SetUserDefaultCommunityCommand cmd = new SetUserDefaultCommunityCommand();
        cmd.setUserId(userId);
        cmd.setNamespaceId(namecpaceId);
        RestResponse response = sdkRestClient.restCall("post", "/evh/user/setUserDefaultCommunity", cmd, RestResponse.class);
    }

    public ListBorderAndContentResponse listAllBorderAccessPoints() {
        RestResponse response = sdkRestClient.restCall("post", "/evh/user/listBorderAndContent", null, RestResponse.class);
        ListBorderAndContentResponse res = (ListBorderAndContentResponse)response.getResponseObject();
        return res;
    }

    public void setDefaultCommunityForWx(Long userId, Integer namecpaceId) {
        SetUserDefaultCommunityCommand cmd = new SetUserDefaultCommunityCommand();
        cmd.setUserId(userId);
        cmd.setNamespaceId(namecpaceId);
        RestResponse response = sdkRestClient.restCall("post", "/evh/user/setUserDefaultCommunityForWx", cmd, RestResponse.class);
    }

    public void updateUserCurrentCommunityToProfile(Long userId, Long communityId, Integer namecpaceId) {
        SetUserCurrentCommunityCommand cmd = new SetUserCurrentCommunityCommand();
        cmd.setUserId(userId);
        cmd.setNamespaceId(namecpaceId);
        cmd.setCommunityId(communityId);
        RestResponse response = sdkRestClient.restCall("post", "/evh/user/updateUserCurrentCommunityToProfile", cmd, RestResponse.class);
    }
}