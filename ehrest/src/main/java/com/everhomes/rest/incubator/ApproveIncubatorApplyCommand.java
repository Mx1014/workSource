package com.everhomes.rest.incubator;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>applyId: applyId</li>
 *     <li>approveStatus: 审批状态，0-待审批，1-拒绝，2-通过 参考 {@link ApproveStatus}</li>
 *     <li>approveOpinion: approveOpinion</li>
 * </ul>
 */
public class ApproveIncubatorApplyCommand {

    private Long applyId;
    private Byte approveStatus;
    private String approveOpinion;

    public Long getApplyId() {
        return applyId;
    }

    public void setApplyId(Long applyId) {
        this.applyId = applyId;
    }

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
