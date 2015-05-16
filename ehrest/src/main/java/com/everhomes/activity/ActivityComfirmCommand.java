package com.everhomes.activity;

import javax.validation.constraints.NotNull;

public class ActivityComfirmCommand {
    @NotNull
    private Long activityRosterId;

    private Long confirmFamilyId;

    private String targetName;

    public Long getActivityRosterId() {
        return activityRosterId;
    }

    public void setActivityRosterId(Long activityRosterId) {
        this.activityRosterId = activityRosterId;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public Long getConfirmFamilyId() {
        return confirmFamilyId;
    }

    public void setConfirmFamilyId(Long confirmFamilyId) {
        this.confirmFamilyId = confirmFamilyId;
    }

}
