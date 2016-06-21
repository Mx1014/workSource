package com.everhomes.rest.promotion;

import java.sql.Timestamp;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.everhomes.discover.ItemType;
import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>title: 标题</li>
 * <li>description: 运营推广描述</li>
 * <li>namespaceId: 域空间ID</li>
 * <li>actionType: 动作类型，参考{@link com.everhomes.rest.promotion.OpPromotionActionType}</li>
 * <li>actionData: 动作类型</li>
 * <li>startTime: 活动开始时间</li>
 * <li>endTime: 活动结束时间</li>
 * <li>policyType: 推送数据，参考{@link com.everhomes.rest.promotion.OpPromotionConditionType}</li>
 * <li>policyData: 推送数据，参考{@link com.everhomes.rest.promotion.OpPromotionRangePriceData}</li>
 * </ul>
 */
public class CreateOpPromotionCommand {
    @NotNull
    private String     title;
    
    @NotNull
    private String     description;
    
    @NotNull
    private Integer     namespaceId;
    
    @NotNull
    private String     actionData;
    
    @NotNull
    private Byte     actionType;
    
    @NotNull
    private String     pushMessage;
    
    @NotNull
    private Long     startTime;
    
    @NotNull
    private Byte     policyType;
    
    @NotNull
    private String policyData;
    
    @NotNull
    private Long     endTime;
    
    private String   iconUri;
    
    @ItemType(OpPromotionAssignedScopeDTO.class)
    private List<OpPromotionAssignedScopeDTO> assignedScopes;
    
    public String getTitle() {
        return title;
    }


    public void setTitle(String title) {
        this.title = title;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
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

    public Byte getPolicyType() {
        return policyType;
    }


    public void setPolicyType(Byte policyType) {
        this.policyType = policyType;
    }

    public Long getStartTime() {
        return startTime;
    }


    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }


    public Long getEndTime() {
        return endTime;
    }


    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }


    public List<OpPromotionAssignedScopeDTO> getAssignedScopes() {
        return assignedScopes;
    }


    public void setAssignedScopes(List<OpPromotionAssignedScopeDTO> assignedScopes) {
        this.assignedScopes = assignedScopes;
    }
    
    
    public String getPolicyData() {
        return policyData;
    }


    public void setPolicyData(String policyData) {
        this.policyData = policyData;
    }


    public String getIconUri() {
        return iconUri;
    }


    public void setIconUri(String iconUri) {
        this.iconUri = iconUri;
    }


    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
