package com.everhomes.rest.community_map;

/**
 * @author sw on 2017/8/19.
 */
public class GetCommunityMapInitDataCommand {
    private String sceneToken;
    private String version;

    public String getSceneToken() {
        return sceneToken;
    }

    public void setSceneToken(String sceneToken) {
        this.sceneToken = sceneToken;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
