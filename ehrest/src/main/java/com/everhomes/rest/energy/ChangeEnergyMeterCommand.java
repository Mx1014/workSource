package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <ul>
 *     <li>organizationId: 组织id</li>
 *     <li>communityId: 小区id</li>
 *     <li>oldReading: 旧表读数</li>
 *     <li>newReading: 新表读数</li>
 *     <li>maxReading: 新表最大量程</li>
 *     <li>meterId: 表记id</li>
 * </ul>
 */
public class ChangeEnergyMeterCommand {

    @NotNull private Long organizationId;
    @NotNull private Long communityId;
    @NotNull private BigDecimal oldReading;
    @NotNull private BigDecimal newReading;
    private BigDecimal maxReading;
    @NotNull private Long meterId;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public BigDecimal getOldReading() {
        return oldReading;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public void setOldReading(BigDecimal oldReading) {
        this.oldReading = oldReading;
    }

    public BigDecimal getNewReading() {
        return newReading;
    }

    public void setNewReading(BigDecimal newReading) {
        this.newReading = newReading;
    }

    public BigDecimal getMaxReading() {
        return maxReading;
    }

    public void setMaxReading(BigDecimal maxReading) {
        this.maxReading = maxReading;
    }

    public Long getMeterId() {
        return meterId;
    }

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
