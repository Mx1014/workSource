package com.everhomes.rest.community_map;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/8/14.
 */
public class ListCommunityMapSearchTypesCommand {
    private String sceneToken;

    public String getSceneToken() {
        return sceneToken;
    }

    public void setSceneToken(String sceneToken) {
        this.sceneToken = sceneToken;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
