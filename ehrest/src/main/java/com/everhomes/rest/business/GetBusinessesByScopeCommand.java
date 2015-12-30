package com.everhomes.rest.business;



import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>name: 商家名字</li>
 * <li>displayName: 商家显示名</li>
 * <li>logoUri: 商家logo/li>
 * <li>url: 访问商家信息的url</li>
 * <li>contact: 商家拥有者名字</li>
 * <li>phone: 商家所有在联系方式</li>
 * <li>longitude: 商家地址所在经度</li>
 * <li>latitude: 商家地址所在纬度</li>
 * <li>geohash: 经纬度生成的geohash值</li>
 * <li>address: 商家地址详情</li>
 * <li>description: 商家描述</li>
 * </ul>
 */

public class GetBusinessesByScopeCommand{
    private Byte scopeType;
    private Long scopeId;
    
    
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


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
