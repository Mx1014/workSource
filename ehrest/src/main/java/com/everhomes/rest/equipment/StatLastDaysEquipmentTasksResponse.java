package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>complete: 完成</li>
 *     <li>completeMaintance: 整改完成</li>
 *     <li>completeInspection: 巡检完成</li>
 *     <li>reviewQualified: 审核通过</li>
 *     <li>reviewUnqualified: 审核不通过</li>
 *     <li>reviewed: 审核任务</li>
 * </ul>
 * Created by ying.xiong on 2017/4/19.
 */
public class StatLastDaysEquipmentTasksResponse {

    private Long complete;

    private Long completeMaintance;

    private Long completeInspection;

    private Long reviewQualified;

    private Long reviewUnqualified;
    
    private Long reviewed;

    public Long getComplete() {
        return complete;
    }

    public void setComplete(Long complete) {
        this.complete = complete;
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

    public Long getReviewed() {
		return reviewed;
	}

	public void setReviewed(Long reviewed) {
		this.reviewed = reviewed;
	}

	@Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
