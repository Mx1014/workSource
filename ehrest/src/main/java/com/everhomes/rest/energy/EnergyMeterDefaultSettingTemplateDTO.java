package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import java.math.BigDecimal;

/**
 * <ul>
 *     <li>namespaceId: 域空间</li>
 *     <li>name: 属性名称</li>
 *     <li>settingValue: 属性值</li>
 *     <li>meterType: 表记类型 1:WATER, 2: ELECTRIC, 3: ALL</li>
 *     <li>settingType: 设置类型 1: price, 2: rate, 3: amountFormula, 4: costFormula, 5: dayPrompt, 6: monthPrompt etc</li>
 *     <li>communityId: 园区id</li>
 *     <li>ownerType: 拥有者类型</li>
 *     <li>ownerId: 拥有者id</li>
 * </ul>
 * Created by ying.xiong on 2017/3/22.
 */
public class EnergyMeterDefaultSettingTemplateDTO {

    private Integer namespaceId;
    private String name;
    private BigDecimal settingValue;
    private Byte meterType;
    private Byte settingType;
    private Long communityId;
    private String ownerType;
    private Long ownerId;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Byte getMeterType() {
        return meterType;
    }

    public void setMeterType(Byte meterType) {
        this.meterType = meterType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Byte getSettingType() {
        return settingType;
    }

    public void setSettingType(Byte settingType) {
        this.settingType = settingType;
    }

    public BigDecimal getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(BigDecimal settingValue) {
        this.settingValue = settingValue;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
