package com.everhomes.rest.pmtask;

import java.util.Objects;

/**
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>ownerType: 所属类型</li>
 * <li>ownerId: 所属项目id</li>
 * <li>ownerName: 所属项目名称</li>
 * <li>totalCount: 合计</li>
 * <li>completeCount: 处理完成数量</li>
 * <li>processingCount: 处理中数量</li>
 * <li>closeCount: 已取消数量</li>
 * <li>initCount: 用户发起数量</li>
 * <li>agentCount: 员工代发数量</li>
 * </ul>
 */
public class PmTaskStatDTO {

    private Integer namespaceId;
    private String ownerType;
    private Long ownerId;
    private String ownerName;
    private Integer totalCount;
    private Integer completeCount;
    private Integer processingCount;
    private Integer closeCount;
    private Integer initCount;
    private Integer agentCount;

    public PmTaskStatDTO() {
        super();
        this.totalCount = 0;
        this.completeCount = 0;
        this.processingCount = 0;
        this.closeCount = 0;
        this.initCount = 0;
        this.agentCount = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PmTaskStatDTO that = (PmTaskStatDTO) o;
        return Objects.equals(ownerId, that.ownerId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ownerId);
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

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

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public Integer getCompleteCount() {
        return completeCount;
    }

    public void setCompleteCount(Integer completeCount) {
        this.completeCount = completeCount;
    }

    public Integer getProcessingCount() {
        return processingCount;
    }

    public void setProcessingCount(Integer processingCount) {
        this.processingCount = processingCount;
    }

    public Integer getCloseCount() {
        return closeCount;
    }

    public void setCloseCount(Integer closeCount) {
        this.closeCount = closeCount;
    }

    public Integer getInitCount() {
        return initCount;
    }

    public void setInitCount(Integer initCount) {
        this.initCount = initCount;
    }

    public Integer getAgentCount() {
        return agentCount;
    }

    public void setAgentCount(Integer agentCount) {
        this.agentCount = agentCount;
    }
}
