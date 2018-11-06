package com.everhomes.rest.servicemoduleapp;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *     <li>scopeType: 范围，1-园区，4-公司</li>
 *     <li>scopeId: scopeId</li>
 *     <li>appIds: 按照顺序排列顺序传来appId</li>
 * </ul>
 */
public class UpdateRecommendAppsCommand {

    private Byte scopeType;

    private Long scopeId;

    private List<Long> appIds;

    public Byte getScopeType() {
        return scopeType;
    }

    public void setScopeType(Byte scopeType) {
        this.scopeType = scopeType;
    }

    public Long getScopeId() {
        return scopeId;
    }

    public void setScopeId(Long scopeId) {
        this.scopeId = scopeId;
    }

    public List<Long> getAppIds() {
        return appIds;
    }

    public void setAppIds(List<Long> appIds) {
        this.appIds = appIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
