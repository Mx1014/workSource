package com.everhomes.rest.energy;

/**
 * Created by ying.xiong on 2018/1/2.
 */
public class SyncOfflineDataCommand {
    private Byte categoryType;

    private Integer namespaceId;

    private Long communityId;

    private Long ownerId;

    private String buildingUpdateTime;

    private String categoryUpdateTime;

    private String taskUpdateTime;

    public String getBuildingUpdateTime() {
        return buildingUpdateTime;
    }

    public void setBuildingUpdateTime(String buildingUpdateTime) {
        this.buildingUpdateTime = buildingUpdateTime;
    }

    public String getCategoryUpdateTime() {
        return categoryUpdateTime;
    }

    public void setCategoryUpdateTime(String categoryUpdateTime) {
        this.categoryUpdateTime = categoryUpdateTime;
    }

    public String getTaskUpdateTime() {
        return taskUpdateTime;
    }

    public void setTaskUpdateTime(String taskUpdateTime) {
        this.taskUpdateTime = taskUpdateTime;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Byte getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(Byte categoryType) {
        this.categoryType = categoryType;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }
}
