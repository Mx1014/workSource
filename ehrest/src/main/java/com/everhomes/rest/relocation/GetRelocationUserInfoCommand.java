package com.everhomes.rest.relocation;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/11/21.
 */
public class GetRelocationUserInfoCommand {
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
