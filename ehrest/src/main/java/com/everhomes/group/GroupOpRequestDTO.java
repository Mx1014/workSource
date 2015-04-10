// @formatter:off
package com.everhomes.group;

import com.everhomes.util.StringHelper;

public class GroupOpRequestDTO {
    private Long id;
    private Long groupId;
    private Long reqestorUid;
    private String requestorName;
    private String requestorAvatar;
    private String requestorComment;
    private Byte operationType;
    private Byte status;
    private Long operatorUid;
    private String processMessage;
    private String createTime;
    private String processTime;

    public GroupOpRequestDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getReqestorUid() {
        return reqestorUid;
    }

    public void setReqestorUid(Long reqestorUid) {
        this.reqestorUid = reqestorUid;
    }

    public String getRequestorName() {
        return requestorName;
    }

    public void setRequestorName(String requestorName) {
        this.requestorName = requestorName;
    }

    public String getRequestorAvatar() {
        return requestorAvatar;
    }

    public void setRequestorAvatar(String requestorAvatar) {
        this.requestorAvatar = requestorAvatar;
    }

    public String getRequestorComment() {
        return requestorComment;
    }

    public void setRequestorComment(String requestorComment) {
        this.requestorComment = requestorComment;
    }

    public Byte getOperationType() {
        return operationType;
    }

    public void setOperationType(Byte operationType) {
        this.operationType = operationType;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Long getOperatorUid() {
        return operatorUid;
    }

    public void setOperatorUid(Long operatorUid) {
        this.operatorUid = operatorUid;
    }

    public String getProcessMessage() {
        return processMessage;
    }

    public void setProcessMessage(String processMessage) {
        this.processMessage = processMessage;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getProcessTime() {
        return processTime;
    }

    public void setProcessTime(String processTime) {
        this.processTime = processTime;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
