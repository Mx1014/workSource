//@formatter:off
package com.everhomes.rest.asset;

import java.util.List;

/**
 * @author created by yangcx
 * @date 2018年6月13日----下午3:36:24
 */

/**
 *<ul>
 * <li>namespaceId: 域空间id</li>
 * <li>username: 个人客户名称</li>
 * <li>addressIds : 账单关联的楼栋门牌列表</li>
 *</ul>
 */
public class IsUserExistInAddressCmd {
    private Integer namespaceId;
    private String username;
    private List<Long> addressIds;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Long> getAddressIds() {
		return addressIds;
	}

	public void setAddressIds(List<Long> addressIds) {
		this.addressIds = addressIds;
	}
}
