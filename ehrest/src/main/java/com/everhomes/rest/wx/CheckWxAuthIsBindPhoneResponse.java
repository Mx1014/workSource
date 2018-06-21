// @formatter:off
package com.everhomes.rest.wx;

import com.everhomes.rest.RestResponse;
import com.everhomes.rest.user.LogonCommandResponse;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>bindType: 微信是否绑定手机号，请参考{@link com.everhomes.rest.wx.WxAuthBindPhoneType}</li>
 *     <li>LogonCommandResponse: 登录参数，请参考{@link com.everhomes.rest.user.LogonCommandResponse}</li>
 *     <li>identifierToken: 手机号</li>
 *     <li>regionCode: 区号</li>
 * </ul>
 */
public class CheckWxAuthIsBindPhoneResponse extends RestResponse{

    private Byte bindType;

    private String identifierToken;

    private String regionCode;

    private LogonCommandResponse logonCommandResponse;

    public CheckWxAuthIsBindPhoneResponse(){}

    public LogonCommandResponse getLogonCommandResponse() {
        return logonCommandResponse;
    }

    public void setLogonCommandResponse(LogonCommandResponse logonCommandResponse) {
        this.logonCommandResponse = logonCommandResponse;
    }

    public String getIdentifierToken() {
        return identifierToken;
    }

    public void setIdentifierToken(String identifierToken) {
        this.identifierToken = identifierToken;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
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
