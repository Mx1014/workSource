package com.everhomes.rest.community_map;

import com.everhomes.util.StringHelper;

/**
 * @author sw on 2017/8/14.
 */
public class GetCommunityMapBuildingDetailByIdCommand {

    private String sceneToken;

    private Long buildingId;

    public String getSceneToken() {
        return sceneToken;
    }

    public void setSceneToken(String sceneToken) {
        this.sceneToken = sceneToken;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
