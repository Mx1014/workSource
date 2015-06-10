

package com.everhomes.activity;

import javax.validation.constraints.NotNull;
/**
 * 
 * @author elians
 *<ul>
 *<li>activityRosterId:活动登记ID<li>
 *<li>confirmFamilyId:确认家庭ID</li>
 *<li>targetName：被确认对象</li>
 *</ul>
 */
public class ActivityConfirmCommand {
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
