// @formatter:off
package com.everhomes.rest.launchpad;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>scopeType: item可见范围类型 参考{@link com.everhomes.rest.common.ScopeType}</li>
 * <li>scopeId: 看见范围具体Id，全国为0,城市或小区Id</li>
 * <li>defaultOrder: 默认顺序</li>
 * <li>applyPolicy: 应用策略{@link com.everhomes.rest.launchpad.ApplyPolicy}</li>
 * </ul>
 */
public class ItemScope {

    private Byte  scopeCode;
    private Long    scopeId;
    private Integer defaultOrder;
    private Byte    applyPolicy;
    
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

    public Integer getDefaultOrder() {
        return defaultOrder;
    }

    public void setDefaultOrder(Integer defaultOrder) {
        this.defaultOrder = defaultOrder;
    }

    public Byte getApplyPolicy() {
        return applyPolicy;
    }

    public void setApplyPolicy(Byte applyPolicy) {
        this.applyPolicy = applyPolicy;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
