package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 * <li>totalTasks: 任务总数</li>
 * <li>delayInspection: 巡检延误任务</li>
 * <li>reviewDelayTasks: 审核延误任务数</li>
 * <li>waitingForExecuting: 待执行任务数</li>
 * <li>inMaintance: 整改中任务数</li>
 * <li>completeMaintance: 整改完成任务数</li>
 * <li>completeWaitingForApproval: 巡检完成待审核任务数</li>
 * <li>completeReviewDelay: 巡检完成待审审批延误</li>
 * </ul>
 * Created by ying.xiong on 2017/4/20.
 */
public class TasksStatData {
    private Long totalTasks;

    private Long delayInspection;

    private Long reviewDelayTasks;

    private Long waitingForExecuting;

    private Long inMaintance;

    private Long completeMaintance;

    private Long completeWaitingForApproval;

    private Long completeReviewDelay;

    private Long completeInspectionTasks;

    public Long getDelayInspection() {
        return delayInspection;
    }

    public void setDelayInspection(Long delayInspection) {
        this.delayInspection = delayInspection;
    }

    public Long getWaitingForExecuting() {
        return waitingForExecuting;
    }

    public void setWaitingForExecuting(Long waitingForExecuting) {
        this.waitingForExecuting = waitingForExecuting;
    }

    public Long getCompleteMaintance() {
        return completeMaintance;
    }

    public void setCompleteMaintance(Long completeMaintance) {
        this.completeMaintance = completeMaintance;
    }

    public Long getCompleteWaitingForApproval() {
        return completeWaitingForApproval;
    }

    public void setCompleteWaitingForApproval(Long completeWaitingForApproval) {
        this.completeWaitingForApproval = completeWaitingForApproval;
    }

    public Long getInMaintance() {
        return inMaintance;
    }

    public void setInMaintance(Long inMaintance) {
        this.inMaintance = inMaintance;
    }

    public Long getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(Long totalTasks) {
        this.totalTasks = totalTasks;
    }

    public Long getReviewDelayTasks() {
        return reviewDelayTasks;
    }

    public void setReviewDelayTasks(Long reviewDelayTasks) {
        this.reviewDelayTasks = reviewDelayTasks;
    }

    public Long getCompleteReviewDelay() {
        return completeReviewDelay;
    }

    public void setCompleteReviewDelay(Long completeReviewDelay) {
        this.completeReviewDelay = completeReviewDelay;
    }


    public Long getCompleteInspectionTasks() {
        return completeInspectionTasks;
    }

    public void setCompleteInspectionTasks(Long completeInspectionTasks) {
        this.completeInspectionTasks = completeInspectionTasks;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
