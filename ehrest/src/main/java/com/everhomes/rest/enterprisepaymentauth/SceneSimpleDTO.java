package com.everhomes.rest.enterprisepaymentauth;

import com.everhomes.util.StringHelper;


/**
 * <ul>
 * <li>sceneAppId: 场景id  </li>
 * <li>sceneAppName: 场景名称</li>
 * </ul>
 */
public class SceneSimpleDTO {
    private Long sceneAppId;
    private String sceneAppName;

    public Long getSceneAppId() {
        return sceneAppId;
    }

    public void setSceneAppId(Long sceneAppId) {
        this.sceneAppId = sceneAppId;
    }

    public String getSceneAppName() {
        return sceneAppName;
    }

    public void setSceneAppName(String sceneAppName) {
        this.sceneAppName = sceneAppName;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

}
