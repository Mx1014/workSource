// @formatter:off
package com.everhomes.rest.business;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>scopeType: 可见范围类型 参考{@link com.everhomes.rest.common.ScopeType}</li>
 * <li>scopeId: 看见范围具体Id，全国为0,城市或小区Id</li>
 * </ul>
 */
public class BusinessScope {

    private Byte  scopeCode;
    private Long    scopeId;
    
    public Byte getScopeCode() {
		return scopeCode;
	}

	public void setScopeCode(Byte scopeCode) {
		this.scopeCode = scopeCode;
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
