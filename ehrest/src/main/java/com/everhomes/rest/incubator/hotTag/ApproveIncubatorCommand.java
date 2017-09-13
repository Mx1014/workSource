package com.everhomes.rest.incubator.hotTag;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>approveStatus: 审批状态，0-待审批，1-拒绝，2-通过</li>
 *     <li>approveOpinion: approveOpinion</li>
 * </ul>
 */
public class ApproveIncubatorCommand {

    private Byte approveStatus;
    private String approveOpinion;

    public Byte getApproveStatus() {
        return approveStatus;
    }

    public void setApproveStatus(Byte approveStatus) {
        this.approveStatus = approveStatus;
    }

    public String getApproveOpinion() {
        return approveOpinion;
    }

    public void setApproveOpinion(String approveOpinion) {
        this.approveOpinion = approveOpinion;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
