// @formatter:off
package com.everhomes.rest.business;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: id</li>
 * <li>ownerId: 商家id</li>
 * <li>scopeType: 可见范围类型 参考{@link com.everhomes.business.BusinessScopeType}</li>
 * <li>scopeId: 看见范围具体Id，全国为0,城市或小区Id</li>
 * <li>scopeName: 范围名称</li>
 * </ul>
 */
public class BusinessVisibleScopeDTO {
    private Long id;
    private Long ownerId;
    private String  scopeType;
    private Long    scopeId;
    private String scopeName;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getScopeName() {
        return scopeName;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }

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
