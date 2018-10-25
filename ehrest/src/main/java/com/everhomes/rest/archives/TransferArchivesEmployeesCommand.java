package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 * <li>detailIds: (List)员工 detailId</li>
 * <li>departmentIds: 部门 id(以数组形式传参)</li>
 * <li>jobPositionIds: 岗位 ids</li>
 * <li>jobLevelIds: 职级 ids</li>
 * <li>organizationId: 公司 id</li>
 * <li>effectiveTime: 生效时间</li>
 * <li>transferType: 调整类型: 0-晋升,1-调整,2-其他 参考{@link com.everhomes.rest.archives.ArchivesTransferType}</li>
 * <li>transferReason: 调整原因</li>
 * </ul>
 */
public class TransferArchivesEmployeesCommand {

    private Integer namespaceId;

    @ItemType(Long.class)
    private List<Long> detailIds;

    @ItemType(Long.class)
    private List<Long> departmentIds;

    @ItemType(Long.class)
    private List<Long> jobPositionIds;

    @ItemType(Long.class)
    private List<Long> jobLevelIds;

    private Long organizationId;

    private String effectiveTime;

    private Byte transferType;

    private String transferReason;

    public TransferArchivesEmployeesCommand() {
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public List<Long> getDetailIds() {
        return detailIds;
    }

    public void setDetailIds(List<Long> detailIds) {
        this.detailIds = detailIds;
    }

    public Long getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(Long organizationId) {
        this.organizationId = organizationId;
    }

    public String getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(String effectiveTime) {
        this.effectiveTime = effectiveTime;
    }

    public Byte getTransferType() {
        return transferType;
    }

    public void setTransferType(Byte transferType) {
        this.transferType = transferType;
    }

    public String getTransferReason() {
        return transferReason;
    }

    public void setTransferReason(String transferReason) {
        this.transferReason = transferReason;
    }

    public List<Long> getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(List<Long> departmentIds) {
        this.departmentIds = departmentIds;
    }

    public List<Long> getJobPositionIds() {
        return jobPositionIds;
    }

    public void setJobPositionIds(List<Long> jobPositionIds) {
        this.jobPositionIds = jobPositionIds;
    }

    public List<Long> getJobLevelIds() {
        return jobLevelIds;
    }

    public void setJobLevelIds(List<Long> jobLevelIds) {
        this.jobLevelIds = jobLevelIds;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
