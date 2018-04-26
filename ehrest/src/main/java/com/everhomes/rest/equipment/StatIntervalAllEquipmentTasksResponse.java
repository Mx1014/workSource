package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>totalTasks: 任务总数</li>
 *     <li>waitingForExecuting: 待执行任务</li>
 *     <li>inMaintance: 整改中任务</li>
 *     <li>completeInspection: 巡检完成</li>
 *     <li>completeMaintance: 整改完成任务</li>
 *     <li>delayInspection: 巡检延误任务</li>
 *     <li>delayInspectionRate: 巡检延误任务率</li>
 *     <li>reviewTasks: 审核任务总数</li>
 *     <li>unReviewedTasks: 待审核任务</li>
 *     <li>reviewedTasks: 已审核任务</li>
 *     <li>reviewDelayTasks: 审核延误任务</li>
 *     <li>reviewQualified: 审核通过任务</li>
 *     <li>reviewUnqualified: 审核不通过任务</li>
 *     <li>maintanceRate: 整改率</li>
 *     <li>reviewQualifiedRate: 审核通过率</li>
 *     <li>reviewDalayRate: 审核延误率</li>
 *     <li>completeReviewDelay: 巡检审批延误数目</li>
 * </ul>
 * Created by ying.xiong on 2017/4/19.
 */
public class StatIntervalAllEquipmentTasksResponse {

    private Long totalTasks;

    private Long waitingForExecuting;

    private Long inMaintance;

    private Long completeInspection;

    private Long completeMaintance;

    private Long delayInspection;

    private Long reviewTasks;

    private Long unReviewedTasks;

    private Long reviewedTasks;

    private Long reviewDelayTasks;

    private Long reviewQualified;

    private Long reviewUnqualified;

    private Double maintanceRate;

    private Double reviewQualifiedRate;

    private Double reviewDalayRate;

    private Long completeReviewDelay;

    private Double delayInspectionRate;

    public Long getCompleteReviewDelay() {
        return completeReviewDelay;
    }

    public void setCompleteReviewDelay(Long completeReviewDelay) {
        this.completeReviewDelay = completeReviewDelay;
    }

    public Double getMaintanceRate() {
        return maintanceRate;
    }

    public void setMaintanceRate(Double maintanceRate) {
        this.maintanceRate = maintanceRate;
    }

    public Double getReviewDalayRate() {
        return reviewDalayRate;
    }

    public void setReviewDalayRate(Double reviewDalayRate) {
        this.reviewDalayRate = reviewDalayRate;
    }

    public Double getReviewQualifiedRate() {
        return reviewQualifiedRate;
    }

    public void setReviewQualifiedRate(Double reviewQualifiedRate) {
        this.reviewQualifiedRate = reviewQualifiedRate;
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

    public Long getDelayInspection() {
        return delayInspection;
    }

    public void setDelayInspection(Long delayInspection) {
        this.delayInspection = delayInspection;
    }


    public Double getDelayInspectionRate() {
        return delayInspectionRate;
    }

    public void setDelayInspectionRate(Double delayInspectionRate) {
        this.delayInspectionRate = delayInspectionRate;
    }

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
