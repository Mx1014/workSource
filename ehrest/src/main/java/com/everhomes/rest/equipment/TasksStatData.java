package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>totalTasks: 任务总数</li>
 *     <li>delay: 延误任务数</li>
 *     <li>delayInspection: 巡检延误任务</li>
 *     <li>delayMaintance: 整改延误任务</li>
 *     <li>reviewDelayTasks: 审核延误任务数</li>
 *     <li>waitingForExecuting: 待执行任务数</li>
 *     <li>inMaintance: 整改中任务数</li>
 *     <li>complete: 完成任务数</li>
 *     <li>completeMaintance: 整改完成任务数</li>
 *     <li>completeInspection: 巡检完成任务数</li>
 *     <li>completeWaitingForApproval: 巡检完成待审核任务数</li>
 *     <li>completeMaintanceWaitingForApproval: 整改完成待审核任务数</li>
 *     <li>needMaintanceWaitingForApproval: 需整改待审核任务数</li>
 *     <li>needMaintanceReviewDelay: 整改审核延误</li>
 *     <li>completeReviewDelay: 巡检核查延误</li>
 * </ul>
 * Created by ying.xiong on 2017/4/20.
 */
public class TasksStatData {
    private Long totalTasks;

    private Long delay;

    private Long delayInspection;

    private Long delayMaintance;

    private Long waitingForExecuting;

    private Long inMaintance;

    private Long complete;

    private Long completeMaintance;

    private Long completeInspection;

    private Long completeWaitingForApproval;

    private Long completeMaintanceWaitingForApproval;

    private Long needMaintanceWaitingForApproval;

    private Long reviewDelayTasks;
    private Long needMaintanceReviewDelay;
    private Long completeReviewDelay;

    public Long getDelayInspection() {
        return delayInspection;
    }

    public void setDelayInspection(Long delayInspection) {
        this.delayInspection = delayInspection;
    }

    public Long getDelayMaintance() {
        return delayMaintance;
    }

    public void setDelayMaintance(Long delayMaintance) {
        this.delayMaintance = delayMaintance;
    }

    public Long getWaitingForExecuting() {
        return waitingForExecuting;
    }

    public void setWaitingForExecuting(Long waitingForExecuting) {
        this.waitingForExecuting = waitingForExecuting;
    }

    public Long getComplete() {
        return complete;
    }

    public void setComplete(Long complete) {
        this.complete = complete;
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

    public Long getCompleteWaitingForApproval() {
        return completeWaitingForApproval;
    }

    public void setCompleteWaitingForApproval(Long completeWaitingForApproval) {
        this.completeWaitingForApproval = completeWaitingForApproval;
    }

    public Long getDelay() {
        return delay;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }

    public Long getInMaintance() {
        return inMaintance;
    }

    public void setInMaintance(Long inMaintance) {
        this.inMaintance = inMaintance;
    }

    public Long getNeedMaintanceWaitingForApproval() {
        return needMaintanceWaitingForApproval;
    }

    public void setNeedMaintanceWaitingForApproval(Long needMaintanceWaitingForApproval) {
        this.needMaintanceWaitingForApproval = needMaintanceWaitingForApproval;
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

    public Long getCompleteMaintanceWaitingForApproval() {
        return completeMaintanceWaitingForApproval;
    }

    public void setCompleteMaintanceWaitingForApproval(Long completeMaintanceWaitingForApproval) {
        this.completeMaintanceWaitingForApproval = completeMaintanceWaitingForApproval;
    }

    public Long getCompleteReviewDelay() {
        return completeReviewDelay;
    }

    public void setCompleteReviewDelay(Long completeReviewDelay) {
        this.completeReviewDelay = completeReviewDelay;
    }

    public Long getNeedMaintanceReviewDelay() {
        return needMaintanceReviewDelay;
    }

    public void setNeedMaintanceReviewDelay(Long needMaintanceReviewDelay) {
        this.needMaintanceReviewDelay = needMaintanceReviewDelay;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
