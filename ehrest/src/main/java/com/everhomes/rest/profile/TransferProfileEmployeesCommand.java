package com.everhomes.rest.profile;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

import java.sql.Date;
import java.util.List;

/**
 * <ul>
 * <li>detailIds: (List)员工 detailId</li>
 * <li>departmentId: 部门 id</li>
 * <li>jobPositionId: 职务 id</li>
 * <li>organizationId: 工作地点 id</li>
 * <li>effectiveTime: 生效时间</li>
 * <li>transferType: 调整类型: 1-晋升,2-调整,3-其他</li>
 * <li>transferReason: 调整原因</li>
 * </ul>
 */
public class TransferProfileEmployeesCommand {

    @ItemType(Long.class)
    private List<Long> detailIds;

    private Long departmentId;

    private Long jobPositionId;

    private Long organizationId;

    private Date effectiveTime;

    private Byte transferType;

    private String transferReason;

    public TransferProfileEmployeesCommand() {
    }

    public List<Long> getDetailIds() {
        return detailIds;
    }

    public void setDetailIds(List<Long> detailIds) {
        this.detailIds = detailIds;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Long getJobPositionId() {
        return jobPositionId;
    }

    public void setJobPositionId(Long jobPositionId) {
        this.jobPositionId = jobPositionId;
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

    public void setEffectiveTime(Date effectiveTime) {
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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
