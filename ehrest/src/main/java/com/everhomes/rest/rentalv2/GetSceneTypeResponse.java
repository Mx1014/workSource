package com.everhomes.rest.rentalv2;

public class GetSceneTypeResponse {
    private String SceneType;
    private Byte allowRent;

    public String getSceneType() {
        return SceneType;
    }

    public void setSceneType(String sceneType) {
        SceneType = sceneType;
    }

    public Byte getAllowRent() {
        return allowRent;
    }

    public void setAllowRent(Byte allowRent) {
        this.allowRent = allowRent;
    }
}
