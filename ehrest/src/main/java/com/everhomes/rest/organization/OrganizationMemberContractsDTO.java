package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * Created by Ryan on 2017/5/19.
 * <ul>
 * <li>memberId: 员工编号</li>
 * <li>contractNumber: 合同编号</li>
 * <li>startTime: 生效时间</li>
 * <li>endTime: 到期时间</li>
 * </ul>
 */
public class OrganizationMemberContractsDTO {

    private Long memberId;

    private String contractNumber;

    private String startTime;

    private String endTime;

    public OrganizationMemberContractsDTO() {
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
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
