// @formatter:off
package com.everhomes.rest.wx;

import com.everhomes.discover.ItemType;
import com.everhomes.rest.RestResponse;
import com.everhomes.rest.user.LogonCommandResponse;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>bindType: 微信是否绑定手机号，请参考{@link com.everhomes.rest.wx.WxAuthBindPhoneType}</li>
 *     <li>LogonCommandResponse: 登录参数，请参考{@link com.everhomes.rest.user.LogonCommandResponse}</li>
 *     <li>identifierToken: 手机号</li>
 *     <li>regionCode: 区号</li>
 * </ul>
 */
public class CheckWxAuthIsBindPhoneResponse{

    private Byte bindType;

    private String identifierToken;

    private Integer regionCode;

    private long uid;
    private String loginToken;
    private String contentServer;

    @ItemType(String.class)
    private List<String> accessPoints;

    public CheckWxAuthIsBindPhoneResponse(){}

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getLoginToken() {
        return loginToken;
    }

    public void setLoginToken(String loginToken) {
        this.loginToken = loginToken;
    }

    public String getContentServer() {
        return contentServer;
    }

    public void setContentServer(String contentServer) {
        this.contentServer = contentServer;
    }

    public List<String> getAccessPoints() {
        return accessPoints;
    }

    public void setAccessPoints(List<String> accessPoints) {
        this.accessPoints = accessPoints;
    }

    public String getIdentifierToken() {
        return identifierToken;
    }

    public void setIdentifierToken(String identifierToken) {
        this.identifierToken = identifierToken;
    }

    public Integer getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(Integer regionCode) {
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
