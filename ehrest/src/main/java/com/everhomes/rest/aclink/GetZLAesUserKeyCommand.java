// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * <li>authId: 授权id列表表</li>
 * <li>macAddress: 门禁设备MAC地址</li>
 * <li>兼容旧版本:</li>
 * <li>userIds: 用户id列表</li>
 * <li>MacAddress: 门禁设备MAC地址</li>
 * </ul>
 *
 */
public class GetZLAesUserKeyCommand {
	private List<Long> authId;
	private String macAddress;
	private List<Long> userIds;
	private String MacAddress;
	
    public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	public List<Long> getAuthId() {
		return authId == null || authId.size() == 0 ? userIds : authId;
	}

	public void setAuthId(List<Long> authId) {
		this.authId = authId;
	}

	public String getMacAddress() {
		return macAddress == null || macAddress.isEmpty() ? MacAddress : macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
