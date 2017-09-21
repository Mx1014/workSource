package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * <ul>
 * <li>detailId: 员工id</li>
 * <li>operationType: 变动类型 0-入职, 1-转正, 2-调整, 3-离职 参考{@link com.everhomes.rest.archives.ArchivesOperationType}</li>
 * <li>operationTime: 日期</li>
 * <li>operationCategory: 不同的操作类型 </li>
 * 调整类型 参考{@link com.everhomes.rest.archives.ArchivesTransferType}
 * 离职类型 参考{@link com.everhomes.rest.archives.ArchivesDismissType}
 * <li>operationReason: 变动原因</li>
 * <li>operationRemark: 变动备注</li>
 * <li>operatorUid: 操作人id</li>
 * <li>operatorName: 操作人姓名</li>
 * <li>createTime: 记录生成时间</li>
 * </ul>
 */
public class ArchivesLogDTO {

    private Long detailId;

    private Byte operationType;

    private Date operationTime;

    private Byte operationCategory;

    private String operationReason;

    private String operationRemark;

    private Long operatorUid;

    private String operatorName;

    private Timestamp createTime;

    public ArchivesLogDTO() {
    }

    public Long getDetailId() {
        return detailId;
    }

    public void setDetailId(Long detailId) {
        this.detailId = detailId;
    }

    public Byte getOperationType() {
        return operationType;
    }

    public void setOperationType(Byte operationType) {
        this.operationType = operationType;
    }

    public Date getOperationTime() {
        return operationTime;
    }

    public void setOperationTime(Date operationTime) {
        this.operationTime = operationTime;
    }

    public Byte getOperationCategory() {
        return operationCategory;
    }

    public void setOperationCategory(Byte operationCategory) {
        this.operationCategory = operationCategory;
    }

    public String getOperationReason() {
        return operationReason;
    }

    public void setOperationReason(String operationReason) {
        this.operationReason = operationReason;
    }

    public String getOperationRemark() {
        return operationRemark;
    }

    public void setOperationRemark(String operationRemark) {
        this.operationRemark = operationRemark;
    }

    public Long getOperatorUid() {
        return operatorUid;
    }

    public void setOperatorUid(Long operatorUid) {
        this.operatorUid = operatorUid;
    }

    public String getOperatorName() {
        return operatorName;
    }

    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
