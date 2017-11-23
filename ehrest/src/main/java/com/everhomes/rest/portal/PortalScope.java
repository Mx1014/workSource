package com.everhomes.rest.portal;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 * <li>scopeType: 范围类型</li>
 * <li>scopeIds: 范围id集合</li>
 * </ul>
 */
public class PortalScope {
    private String scopeType;

    @ItemType(Long.class)
    private List<Long> scopeIds;

    public String getScopeType() {
        return scopeType;
    }

    public void setScopeType(String scopeType) {
        this.scopeType = scopeType;
    }

    public List<Long> getScopeIds() {
        return scopeIds;
    }

    public void setScopeIds(List<Long> scopeIds) {
        this.scopeIds = scopeIds;
    }
}
