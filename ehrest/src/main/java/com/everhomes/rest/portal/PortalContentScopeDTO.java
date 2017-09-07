package com.everhomes.rest.portal;

import com.everhomes.discover.ItemType;

import java.util.List;

/**
 * <ul>
 * <li>scopeType: 范围类型</li>
 * <li>scopes: 具体范围数据，参考{@link com.everhomes.rest.portal.PortalScopeDTO}</li>
 * </ul>
 */
public class PortalContentScopeDTO {
    private String scopeType;

    @ItemType(PortalScopeDTO.class)
    private List<PortalScopeDTO> scopes;

    public String getScopeType() {
        return scopeType;
    }

    public void setScopeType(String scopeType) {
        this.scopeType = scopeType;
    }

    public List<PortalScopeDTO> getScopes() {
        return scopes;
    }

    public void setScopes(List<PortalScopeDTO> scopes) {
        this.scopes = scopes;
    }
}
