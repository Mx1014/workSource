package com.everhomes.rest.portal;

/**
 * <ul>
 * <li>scopeId: 范围id</li>
 * <li>scopeName: 范围名称</li>
 * </ul>
 */
public class PortalScopeDTO {
    private Long scopeId;
    private String scopeName;

    public Long getScopeId() {
        return scopeId;
    }

    public void setScopeId(Long scopeId) {
        this.scopeId = scopeId;
    }

    public String getScopeName() {
        return scopeName;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }
}
