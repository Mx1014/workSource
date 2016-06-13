// @formatter:off
package com.everhomes.rest.group;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <p>成为管理员申请信息: 当申请为管理员时，requestor为申请者，无target；
 *    当邀请人成为管理员时，requestor为邀请者、target为被邀请者；</p>
 * <ul>
 * <li>id: 申请记录ID</li>
 * <li>groupId: group id</li>
 * <li>groupName: group名称</li>
 * <li>reqestorUid: 申请人ID</li>
 * <li>requestorName: 申请人名称</li>
 * <li>requestorAvatar: 申请人头像URI</li>
 * <li>requestorAvatarUrl: 申请人头像URL</li>
 * <li>requestorComment: 申请原因</li>
 * <li>operationType: 操作类型</li>
 * <li>status: 成为管理员申请的状态，参考{@link com.everhomes.rest.group.GroupOpRequestStatus}</li>
 * <li>targetType: 目标类型，{@link com.everhomes.entity.EntityType}</li>
 * <li>ID: 目标用户ID</li>
 * <li>operatorUid: 操作人ID</li>
 * <li>processMessage: 操作描述</li>
 * <li>createTime: 记录创建时间</li>
 * <li>processTime: 申请处理时间</li>
 * </ul>
 */
public class GroupOpRequestDTO {
    private Long id;
    private Long groupId;
    private String groupName;
    private Long requestorUid;
    private String requestorName;
    private String requestorAvatar;
    private String requestorAvatarUrl;
    private String requestorComment;
    private Byte operationType;
    private String targetType;
    private Long targetId;
    private Byte status;
    private Long operatorUid;
    private String processMessage;
    private Timestamp createTime;
    private Timestamp processTime;

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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getRequestorUid() {
        return requestorUid;
    }

    public void setRequestorUid(Long requestorUid) {
        this.requestorUid = requestorUid;
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

    public String getRequestorAvatarUrl() {
        return requestorAvatarUrl;
    }

    public void setRequestorAvatarUrl(String requestorAvatarUrl) {
        this.requestorAvatarUrl = requestorAvatarUrl;
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

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Timestamp processTime) {
        this.processTime = processTime;
    }
    
    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
