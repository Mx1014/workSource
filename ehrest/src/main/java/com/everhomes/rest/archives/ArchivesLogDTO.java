package com.everhomes.rest.archives;

import com.everhomes.util.StringHelper;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Map;

/**
 * <ul>
 * <li>detailId: 员工id</li>
 * <li>operationType: 变动类型 0-入职, 1-转正, 2-调整, 3-离职 参考{@link com.everhomes.rest.archives.ArchivesOperationType}</li>
 * <li>operationTime: 操作日期</li>
 * <li>logs: 补充日志 </li>
 * <li>operatorUid: 操作人id</li>
 * <li>operatorName: 操作人姓名</li>
 * <li>createTime: 记录生成时间</li>
 * </ul>
 */
public class ArchivesLogDTO {

    private Long detailId;

    private Byte operationType;

    private Date operationTime;

    private Map<String, String> logs;

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

    public Map<String, String> getLogs() {
        return logs;
    }

    public void setLogs(Map<String, String> logs) {
        this.logs = logs;
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
