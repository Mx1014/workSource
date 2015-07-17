// @formatter:off
package com.everhomes.business;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>scopeType: 可见范围类型 参考{@link com.everhomes.business.BusinessScopeType}</li>
 * <li>scopeId: 看见范围具体Id，全国为0,城市或小区Id</li>
 * </ul>
 */
public class BusinessScope {

    private String  scopeType;
    private Long    scopeId;
    
    public String getScopeType() {
        return scopeType;
    }

    public void setScopeType(String scopeType) {
        this.scopeType = scopeType;
    }

    public Long getScopeId() {
        return scopeId;
    }

    public void setScopeId(Long scopeId) {
        this.scopeId = scopeId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
