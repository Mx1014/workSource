package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 合同信息编号</li>
 * <li>contractNumber: 合同编号</li>
 * <li>startTime: 生效时间</li>
 * <li>endTime: 到期时间</li>
 * </ul>
 */
public class UpdateOrganizationMemberContractsCommand {

    private Long id;

    private String contractNumber;

    private String startTime;

    private String endTime;

    public UpdateOrganizationMemberContractsCommand() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

