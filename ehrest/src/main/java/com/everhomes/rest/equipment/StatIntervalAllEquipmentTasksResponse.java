package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>totalTasks: 任务总数</li>
 *     <li>waitingForExecutingTasks: 待执行任务</li>
 *     <li>inMaintanceTasks: 整改中任务</li>
 *     <li>completeTasks: 已执行完成任务</li>
 *     <li>delayTasks: 已延误任务</li>
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

    private Long waitingForExecutingTasks;

    private Long inMaintanceTasks;

    private Long completeTasks;

    private Long delayTasks;

    private Long reviewTasks;

    private Long unReviewedTasks;

    private Long reviewedTasks;

    private Long reviewDelayTasks;

    private Long reviewQualifiedTasks;

    private Long reviewUnqualifiedTasks;

    public Long getCompleteTasks() {
        return completeTasks;
    }

    public void setCompleteTasks(Long completeTasks) {
        this.completeTasks = completeTasks;
    }

    public Long getDelayTasks() {
        return delayTasks;
    }

    public void setDelayTasks(Long delayTasks) {
        this.delayTasks = delayTasks;
    }

    public Long getInMaintanceTasks() {
        return inMaintanceTasks;
    }

    public void setInMaintanceTasks(Long inMaintanceTasks) {
        this.inMaintanceTasks = inMaintanceTasks;
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

    public Long getReviewQualifiedTasks() {
        return reviewQualifiedTasks;
    }

    public void setReviewQualifiedTasks(Long reviewQualifiedTasks) {
        this.reviewQualifiedTasks = reviewQualifiedTasks;
    }

    public Long getReviewTasks() {
        return reviewTasks;
    }

    public void setReviewTasks(Long reviewTasks) {
        this.reviewTasks = reviewTasks;
    }

    public Long getReviewUnqualifiedTasks() {
        return reviewUnqualifiedTasks;
    }

    public void setReviewUnqualifiedTasks(Long reviewUnqualifiedTasks) {
        this.reviewUnqualifiedTasks = reviewUnqualifiedTasks;
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

    public Long getWaitingForExecutingTasks() {
        return waitingForExecutingTasks;
    }

    public void setWaitingForExecutingTasks(Long waitingForExecutingTasks) {
        this.waitingForExecutingTasks = waitingForExecutingTasks;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
