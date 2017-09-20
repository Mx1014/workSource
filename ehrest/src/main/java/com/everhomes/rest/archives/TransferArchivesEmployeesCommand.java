package com.everhomes.rest.archives;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Date;
import java.util.List;

/**
 * <ul>
 * <li>detailIds: (List)员工 detailId</li>
 * <li>departmentIds: 部门 id(以数组形式传参)</li>
 * <li>jobPositionIds: 岗位 id</li>
 * <li>organizationId: 工作地点 id</li>
 * <li>effectiveTime: 生效时间</li>
 * <li>transferType: 调整类型: 1-晋升,2-调整,3-其他</li>
 * <li>transferReason: 调整原因</li>
 * </ul>
 */
public class TransferArchivesEmployeesCommand {

    @ItemType(Long.class)
    private List<Long> detailIds;

    @ItemType(Long.class)
    private List<Long> departmentIds;

    @ItemType(Long.class)
    private List<Long> jobPositionIds;

    private Long organizationId;

    private Date effectiveTime;

    private Byte transferType;

    private String transferReason;

    public TransferArchivesEmployeesCommand() {
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

    public Date getEffectiveTime() {
        return effectiveTime;
    }

    public void setEffectiveTime(String effectiveTime) {
        this.effectiveTime = ArchivesUtil.parseDate(effectiveTime);
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
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
}
