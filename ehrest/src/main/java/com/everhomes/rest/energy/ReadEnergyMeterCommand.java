package com.everhomes.rest.energy;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <ul>
 *     <li>organizationId: 组织id</li>
 *     <li>communityId: 小区id</li>
 *     <li>meterId: 表记id</li>
 *     <li>lastReading: 上次读数</li>
 *     <li>currReading: 这次读数</li>
 *     <li>resetMeterFlag: 是否为复始计量 {@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * </ul>
 */
public class ReadEnergyMeterCommand {

    @NotNull private Long organizationId;
    @NotNull private Long meterId;
    private Long communityId;
    private Byte resetMeterFlag;
    private BigDecimal lastReading;
    @NotNull private BigDecimal currReading;

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Byte getResetMeterFlag() {
        return resetMeterFlag;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public void setResetMeterFlag(Byte resetMeterFlag) {
        this.resetMeterFlag = resetMeterFlag;
    }

    public BigDecimal getLastReading() {
        return lastReading;
    }

    public void setLastReading(BigDecimal lastReading) {
        this.lastReading = lastReading;
    }

    public BigDecimal getCurrReading() {
        return currReading;
    }

    public void setCurrReading(BigDecimal currReading) {
        this.currReading = currReading;
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
