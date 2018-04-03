package com.everhomes.rest.ui.user;

import com.everhomes.util.StringHelper;

import java.util.List;

/**
 * <ul>
 *   <li>sceneType:场景类型	</li>
 * </ul>
 *
 */
public class ListUserRelatedScenesByCurrentTypeCommand {
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
