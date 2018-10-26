package com.everhomes.rest.aclink;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul> 添加访客授权 
 * <li>phone: 电话</li> 
 * <li>userName: 访客姓名</li>
 * <li>headImgUri:[String ]头像照片地址</li>
 * <li>macAddresses: 授权的macAddresses</li>
 * <li>authId: 授权的id</li>
 * </ul>
 *
 */
public class OpenAuthDTO {

	@NotNull
    private String phone;

    private String userName;

	private String headImgUri;
	
	private String macAddresses;
	
	private Long authId;
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getHeadImgUri() {
		return headImgUri;
	}
	public void setHeadImgUri(String headImgUri) {
		this.headImgUri = headImgUri;
	}
	public Long getAuthId() {
		return authId;
	}
	public void setAuthId(Long authId) {
		this.authId = authId;
	}
	public String getMacAddresses() {
		return macAddresses;
	}
	public void setMacAddresses(String macAddresses) {
		this.macAddresses = macAddresses;
	}
	
}
