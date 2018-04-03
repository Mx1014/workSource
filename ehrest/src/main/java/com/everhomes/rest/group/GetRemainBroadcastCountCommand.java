// @formatter:off
package com.everhomes.rest.group;

import com.everhomes.util.StringHelper;

/**
 * 
 * <ul>参数：
 * <li>namespaceId: 域空间名称</li>
 * <li>groupId: 圈id</li>
 * </ul>
 */
public class GetRemainBroadcastCountCommand {
	private Integer namespaceId;
	private Long groupId;

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
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
