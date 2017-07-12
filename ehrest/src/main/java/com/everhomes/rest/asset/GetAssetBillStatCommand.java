package com.everhomes.rest.asset;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 *     <li>tenantType: 租户类型：family；enterprise</li>
 *     <li>tenantId: 公司id或家庭id</li>
 *     <li>addressId：地址id</li>
 *     <li>targetId：账单所属园区id</li>
 *     <li>targetType：账单所属园区类型</li>
 * </ul>
 * Created by ying.xiong on 2017/4/12.
 */
public class GetAssetBillStatCommand {
    private String tenantType;

    private Long tenantId;

    private Long addressId;

    @NotNull
    private Long targetId;

    @NotNull
    private String targetType;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantType() {
        return tenantType;
    }

    public void setTenantType(String tenantType) {
        this.tenantType = tenantType;
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
