package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>currentWaitingForExecuting: 当前待执行</li>
 *     <li>currentWaitingForApproval: 当前待审核</li>
 *     <li>waitingForExecuting: 待执行</li>
 *     <li>inMaintance: 整改中</li>
 *     <li>complete: 当日已完成</li>
 *     <li>reviewed: 当日已审核</li>
 *     <li>completeMaintance: 整改完成</li>
 *     <li>completeInspection: 巡检完成</li>
 *     <li>completeWaitingForApproval: 巡检完成待审核任务数</li>
 *     <li>completeMaintanceWaitingForApproval;: 整改完成待审核任务数</li>
 *     <li>needMaintanceWaitingForApproval: 需整改待审核</li>
 *     <li>reviewQualified: 审核通过</li>
 *     <li>reviewUnqualified: 审核不通过</li>
 * </ul>
 * Created by ying.xiong on 2017/4/18.
 */
public class StatTodayEquipmentTasksResponse {

    private Long currentWaitingForExecuting;

    private Long currentWaitingForApproval;

    private Long waitingForExecuting;

    private Long inMaintance;

    private Long complete;

    private Long reviewed;

    private Long completeMaintance;

    private Long completeInspection;

    private Long completeWaitingForApproval;

    private Long completeMaintanceWaitingForApproval;

    private Long needMaintanceWaitingForApproval;

    private Long reviewQualified;

    private Long reviewUnqualified;

    public Long getCurrentWaitingForExecuting() {
        return currentWaitingForExecuting;
    }

    public void setCurrentWaitingForExecuting(Long currentWaitingForExecuting) {
        this.currentWaitingForExecuting = currentWaitingForExecuting;
    }

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

    public Long getCompleteInspection() {
        return completeInspection;
    }

    public void setCompleteInspection(Long completeInspection) {
        this.completeInspection = completeInspection;
    }

    public Long getCompleteMaintance() {
        return completeMaintance;
    }

    public void setCompleteMaintance(Long completeMaintance) {
        this.completeMaintance = completeMaintance;
    }

    public Long getCurrentWaitingForApproval() {
        return currentWaitingForApproval;
    }

    public void setCurrentWaitingForApproval(Long currentWaitingForApproval) {
        this.currentWaitingForApproval = currentWaitingForApproval;
    }

    public Long getReviewed() {
        return reviewed;
    }

    public void setReviewed(Long reviewed) {
        this.reviewed = reviewed;
    }

    public Long getCompleteMaintanceWaitingForApproval() {
        return completeMaintanceWaitingForApproval;
    }

    public void setCompleteMaintanceWaitingForApproval(Long completeMaintanceWaitingForApproval) {
        this.completeMaintanceWaitingForApproval = completeMaintanceWaitingForApproval;
    }

    @Override

    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
