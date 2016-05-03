package com.everhomes.rest.business;

import java.util.List;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userId: 当前用户id</li>
 * <li>namespaceIds: 命名空间列表</li>
 * <li>scopeType: 可见范围类型 参考{@link com.everhomes.rest.common.ScopeType}</li>
 * </ul>
 */
public class ReSyncBusinessCommand extends BusinessCommand{
	private Long    userId;
	@ItemType(Integer.class)
	private List<Integer> namespaceIds;
	private Byte scopeType;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<Integer> getNamespaceIds() {
		return namespaceIds;
	}

	public void setNamespaceIds(List<Integer> namespaceIds) {
		this.namespaceIds = namespaceIds;
	}

	public Byte getScopeType() {
		return scopeType;
	}

	public void setScopeType(Byte scopeType) {
		this.scopeType = scopeType;
	}

	@Override
	public String toString() {
		return StringHelper.toJsonString(this);
	}

}
