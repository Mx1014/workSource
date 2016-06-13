package com.everhomes.rest.activity;

import javax.validation.constraints.NotNull;

import com.everhomes.util.StringHelper;

/**
 * 
 * @author elians
 *<ul>
 *<li>rosterId:登记ID</li>
 *<li>familyId:家庭ID</li>
 *<li>reason:原因</li>
 *</ul>
 */
public class ActivityRejectCommand {

    @NotNull
    private Long rosterId;

    private Long familyId;

    private String reason;

    public Long getRosterId() {
        return rosterId;
    }

    public void setRosterId(Long rosterId) {
        this.rosterId = rosterId;
    }


    public Long getFamilyId() {
        return familyId;
    }

    public void setFamilyId(Long familyId) {
        this.familyId = familyId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
