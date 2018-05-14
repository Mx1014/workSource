package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

import java.sql.Date;

/**
 * <ul>
 * <li>detailId: 员工detailId</li>
 * <li>contactName: 员工姓名</li>
 * <li>operateType: 操作类型 0-入职 1-转正 2-调动 3-离职 参考{@link com.everhomes.rest.archives.ArchivesOperationType}</li>
 * <li>operateDate: 操作时间</li>
 * <li>status: 状态 0-取消 1-等待 2-已完成 参考{@link com.everhomes.rest.archives.ArchivesOperationStatus}</li>
 * </ul>
 */
public class ArchivesOperationalConfigurationDTO {

    private Long id;

    private Long detailId;

    private String contactName;

    private Byte operateType;

    private Date operateDate;

    private Byte status;

    public ArchivesOperationalConfigurationDTO() {
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

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public Byte getOperateType() {
        return operateType;
    }

    public void setOperateType(Byte operateType) {
        this.operateType = operateType;
    }

    public Date getOperateDate() {
        return operateDate;
    }

    public void setOperateDate(Date operateDate) {
        this.operateDate = operateDate;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
