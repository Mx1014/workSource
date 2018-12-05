// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>macAddress:门禁mac地址列表</li>
 * </ul>
 * @author liuyilin
 *
 */
public class ListZLDoorAccessResponse {
	private List<String> macAddress;

	public List<String> getMacAddress() {
		return macAddress;
	}
	public void setMacAddress(List<String> macAddress) {
		this.macAddress = macAddress;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
