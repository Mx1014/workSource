package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

/**
 * Created by Administrator on 2017/8/14.
 */
public class ListUserRelatedScenesByTypeCommand {
    private String sceneType;

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }

    public String getSceneType() {
        return sceneType;
    }

    public void setSceneType(String sceneType) {
        this.sceneType = sceneType;
    }
}
