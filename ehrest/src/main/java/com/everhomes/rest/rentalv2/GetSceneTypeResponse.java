package com.everhomes.rest.rentalv2;

public class GetSceneTypeResponse {
    private String sceneType;
    private Byte allowRent;


    public Byte getAllowRent() {
        return allowRent;
    }

    public void setAllowRent(Byte allowRent) {
        this.allowRent = allowRent;
    }

    public String getSceneType() {
        return sceneType;
    }

    public void setSceneType(String sceneType) {
        this.sceneType = sceneType;
    }
}
