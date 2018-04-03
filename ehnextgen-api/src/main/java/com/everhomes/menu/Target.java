package com.everhomes.menu;

/**
 * Created by sfyan on 2017/6/2.
 */
public class Target {

    private String targetType;

    private Long targetId;

    public Target(){

    }

    public Target(String targetType, Long targetId){
        this.targetType = targetType;
        this.targetId = targetId;
    }

    public String getTargetType() {
        return targetType;
    }

    public void setTargetType(String targetType) {
        this.targetType = targetType;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
}
