// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * <ul> 
 * <li>authId: 授权id列表表</li>
 * <li>macAddress: 门禁设备MAC地址</li>
 * </ul>
 *
 */
public class GetZLAesUserKeyCommand {
	@NotNull
	private List<Long> authId;
	@NotNull
	private String macAddress;
    public List<Long> getAuthId() {
		return authId;
	}

	public void setAuthId(List<Long> authId) {
		this.authId = authId;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
