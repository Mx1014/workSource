package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

import java.sql.Date;

/**
 * <ul>
 * <li>detailId: 员工detailId</li>
 * <li>contactName: 员工姓名</li>
 * <li>operationType: 操作类型 0-入职 1-转正 2-调动 3-离职 参考{@link com.everhomes.rest.archives.ArchivesOperationType}</li>
 * <li>operationDate: 操作时间</li>
 * <li>status: 状态 0-取消 1-等待 2-已完成 参考{@link com.everhomes.rest.archives.ArchivesOperationStatus}</li>
 * </ul>
 */
public class ArchivesOperationalConfigurationDTO {

    private Long id;

    private Long detailId;

    private String contactName;

    private Byte operationType;

    private Date operationDate;

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

    public Byte getOperationType() {
        return operationType;
    }

    public void setOperationType(Byte operationType) {
        this.operationType = operationType;
    }

    public Date getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(Date operationDate) {
        this.operationDate = operationDate;
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
