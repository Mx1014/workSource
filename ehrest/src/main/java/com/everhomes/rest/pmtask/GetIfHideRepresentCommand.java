package com.everhomes.rest.pmtask;

/**
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>sceneToken: 当前场景</li>
 * <li>appId: 应用id 6为报修 9为投诉与建议</li>
 * </ul>
 */
public class GetIfHideRepresentCommand {
    private Integer namespaceId;
    private String sceneToken;
    private Long appId;

    public Integer getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Integer namespaceId) {
        this.namespaceId = namespaceId;
    }

    public String getSceneToken() {
        return sceneToken;
    }

    public void setSceneToken(String sceneToken) {
        this.sceneToken = sceneToken;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }
}
