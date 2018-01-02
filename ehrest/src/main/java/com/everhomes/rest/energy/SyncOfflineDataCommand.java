package com.everhomes.rest.energy;

/**
 * Created by ying.xiong on 2018/1/2.
 */
public class SyncOfflineDataCommand {
    private Byte categoryType;

    private Integer namespaceId;

    private Long communityId;

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
