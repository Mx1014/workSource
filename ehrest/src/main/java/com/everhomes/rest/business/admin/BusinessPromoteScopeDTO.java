package com.everhomes.rest.business.admin;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>scopeCode: 推荐范围类型，{@link com.everhomes.business.BusinessScopeType}</li>
 * <li>scopeId: 范围id，（城市或小区）</li>
 * <li>regionName: 范围的具体名称，城市名，小区名</li>
 * </ul>
 */

public class BusinessPromoteScopeDTO{
    private java.lang.Byte scopeCode;
    private java.lang.Long scopeId;
    private java.lang.String regionName;
    
    public java.lang.Byte getScopeCode() {
        return scopeCode;
    }

    public void setScopeCode(java.lang.Byte scopeCode) {
        this.scopeCode = scopeCode;
    }

    public java.lang.Long getScopeId() {
        return scopeId;
    }

    public void setScopeId(java.lang.Long scopeId) {
        this.scopeId = scopeId;
    }

    public java.lang.String getRegionName() {
        return regionName;
    }

    public void setRegionName(java.lang.String regionName) {
        this.regionName = regionName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
