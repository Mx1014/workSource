package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>id: 主键id</li>
 *     <li>ownerId: 拥有者id</li>
 *     <li>ownerType: 拥有者类型</li>
 *     <li>communityId: 园区id</li>
 *     <li>name: 方案名称</li>
 *     <li>description: 方案说明</li>
 *     <li>expression: 区间价格列表 参考{@link EnergyMeterPriceConfigExpressionDTO}</li>
 * </ul>
 */
public class EnergyMeterPriceConfigDTO {

    private Long id;
    private Long ownerId;
    private String ownerType;
    private Long communityId;

    private String name;
    private String description;
    private EnergyMeterPriceConfigExpressionDTO expression;

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

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EnergyMeterPriceConfigExpressionDTO getExpression() {
        return expression;
    }

    public void setExpression(EnergyMeterPriceConfigExpressionDTO expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
