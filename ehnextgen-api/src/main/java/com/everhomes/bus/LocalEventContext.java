package com.everhomes.bus;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>namespaceId: 域空间id</li>
 *     <li>uid: 用户id</li>
 *     <li>sceneType: 场景类型{@link com.everhomes.rest.ui.user.SceneType}</li>
 * </ul>
 */
public class LocalEventContext {

    private Integer namespaceId;
    private Long uid;
    private String sceneType;
    //add by liangming.huang 20180831 新积分系统需要
    private Long communityId;
    private Long appId;

    public LocalEventContext() {
    }

    public LocalEventContext(String sceneType, Integer namespaceId, Long uid) {
        this.sceneType = sceneType;
        this.namespaceId = namespaceId;
        this.uid = uid;
    }

    public Long getCommunityId() {
        return communityId;
    }

    public void setCommunityId(Long communityId) {
        this.communityId = communityId;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getSceneType() {
        return sceneType;
    }

    public void setSceneType(String sceneType) {
        this.sceneType = sceneType;
    }

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
