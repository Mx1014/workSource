package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>totalTasks: 任务总数</li>
 *     <li>waitingForExecuting: 待执行任务</li>
 *     <li>inMaintance: 整改中任务</li>
 *     <li>complete: 已执行完成任务</li>
 *     <li>delay: 已延误任务</li>
 *     <li>reviewTasks: 审核任务总数</li>
 *     <li>unReviewedTasks: 待审核任务</li>
 *     <li>reviewedTasks: 已审核任务</li>
 *     <li>reviewDelayTasks: 审核延误任务</li>
 *     <li>reviewQualified: 审核通过任务</li>
 *     <li>reviewUnqualified: 审核不通过任务</li>
 * </ul>
 * Created by ying.xiong on 2017/4/19.
 */
public class StatIntervalAllEquipmentTasksResponse {

    private Long totalTasks;

    private Long waitingForExecuting;

    private Long inMaintance;

    private Long complete;

    private Long delay;

    private Long reviewTasks;

    private Long unReviewedTasks;

    private Long reviewedTasks;

    private Long reviewDelayTasks;

    private Long reviewQualified;

    private Long reviewUnqualified;

    public Long getComplete() {
        return complete;
    }

    public void setComplete(Long complete) {
        this.complete = complete;
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

    public Long getReviewDelayTasks() {
        return reviewDelayTasks;
    }

    public void setReviewDelayTasks(Long reviewDelayTasks) {
        this.reviewDelayTasks = reviewDelayTasks;
    }

    public Long getReviewedTasks() {
        return reviewedTasks;
    }

    public void setReviewedTasks(Long reviewedTasks) {
        this.reviewedTasks = reviewedTasks;
    }

    public Long getReviewQualified() {
        return reviewQualified;
    }

    public void setReviewQualified(Long reviewQualified) {
        this.reviewQualified = reviewQualified;
    }

    public Long getReviewTasks() {
        return reviewTasks;
    }

    public void setReviewTasks(Long reviewTasks) {
        this.reviewTasks = reviewTasks;
    }

    public Long getReviewUnqualified() {
        return reviewUnqualified;
    }

    public void setReviewUnqualified(Long reviewUnqualified) {
        this.reviewUnqualified = reviewUnqualified;
    }

    public Long getTotalTasks() {
        return totalTasks;
    }

    public void setTotalTasks(Long totalTasks) {
        this.totalTasks = totalTasks;
    }

    public Long getUnReviewedTasks() {
        return unReviewedTasks;
    }

    public void setUnReviewedTasks(Long unReviewedTasks) {
        this.unReviewedTasks = unReviewedTasks;
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
