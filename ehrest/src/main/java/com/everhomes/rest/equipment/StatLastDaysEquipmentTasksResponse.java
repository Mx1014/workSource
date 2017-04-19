package com.everhomes.rest.equipment;

import com.everhomes.util.StringHelper;

/**
 * <ul>
 *     <li>complete: 完成</li>
 *     <li>reviewQualified: 审核通过</li>
 *     <li>reviewUnqualified: 审核不通过</li>
 * </ul>
 * Created by ying.xiong on 2017/4/19.
 */
public class StatLastDaysEquipmentTasksResponse {

    private Long complete;

    private Long reviewQualified;

    private Long reviewUnqualified;

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

    @Override
    public String toString() {
        return StringHelper.toJsonString(this);
    }
}
