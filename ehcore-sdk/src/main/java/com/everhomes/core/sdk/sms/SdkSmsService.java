package com.everhomes.core.sdk.sms;

import com.everhomes.core.sdk.NsDispatcher;
import com.everhomes.rest.RestResponseBase;
import com.everhomes.rest.user.PointCheckVCDTO;
import com.everhomes.rest.user.PointCheckVerificationCodeCommand;
import com.everhomes.rest.user.PointCheckVerificationCodeRestResponse;
import com.everhomes.rest.user.SendVerificationCodeByPhoneCommand;
import org.springframework.stereotype.Service;

@Service
public class SdkSmsService extends NsDispatcher {

    public String sendShortMessage(String phone, Integer namespaceId, Integer regionCode) {
        SendVerificationCodeByPhoneCommand cmd = new SendVerificationCodeByPhoneCommand();
        cmd.setPhone(phone);
        cmd.setNamespaceId(namespaceId);
        cmd.setRegionCode(regionCode);

        RestResponseBase responseBase = dispatcher(namespaceId, sdkClient -> {
            return sdkClient.restCall("post", "/evh/user/sendVerificationCodeByPhone", cmd, RestResponseBase.class);
        });
        return responseBase.getErrorCode().toString();
    }

    public PointCheckVCDTO checkVerificationCode(Integer namespaceId, PointCheckVerificationCodeCommand cmd) {
        PointCheckVerificationCodeRestResponse resp = dispatcher(namespaceId, sdkClient -> {
            return sdkClient.restCall("post", "/evh/user/pointCheckVerificationCode", cmd, PointCheckVerificationCodeRestResponse.class);
        });
        return resp.getResponse();
    }
}