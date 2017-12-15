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

    public LocalEventContext() {
    }

    public LocalEventContext(String sceneType, Integer namespaceId, Long uid) {
        this.sceneType = sceneType;
        this.namespaceId = namespaceId;
        this.uid = uid;
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
