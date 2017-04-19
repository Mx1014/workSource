package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>waitingForExecuting: 待执行</li>
 *     <li>inMaintance: 整改中</li>
 *     <li>complete: 完成</li>
 *     <li>completeWaitingForApproval: 完成待审核</li>
 *     <li>needMaintanceWaitingForApproval: 需整改待审核</li>
 *     <li>reviewQualified: 审核通过</li>
 *     <li>reviewUnqualified: 审核不通过</li>
 * </ul>
 * Created by ying.xiong on 2017/4/18.
 */
public class StatTodayEquipmentTasksResponse {

    private Long waitingForExecuting;

    private Long inMaintance;

    private Long complete;

    private Long completeWaitingForApproval;

    private Long needMaintanceWaitingForApproval;

    private Long reviewQualified;

    private Long reviewUnqualified;

    public Long getComplete() {
        return complete;
    }

    public void setComplete(Long complete) {
        this.complete = complete;
    }

    public Long getInMaintance() {
        return inMaintance;
    }

    public void setInMaintance(Long inMaintance) {
        this.inMaintance = inMaintance;
    }

    public Long getReviewQualified() {
        return reviewQualified;
    }

    public void setReviewQualified(Long reviewQualified) {
        this.reviewQualified = reviewQualified;
    }

    public Long getReviewUnqualified() {
        return reviewUnqualified;
    }

    public void setReviewUnqualified(Long reviewUnqualified) {
        this.reviewUnqualified = reviewUnqualified;
    }

    public Long getCompleteWaitingForApproval() {
        return completeWaitingForApproval;
    }

    public void setCompleteWaitingForApproval(Long completeWaitingForApproval) {
        this.completeWaitingForApproval = completeWaitingForApproval;
    }

    public Long getNeedMaintanceWaitingForApproval() {
        return needMaintanceWaitingForApproval;
    }

    public void setNeedMaintanceWaitingForApproval(Long needMaintanceWaitingForApproval) {
        this.needMaintanceWaitingForApproval = needMaintanceWaitingForApproval;
    }

    public Long getWaitingForExecuting() {
        return waitingForExecuting;
    }

    public void setWaitingForExecuting(Long waitingForExecuting) {
        this.waitingForExecuting = waitingForExecuting;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
