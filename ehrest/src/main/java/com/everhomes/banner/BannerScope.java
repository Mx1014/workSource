// @formatter:off
package com.everhomes.banner;


import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>scopeType: item可见范围类型 参考{@link com.everhomes.banner.BannerScopeType}</li>
 * <li>scopeId: 看见范围具体Id，全国为0,城市或小区Id</li>
 * <li>order: 默认顺序</li>
 * </ul>
 */
public class BannerScope {

    private String  scopeType;
    private Long    scopeId;
    private Integer order;
    
    
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


    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
