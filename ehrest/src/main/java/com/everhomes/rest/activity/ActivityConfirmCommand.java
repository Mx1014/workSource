

package com.everhomes.rest.activity;

import javax.validation.constraints.NotNull;
/**
 * 
 * @author elians
 *<ul>
 *<li>rosterId:活动登记ID<li>
 *<li>confirmFamilyId:确认家庭ID</li>
 *<li>targetName：被确认对象</li>
 *</ul>
 */
public class ActivityConfirmCommand {
    @NotNull
    private Long rosterId;

    private Long confirmFamilyId;

    private String targetName;

    public Long getRosterId() {
        return rosterId;
    }

    public void setRosterId(Long rosterId) {
        this.rosterId = rosterId;
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
