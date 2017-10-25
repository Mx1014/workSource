package com.everhomes.rest.energy;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * <ul>
 *     <li>organizationId: 组织id</li>
 *     <li>communityId: 小区id</li>
 *     <li>taskId: 任务id</li>
 *     <li>lastReading: 上次读数</li>
 *     <li>currReading: 这次读数</li>
 *     <li>resetMeterFlag: 是否为复始计量 {@link com.everhomes.rest.approval.TrueOrFalseFlag}</li>
 * </ul>
 * Created by ying.xiong on 2017/10/20.
 */
public class ReadTaskMeterCommand {
    @NotNull
    private Long organizationId;
    @NotNull
    private Long taskId;

    private Long communityId;
    private Byte resetMeterFlag;
    private BigDecimal lastReading;
    @NotNull private BigDecimal currReading;

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public BigDecimal getCurrReading() {
        return currReading;
    }

    public void setCurrReading(BigDecimal currReading) {
        this.currReading = currReading;
    }

    public BigDecimal getLastReading() {
        return lastReading;
    }

    public void setLastReading(BigDecimal lastReading) {
        this.lastReading = lastReading;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public Byte getResetMeterFlag() {
        return resetMeterFlag;
    }

    public void setResetMeterFlag(Byte resetMeterFlag) {
        this.resetMeterFlag = resetMeterFlag;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
