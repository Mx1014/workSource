package com.everhomes.rest.organization;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>detailId: 员工编号</li>
 * <li>namespaceId: 域名空间</li>
 * <li>contractNumber: 合同编号</li>
 * <li>startTime: 生效时间</li>
 * <li>endTime: 到期时间</li>
 * <li>creatorUid: 创建人</li>
 * </ul>
 */
public class AddOrganizationMemberContractsCommand {

    private Long detailId;

    private Integer namespaceId;

    private String contractNumber;

    private String startTime;

    private String endTime;

    private Long creatorUid;

    public AddOrganizationMemberContractsCommand() {
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
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

    public Long getCreatorUid() {
        return creatorUid;
    }

    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
