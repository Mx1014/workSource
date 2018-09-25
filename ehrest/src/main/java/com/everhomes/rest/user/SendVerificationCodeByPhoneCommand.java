// @formatter:off
package com.everhomes.rest.user;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;
/**
 * 积分发送验证码
 * @author liangming.huang
 * <ul>
 * <li>phone:手机号</li>
 * <li>namespaceId: 域ID/li>
 * <li>regionCode: 区域码</li>
 * </ul>
 */
public class SendVerificationCodeByPhoneCommand {
    @NotNull
    private String phone;
    
    private Integer namespaceId;

    @NotNull
    private Integer regionCode;
    
    public SendVerificationCodeByPhoneCommand() {
    }

    public Integer getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(Integer regionCode) {
        this.regionCode = regionCode;
    }

    

    public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
