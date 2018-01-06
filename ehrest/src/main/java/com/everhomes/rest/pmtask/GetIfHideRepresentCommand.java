package com.everhomes.rest.pmtask;

/**
 * <ul>
 * <li>namespaceId: 域空间</li>
 * <li>sceneToken: 当前场景</li>
 * </ul>
 */
public class GetIfHideRepresentCommand {
    private Integer namespaceId;
    private String sceneToken;

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
}
