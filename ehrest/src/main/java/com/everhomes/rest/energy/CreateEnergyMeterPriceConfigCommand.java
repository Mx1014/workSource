package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <ul>
 *     <li>ownerType: 组织类型</li>
 *     <li>ownerId: 组织id</li>
 *     <li>communityId: 园区id</li>
 *     <li>name: 方案名称</li>
 *     <li>description: 方案说明</li>
 *     <li>expression: 方案表达式，json格式</li>
 * </ul>
 */
public class CreateEnergyMeterPriceConfigCommand {

    @NotNull
    private String ownerType;
    @NotNull
    private Long ownerId;
    @NotNull
    private Long communityId;

    @NotNull @Size(max = 255) private String name;
    @NotNull @Size(max = 512) private String description;
    @NotNull @Size(max = 1024) private String expression;

    public String getOwnerType() {
        return ownerType;
    }

    public void setOwnerType(String ownerType) {
        this.ownerType = ownerType;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
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

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
