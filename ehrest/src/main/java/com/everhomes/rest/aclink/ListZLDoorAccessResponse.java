// @formatter:off
package com.everhomes.rest.aclink;

import java.util.List;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>listMac:门禁mac地址列表</li>
 * </ul>
 * @author liuyilin
 *
 */
public class ListZLDoorAccessResponse {
	private List<String> listMac;//待对接方新版上线时,改为macAddress

	public List<String> getListMac() {
		return listMac;
	}

	public void setListMac(List<String> listMac) {
		this.listMac = listMac;
	}
	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
