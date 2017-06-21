package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

import javax.validation.constraints.NotNull;

/**
 * <ul>
 * <li>detailId: 员工编号</li>
 * <li>contractNumber: 合同编号</li>
 * <li>startTime: 生效时间</li>
 * <li>endTime: 到期时间</li>
 * </ul>
 */
public class AddOrganizationMemberContractsCommand {

    @NotNull
    private Long detailId;

    @NotNull
    private String contractNumber;

    @NotNull
    private String startTime;

    @NotNull
    private String endTime;

    public AddOrganizationMemberContractsCommand() {
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
