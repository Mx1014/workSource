package com.everhomes.rest.promotion;

import com.everhomes.util.StringHelper;

import java.util.List;
import java.sql.Timestamp;

import com.everhomes.discover.ItemType;

/**
 * <ul>
 * <li>title: 标题</li>
 * <li>description: 运营推广描述</li>
 * <li>namespaceId: 域空间ID</li>
 * <li>actionType: 动作类型，参考{@link com.everhomes.rest.promotion.OpPromotionActionType}</li>
 * <li>actionData: 动作类型</li>
 * <li>startTime: 活动开始时间</li>
 * <li>endTime: 活动结束时间</li>
 * <li>status: 活动状态，{@link com.everhomes.rest.promotion.OpPromotionStatus}</li>
 * </ul>
 */
public class OpPromotionActivityDTO {
    private Byte     status;
    private Integer     pushCount;
    private String     policyData;
    private String     description;
    private String     title;
    private Long     creatorUid;
    private Timestamp     createTime;
    private Integer     namespaceId;
    private String     actionData;
    private Byte     actionType;
    private String     pushMessage;
    private Timestamp     startTime;
    private Byte     processStatus;
    private Byte     policyType;
    private String     iconUri;
    private String iconUrl;
    private Timestamp     endTime;
    private Long     id;
    private Integer     processCount;
    private String nickName;
    
    @ItemType(OpPromotionAssignedScopeDTO.class)
    private List<OpPromotionAssignedScopeDTO> assignedScopes;


    public Byte getStatus() {
        return status;
    }


    public void setStatus(Byte status) {
        this.status = status;
    }


    public Integer getPushCount() {
        return pushCount;
    }


    public void setPushCount(Integer pushCount) {
        this.pushCount = pushCount;
    }

    public String getPolicyData() {
        return policyData;
    }


    public void setPolicyData(String policyData) {
        this.policyData = policyData;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public Long getCreatorUid() {
        return creatorUid;
    }


    public void setCreatorUid(Long creatorUid) {
        this.creatorUid = creatorUid;
    }


    public Timestamp getCreateTime() {
        return createTime;
    }


    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }


    public Integer getNamespaceId() {
        return namespaceId;
    }


    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }


    public String getActionData() {
        return actionData;
    }


    public void setActionData(String actionData) {
        this.actionData = actionData;
    }


    public Byte getActionType() {
        return actionType;
    }


    public void setActionType(Byte actionType) {
        this.actionType = actionType;
    }


    public String getPushMessage() {
        return pushMessage;
    }


    public void setPushMessage(String pushMessage) {
        this.pushMessage = pushMessage;
    }


    public Timestamp getStartTime() {
        return startTime;
    }


    public void setStartTime(Timestamp startTime) {
        this.startTime = startTime;
    }


    public Byte getProcessStatus() {
        return processStatus;
    }


    public void setProcessStatus(Byte processStatus) {
        this.processStatus = processStatus;
    }


    public Byte getPolicyType() {
        return policyType;
    }


    public void setPolicyType(Byte policyType) {
        this.policyType = policyType;
    }


    public String getIconUri() {
        return iconUri;
    }


    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }


    public Timestamp getEndTime() {
        return endTime;
    }


    public void setEndTime(Timestamp endTime) {
        this.endTime = endTime;
    }


    public Long getId() {
        return id;
    }


    public void setId(Long id) {
        this.id = id;
    }

    

    public List<OpPromotionAssignedScopeDTO> getAssignedScopes() {
        return assignedScopes;
    }


    public void setAssignedScopes(List<OpPromotionAssignedScopeDTO> assignedScopes) {
        this.assignedScopes = assignedScopes;
    }

    public Integer getProcessCount() {
        return processCount;
    }


    public void setProcessCount(Integer processCount) {
        this.processCount = processCount;
    }


    public String getNickName() {
        return nickName;
    }


    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getIconUrl() {
        return iconUrl;
    }


    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
