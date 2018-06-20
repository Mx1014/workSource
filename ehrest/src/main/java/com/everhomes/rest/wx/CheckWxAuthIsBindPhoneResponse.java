// @formatter:off
package com.everhomes.rest.wx;

import com.everhomes.rest.user.LogonCommandResponse;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>bindType: 微信是否绑定手机号，请参考{@link com.everhomes.rest.wx.WxAuthBindPhoneType}</li>
 *     <li>LogonCommandResponse: 登录参数，请参考{@link com.everhomes.rest.user.LogonCommandResponse}</li>
 * </ul>
 */
public class CheckWxAuthIsBindPhoneResponse {

    private Byte bindType;

    private LogonCommandResponse logonCommandResponse;

    public CheckWxAuthIsBindPhoneResponse(){}

    public LogonCommandResponse getLogonCommandResponse() {
        return logonCommandResponse;
    }

    public void setLogonCommandResponse(LogonCommandResponse logonCommandResponse) {
        this.logonCommandResponse = logonCommandResponse;
    }

    public Byte getBindType() {
        return bindType;
    }

    public void setBindType(Byte bindType) {
        this.bindType = bindType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
