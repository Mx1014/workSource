package com.everhomes.rest.business;



import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>userId: 当前用户id</li>
 * <li>namespaceId: 命名空间</li>
 * <li>scopeType: 可见范围类型 参考{@link com.everhomes.rest.common.ScopeType}</li>
 * </ul>
 */

public class SyncBusinessCommand extends BusinessCommand{
    private Long    userId;
    private Integer namespaceId;
    private Byte scopeType;
   
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public Integer getNamespaceId() {
		return namespaceId;
	}

	public void setNamespaceId(Integer namespaceId) {
		this.namespaceId = namespaceId;
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
