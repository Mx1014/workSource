package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

import java.sql.Date;

/**
 * <ul>
 * <li>id: 合同标识号</li>
 * <li>detailId: 员工编号</li>
 * <li>contractNumber: 合同编号</li>
 * <li>startTime: 生效时间</li>
 * <li>endTime: 到期时间</li>
 * </ul>
 */
public class OrganizationMemberContractsDTO {

    private Long id;

    private Long detailId;

    private String contractNumber;

    private Date startTime;

    private Date endTime;

    public OrganizationMemberContractsDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
