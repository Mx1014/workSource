package com.everhomes.rest.promotion;

import java.sql.Timestamp;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>id: 运营推广ID</li>
 * <li>namespaceId: 域空间ID</li>
 * <li>sceneType: 场景类型，参考{@link com.everhomes.rest.ui.user.SceneType}</li>
 * <li>description: 运营推广描述</li>
 * <li>actionType: 联系人名字</li>
 * <li>actionData:头像</li>
 * <li>validCount: 运营推广活动有效次数，0时表示没有次数限制，1次表示看一次后不再显示（服务器端只记录此配置，客户端需要根据该字段来决定展示次数）</li>
 * <li>startTime: 活动开始时间</li>
 * <li>endTime: 活动结束时间</li>
 * <li>status: 活动状态，{@link com.everhomes.rest.promotion.OpPromotionStatus}</li>
 * <li>createTime: 活动创建时间</li>
 * </ul>
 */
public class OpPromotionDTO {
    private Long id;
    private Integer namespaceId;
    private String sceneType;
    private String description;
    private String actionType;
    private String actionData;
    private Integer validCount;
    private Byte status;

    private Timestamp createTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getSceneType() {
        return sceneType;
    }

    public void setSceneType(String sceneType) {
        this.sceneType = sceneType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getActionData() {
        return actionData;
    }

    public void setActionData(String actionData) {
        this.actionData = actionData;
    }

    public Integer getValidCount() {
        return validCount;
    }

    public void setValidCount(Integer validCount) {
        this.validCount = validCount;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
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
