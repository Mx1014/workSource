// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * <li>userIds: 用户id列表</li>
 * <li>MacAddress: 门禁设备MAC地址</li>
 * </ul>
 *
 */
public class GetZLAesUserKeyCommand {
	private List<Long> userIds;
	private String MacAddress;
	public List<Long> getUserIds() {
		return userIds;
	}
	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}
	public String getMacAddress() {
		return MacAddress;
	}
	public void setMacAddress(String macAddress) {
		MacAddress = macAddress;
	}
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
