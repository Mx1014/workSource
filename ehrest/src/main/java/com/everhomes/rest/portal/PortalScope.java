package com.everhomes.rest.portal;

/**
 * <ul>
 * <li>scopeType: 范围类型</li>
 * <li>scopeIds: 范围id集合</li>
 * </ul>
 */
public class PortalScope {
    private String scopeType;
    private Long scopeIds;

    public String getScopeType() {
        return scopeType;
    }

    public void setScopeType(String scopeType) {
        this.scopeType = scopeType;
    }

    public Long getScopeIds() {
        return scopeIds;
    }

    public void setScopeIds(Long scopeIds) {
        this.scopeIds = scopeIds;
    }
}
