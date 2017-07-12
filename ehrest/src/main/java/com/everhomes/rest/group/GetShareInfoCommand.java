// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数：
 * <li>namespaceId: 域空间id</li>
 * <li>groupId: 圈id</li>
 * <li>realm: 版本realm</li>
 * </ul>
 */
public class GetShareInfoCommand {
	private Integer namespaceId;
	private Long groupId;
	private String realm;
	
	public Integer getNamespaceId() {
		return namespaceId;
	}
	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
	}
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	public String getRealm() {
		return realm;
	}
	public void setRealm(String realm) {
		this.realm = realm;
	}
	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}
}
